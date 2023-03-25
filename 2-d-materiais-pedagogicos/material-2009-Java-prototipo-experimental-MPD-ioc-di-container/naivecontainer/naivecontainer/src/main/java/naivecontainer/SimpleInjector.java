package naivecontainer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Hashtable;
import java.util.Map;

import naivecontainer.exceptions.NaiveContainerConfigurationException;
import naivecontainer.exceptions.NaiveContainerException;
import naivecontainer.exceptions.UnbindedTypeException;

/* A simple injector */
public class SimpleInjector implements Injector {
	
	private final Map<Dependency<?>, Binding<?>> _bindings = new Hashtable<Dependency<?>,Binding<?>>();	
	
	public SimpleInjector(){
        Dependency<Injector> dep = new Dependency<Injector>(Injector.class);
        _bindings.put(dep, new InstanceBinding<Injector>(dep, this));
    }
	
	public SimpleInjector(InjectorConfiguration cfg) throws NaiveContainerException{
        this();
		cfg.configure(this);
	}
	
	public SimpleInjector withBinding(Binding<?> b){
		_bindings.put(b.getDependency(), b);
		return this;
	}
	
	@Override
	public void addBinding(Binding<?> b) {
		withBinding(b);		
	}

	@Override
	public <T> T getInstanceOfExactType(Class<T> type) throws NaiveContainerConfigurationException, InvocationTargetException {
		ClassDecorator<T> dec = new ClassDecorator<T>(type);
		Dependency<?>[] deps = dec.getDependencies();
		Object[] objs = new Object[deps.length];
		for(int i = 0 ; i<objs.length ; ++i){
			objs[i] = getInstance(deps[i]);			
		}
		return dec.newInstance(objs);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getInstance(Dependency<T> dep) throws NaiveContainerConfigurationException, InvocationTargetException {
		Binding<T> b = (Binding<T>) _bindings.get(dep);
		if(b != null) 
			return b.getInstance(this);
		Class<T> type = dep.getType();
		int mod = type.getModifiers();
		if(!Modifier.isAbstract(mod) && !Modifier.isInterface(mod)) 
			return this.getInstanceOfExactType(type);
		
		throw new UnbindedTypeException(type);		
	}

	@Override
	public <T> T getInstance(Class<T> type)
			throws NaiveContainerConfigurationException,
			InvocationTargetException {
		// TODO Auto-generated method stub
		return getInstance(new Dependency<T>(type));
	}
}
