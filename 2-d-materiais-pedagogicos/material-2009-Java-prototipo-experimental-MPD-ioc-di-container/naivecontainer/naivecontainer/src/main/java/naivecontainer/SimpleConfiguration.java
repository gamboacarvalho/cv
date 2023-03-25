package naivecontainer;

import java.util.Collection;
import java.util.LinkedList;

/* A simple configuration with fluent interface support */
public class SimpleConfiguration implements InjectorConfiguration {

	private final Collection<Binding<?>> _bindings = new LinkedList<Binding<?>>();
	
	@Override
	public final void configure(Injector injector) {
		configure();
		for(Binding<?> b : _bindings){
			injector.addBinding(b);
		}
	}
	/**
	 * Hook method. It should be abstract, but we keep it
	 * with an empty body to avoid turning those class abstract
	 * and break retro-compatibility.   
	 */
	protected void configure() {
		
	}
	public static SimpleConfiguration with(){
		return new SimpleConfiguration();
	}
	
	public class DependencyBinding<T>{
		public Dependency<T> _dependency;
		public DependencyBinding(Class<T> klass){
			_dependency = new Dependency<T>(klass);
		}
		
		public DependencyBinding<T> withName(String name){
			_dependency = new NamedDependency<T>(name, _dependency.getType());
			return this;
		}
		
		public SimpleConfiguration to(Class<? extends T> t){
			_bindings.add(new TypeBinding<T>(_dependency, t));
			return SimpleConfiguration.this;
		}
		public <K extends T> SimpleConfiguration to(K k){
			_bindings.add(new InstanceBinding<T>(_dependency, k));
			return SimpleConfiguration.this;
		}
		public SimpleConfiguration toSingleton(Class<? extends T> k){
			_bindings.add(new SingletonBinding<T>(_dependency, k));
			return SimpleConfiguration.this;
		}

		public SimpleConfiguration toProvider(
				Class<? extends Provider<? extends T>> providerType) {
			_bindings.add(new ProviderBinding<T>(_dependency, providerType));
			return SimpleConfiguration.this;
		}
	}
	
	public <T> DependencyBinding<T> bind(Class<T> klass){
		return new DependencyBinding<T>(klass);
	}
	
}
