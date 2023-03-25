package selfdao;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import selfdao.SqlCmdType.Create;
import selfdao.SqlCmdType.Delete;
import selfdao.SqlCmdType.Read;
import selfdao.SqlCmdType.Update;
import selfdao.jdbc.JdbcExecutor;


/**
 * An instance Builder serves all the request for the same Dao. 
 * 
 * @author mcarvalho
 */
public class Builder implements InvocationHandler, SqlCmdProcessor{

	/*================================================================
	 * -------------------------    STATIC   ------------------------- 
	 *================================================================*/
	private final static Method closeMethod;
	
	static{
		try {
			closeMethod = AutoCloseable.class.getMethod("close");
		} catch (NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static BuilderOfDao with(final JdbcExecutor exec){
		
		return new BuilderOfDao() {
			
			@SuppressWarnings("unchecked")
			@Override
			public <T> T of(Class<? extends T> src) {
				if(!src.isInterface()) 
					throw new IllegalArgumentException("The src Class argument must be an interface!");

				if(!Arrays.asList(src.getInterfaces()).contains(AutoCloseable.class))
					throw new IllegalArgumentException("The src Class must inherit from Autocloseable!");
				
				DomainEntity entityMark = src.getAnnotation(DomainEntity.class);
				if(entityMark == null) 
					throw new IllegalArgumentException("The src Class argument must be annotated with DomainEntity!");
				
				return (T) Proxy.newProxyInstance(
						src.getClassLoader(),
		                new Class[] { src },
		                new Builder(exec, entityMark));
			}
		};
	}

	/*================================================================
	 * -------------------------  INSTANCE   ------------------------- 
	 *================================================================*/
	private final JdbcExecutor exec;
	private final DomainEntity entityMark;
	
	/*
	 * Two Methods are the same if they were declared by 
	 * the same class and have the same name and formal 
	 * parameter types and return type.
	 */
	private final Map<Method, InvocationHandler> targetCache = new HashMap<Method, InvocationHandler>();
	private final Map<Object, Object> identityMap = new HashMap<Object, Object>();
	
	private Builder(JdbcExecutor exec, DomainEntity entityMark) {
		this.exec = exec;
		this.entityMark = entityMark;
	}

	@Override
	public Object invoke(Object proxy, Method m, Object[] args) throws Throwable{
		InvocationHandler target = targetCache.get(m);
		if(target == null){
			if(m.equals(closeMethod)){
				target = buildCloseHandler();
			}else{
				SqlCmd cmdMark = m.getAnnotation(SqlCmd.class);
				target = cmdMark.type().newInstance().visit(this, m, args);
			}
			targetCache.put(m, target);
		}
		return target.invoke(proxy, m, args);
	}

	private InvocationHandler buildCloseHandler() {
		return new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method target, Object[] args) throws Throwable {
				exec.close();
				return null;
			}
		};
	}

	@Override
	public InvocationHandler process(Read cmdType, Method m, Object[] args) {
		return new HandlerRead(exec, entityMark.value(), entityMark.key(), identityMap );
	}

	@Override
	public InvocationHandler process(Create cmdType, Method m, Object[] args) {
		return new HandlerInsert(exec, entityMark.value(), entityMark.key());
	}

	@Override
	public InvocationHandler process(Update cmdType, Method m, Object[] args) {
		return new HandlerUpdate(exec);
	}

	@Override
	public InvocationHandler process(Delete cmdType, Method m, Object[] args) {
		return new HandlerUpdate(exec);
	}

	
}
