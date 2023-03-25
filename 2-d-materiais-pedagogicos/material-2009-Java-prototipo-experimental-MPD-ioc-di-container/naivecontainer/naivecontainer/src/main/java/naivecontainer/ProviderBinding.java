package naivecontainer;

import java.lang.reflect.InvocationTargetException;

import naivecontainer.exceptions.NaiveContainerConfigurationException;

public class ProviderBinding<T> implements Binding<T> {
	
	public ProviderBinding(
			Dependency<T> dependency, 
			Class<? extends Provider<? extends T>> providerType){
		_dependency = dependency;
		_provType = providerType;
	}	

	private final Dependency<T> _dependency;	
	@Override
	public Dependency<T> getDependency() {
		return _dependency;
	}

	private final Class<? extends Provider<? extends T>> _provType;
	@Override
	public T getInstance(Injector injector)
			throws NaiveContainerConfigurationException,
			InvocationTargetException {
		Provider<? extends T> prov = injector.getInstance(_provType);
		return prov.provide();
	}	
}
