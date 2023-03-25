package selfdao;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import selfdao.SqlCmdType.Create;
import selfdao.SqlCmdType.Delete;
import selfdao.SqlCmdType.Read;
import selfdao.SqlCmdType.Update;

public interface SqlCmdProcessor {

	InvocationHandler process(Read cmdType, Method m, Object[] args);

	InvocationHandler process(Create cmdType, Method m, Object[] args);

	InvocationHandler process(Update cmdType, Method m, Object[] args);

	InvocationHandler process(Delete cmdType, Method m, Object[] args);
	
}
