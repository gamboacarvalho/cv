package naivecontainer;

import java.lang.reflect.InvocationTargetException;

import naivecontainer.exceptions.NaiveContainerConfigurationException;

/** A binding from a dependency type into another type */
public class TypeBinding<T> implements Binding<T> {

	public final Dependency<T> _key;
	public final Class<? extends T> _concrete;
	
	public TypeBinding(Dependency<T> key, Class<? extends T> concrete){
		_key = key;
		_concrete = concrete;
	}
	
	public TypeBinding(Class<T> klass, Class<? extends T> concrete){
		_key = new Dependency<T>(klass);
		_concrete = concrete;
	}

	@Override
	public Dependency<T> getDependency() {
		return _key;
	}

	@Override
	public T getInstance(Injector injector) throws NaiveContainerConfigurationException, InvocationTargetException {
		return injector.getInstanceOfExactType(_concrete);
	}	
}
