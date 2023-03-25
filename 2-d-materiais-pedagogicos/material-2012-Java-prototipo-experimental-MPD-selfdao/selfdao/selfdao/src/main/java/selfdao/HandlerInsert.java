package selfdao;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;

import selfdao.jdbc.JdbcBinder;
import selfdao.jdbc.JdbcCmd;
import selfdao.jdbc.JdbcCmdTemplate;
import selfdao.jdbc.JdbcConverter;
import selfdao.jdbc.JdbcExecutor;
import selfdao.jdbc.JdbcExtractor;

public class HandlerInsert<T> implements InvocationHandler {

	private final JdbcExecutor exec;
	private final Class<T> domainClass;
	private final String key;
	private Class<?> keyType;
	private JdbcCmd<?> cmd; // JdbcCmd<K>, where K is the type of the key of T.
	private Constructor<T> ctor;
	
	public HandlerInsert(JdbcExecutor exec, Class<T> value, String key) {
		this.exec = exec;
		this.domainClass = value;
		this.key = key;
	}

	@Override
	public Object invoke(Object proxy, Method m, Object[] args) throws Throwable{
		Constructor<T> c = buildCtor(m);
		Object key = exec.executeInsert(buildCmd(m), args);
		Object[] ctorArgs = new Object[args.length + 1] ;
		System.arraycopy(args, 0, ctorArgs, 1, args.length);
		ctorArgs[0] = key;
		return c.newInstance(ctorArgs);
	}

	private JdbcCmd<Object> buildCmd(Method m) {
		SqlCmd cmdMark = m.getAnnotation(SqlCmd.class);
		String sql = cmdMark.cmd();
		return new JdbcCmdTemplate(sql, buildConverter(), buildBinders(m.getParameterTypes()));
	}

	private JdbcConverter<?> buildConverter() {
		Class<?> kClass = searchKeyType();
		final JdbcExtractor<?> ext = JdbcExtractor.get(kClass);
		return new JdbcConverter() {
			@Override
			public Object convert(ResultSet rs) throws SQLException {
				return ext.extract(rs, 1);
			}
		};
	}

	private JdbcBinder<?>[] buildBinders(Class<?>[] argsTypes){
		int size = argsTypes != null? argsTypes.length: 0;
		JdbcBinder<?>[] binders = new JdbcBinder<?>[size];
		for (int i = 0; i < size; i++) {
			binders[i] = JdbcBinder.of(argsTypes[i]);
		}
		return binders;
	}

	private Constructor<T> buildCtor(Method m) {
		if(ctor == null){
			Class<?>[] restArgs = m.getParameterTypes();
			Class<?>[] ctorArgs = new Class<?>[restArgs.length + 1];
			ctorArgs[0] = searchKeyType();
			System.arraycopy(restArgs, 0, ctorArgs, 1, restArgs.length);
			try {
				ctor = domainClass.getConstructor(ctorArgs);
			} catch (NoSuchMethodException | SecurityException e) {
				throw new RuntimeException();
			}
		}
		return ctor;
	}

	private Class<?> searchKeyType() {
		if(keyType == null){
			for (Method m : domainClass.getMethods()) {
				if(m.getName().equalsIgnoreCase("get" + key)){
					keyType = m.getReturnType();
					return keyType;
				}
			}
			throw new IllegalArgumentException(
					String.format("The domain class %s does not a contain a valid key with the name %s!", 
							domainClass.toString(),
							key));
		}
		return keyType;
	}

}
