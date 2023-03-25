package naivecontainer;

import java.lang.reflect.InvocationTargetException;

public interface Provider<T> {
	T provide() throws InvocationTargetException;
}
