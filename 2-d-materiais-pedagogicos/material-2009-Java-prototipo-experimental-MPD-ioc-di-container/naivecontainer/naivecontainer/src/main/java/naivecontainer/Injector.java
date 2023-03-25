package naivecontainer;

import java.lang.reflect.InvocationTargetException;

import naivecontainer.exceptions.NaiveContainerConfigurationException;
import naivecontainer.exceptions.UnbindedTypeException;

/** An injector creates instances and injects dependencies into them */
public interface Injector {
	public <T> T getInstanceOfExactType(Class<T> type) throws NaiveContainerConfigurationException, InvocationTargetException, UnbindedTypeException;
	public <T> T getInstance(Dependency<T> type) throws NaiveContainerConfigurationException, InvocationTargetException;
	public <T> T getInstance(Class<T> type) throws NaiveContainerConfigurationException, InvocationTargetException;
	
	public void addBinding(Binding<?> b);
}
