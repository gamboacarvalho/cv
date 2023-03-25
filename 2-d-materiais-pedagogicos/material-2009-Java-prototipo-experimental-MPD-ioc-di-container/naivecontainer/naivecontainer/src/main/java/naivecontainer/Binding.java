package naivecontainer;

import java.lang.reflect.InvocationTargetException;

import naivecontainer.exceptions.NaiveContainerConfigurationException;

/** Defines a mapping between a dependency type and a concrete type */
public interface Binding<T> {
	Dependency<T> getDependency();
	T getInstance(Injector injector) throws NaiveContainerConfigurationException, InvocationTargetException;
}
