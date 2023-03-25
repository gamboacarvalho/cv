package selfdao;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;

import selfdao.jdbc.JdbcBinder;
import selfdao.jdbc.JdbcCmd;
import selfdao.jdbc.JdbcCmdTemplate;
import selfdao.jdbc.JdbcConverter;
import selfdao.jdbc.JdbcExecutor;
import selfdao.jdbc.JdbcExtractor;

interface Action{
	Object doIt(Object[] args) throws SQLException;
}

public class HandlerRead implements InvocationHandler {

	private final JdbcExecutor exec;
	private final Class<?> domainClass;
	private final KeyGetter key;
	private Action action; 
	private Map<Object, Object> identityMap;
	
	public HandlerRead(JdbcExecutor exec, Class<?> domainClass, String key, Map<Object, Object> identityMap) {
		this.exec = exec;
		this.domainClass = domainClass;
		this.identityMap = identityMap;
		key = Character.toUpperCase(key.charAt(0)) + key.substring(1);
		try {
			final Method mKey = this.domainClass.getDeclaredMethod("get" + key);
			mKey.setAccessible(true);
			this.key = new KeyGetter() {
				public Object get(Object val) {
					try {
						return mKey.invoke(val);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						throw new RuntimeException(e);
					}
				}
			};
		} catch (NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object invoke(Object proxy, Method m, Object[] args) throws Throwable{
		return buildAction(m).doIt(args);
	}
	
	private Action buildAction(Method m) {
		if(action == null){
			JdbcCmd<?> cmd = buildSqlCmd(m);
			Class<?> retType = m.getReturnType();
			if(Iterable.class.isAssignableFrom(retType))
				action = buildActionReturnIterable(cmd);
			else if(domainClass.isAssignableFrom(retType))
				action = buildActionReturnDomainObject(cmd);
			else
				throw new IllegalArgumentException("Accessor or method does not return a valid type. It must return Iterable or " + domainClass);
		}
		return action;
	}

	private Action buildActionReturnDomainObject(final JdbcCmd<?> cmd) {
		return new Action() {
			@Override
			public Object doIt(Object[] args) throws SQLException{ 
				Iterator<?> res = exec.executeQuery(cmd, args).iterator();
				return res.hasNext()? res.next() : null;
			}
		};
	}

	private Action buildActionReturnIterable(final JdbcCmd<?> cmd) {
		return new Action() {
			@Override
			public Object doIt(Object[] args) throws SQLException{ 
				return exec.executeQuery(cmd, args);
			}
		};
	}

	private JdbcCmd buildSqlCmd(Method m){
		SqlCmd cmdMark = m.getAnnotation(SqlCmd.class);
		String sql = cmdMark.cmd();
		JdbcConverter<?> conv = buildConverter(sql);
		return new JdbcCmdTemplate<>(sql, conv, buildBinders(m.getParameterTypes()));
	}
	
	private JdbcBinder<?>[] buildBinders(Class<?>[] argsTypes){
		int size = argsTypes != null? argsTypes.length: 0;
		JdbcBinder<?>[] binders = new JdbcBinder<?>[size];
		for (int i = 0; i < size; i++) {
			binders[i] = JdbcBinder.of(argsTypes[i]);
		}
		return binders;
	}
	
	@SuppressWarnings("rawtypes")
	private JdbcConverter<?> buildConverter(final String sql){
		return new JdbcConverter() {
			
			JdbcConverter<?> cache ;
			
			@Override
			public Object convert(ResultSet rs) throws SQLException {
				if(cache == null){
					cache = buildConverter(rs);
				}
				return cache.convert(rs);
			}
			
			private JdbcConverter<?> buildConverter(ResultSet rs){
				int nrCtorArgs;
				try {
					nrCtorArgs = rs.getMetaData().getColumnCount();
					for(Constructor<?> ctor : domainClass.getConstructors()){
						if(nrCtorArgs == ctor.getParameterTypes().length){
							return JdbcExtractor.build(ctor, key, identityMap);
						}
					}
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}

				throw new IllegalArgumentException(String.format("Domain class %s does not provide any constructor compatible with query: %s", 
						domainClass,
						sql));
				}
		};
	}
}
