package selfdao;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import selfdao.jdbc.JdbcBinder;
import selfdao.jdbc.JdbcCmd;
import selfdao.jdbc.JdbcCmdTemplate;
import selfdao.jdbc.JdbcConverter;
import selfdao.jdbc.JdbcExecutor;

public class HandlerUpdate implements InvocationHandler {

	private final JdbcExecutor exec;
	private JdbcCmd<?> cmd;
	
	public HandlerUpdate(JdbcExecutor exec) {
		this.exec = exec;
	}

	@Override
	public Object invoke(Object proxy, Method m, Object[] args) throws Throwable{
		exec.executeUpdate(buildCmd(m), args);
		return null;
	}

	private JdbcCmd<Object> buildCmd(Method m) {
		SqlCmd cmdMark = m.getAnnotation(SqlCmd.class);
		String sql = cmdMark.cmd();
		return new JdbcCmdTemplate<>(sql, null, buildBinders(m.getParameterTypes()));
	}
	
	private JdbcBinder<?>[] buildBinders(Class<?>[] argsTypes){
		int size = argsTypes != null? argsTypes.length: 0;
		JdbcBinder<?>[] binders = new JdbcBinder<?>[size];
		for (int i = 0; i < size; i++) {
			binders[i] = JdbcBinder.of(argsTypes[i]);
		}
		return binders;
	}

}
