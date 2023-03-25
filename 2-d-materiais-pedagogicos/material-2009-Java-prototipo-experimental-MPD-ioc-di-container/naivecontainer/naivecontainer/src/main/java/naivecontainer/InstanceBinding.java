package naivecontainer;

/** A binding from a dependency type into an instance */
public class InstanceBinding<T> implements Binding<T> {

	public final Dependency<T> _key;
	public final T  _instance;
	
	public InstanceBinding(Dependency<T> key, T instance){
		_key = key;
		_instance = instance;
	}
	
	public InstanceBinding(Class<T> klass, T instance){
		_key = new Dependency<T>(klass);
		_instance = instance;
	}

	@Override
	public Dependency<T> getDependency() {
		return _key;
	}

	@Override
	public T getInstance(Injector injector) {
		return _instance;
	}	
}
