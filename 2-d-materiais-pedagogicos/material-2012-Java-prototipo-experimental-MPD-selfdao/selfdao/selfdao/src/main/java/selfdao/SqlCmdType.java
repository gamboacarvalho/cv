package selfdao;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


public abstract class SqlCmdType {

	private SqlCmdType() {
	}

	abstract InvocationHandler visit(SqlCmdProcessor proc, Method m, Object[] args);
	
	public final static class Create extends SqlCmdType{
		public InvocationHandler visit(SqlCmdProcessor proc, Method m, Object[] args){
			return proc.process(this, m, args);
		}
	}
	
	public final static class Read extends SqlCmdType{
		public InvocationHandler visit(SqlCmdProcessor proc, Method m, Object[] args){
			return proc.process(this, m, args);
		}
	}
	
	public final static class Update extends SqlCmdType{
		public InvocationHandler visit(SqlCmdProcessor proc, Method m, Object[] args){
			return proc.process(this, m, args);
		}
	}
	
	public final static class Delete extends SqlCmdType{
		public InvocationHandler visit(SqlCmdProcessor proc, Method m, Object[] args){
			return proc.process(this, m, args);
		}
	}

}
