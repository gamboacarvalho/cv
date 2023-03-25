package naivecontainer;

import java.lang.reflect.InvocationTargetException;

import naivecontainer.exceptions.NaiveContainerConfigurationException;

public class SingletonBinding<T> extends TypeBinding<T>{
	
	public SingletonBinding(Dependency<T> key, Class<? extends T> concrete){
		super(key,concrete);
	}

	private T _instance = null;	
	
	@Override
	public T getInstance(Injector injector)
			throws NaiveContainerConfigurationException,
			InvocationTargetException {
		if(_instance != null) return _instance;
		return _instance = super.getInstance(injector);
	}
}
