package naivecontainer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import naivecontainer.exceptions.InvalidConstructorException;
import naivecontainer.exceptions.NaiveContainerConfigurationException;
import naivecontainer.exceptions.UnexpectedException;

/** Decorates Class<T> with some context specific methods */

public class ClassDecorator<T> {
	private final Class<T> _klass;
	Constructor<T> _ctor;
	
	public Class<T> getDecoratedClass(){
		return _klass;
	}

	@SuppressWarnings("unchecked")
	public ClassDecorator(Class<T> klass) throws NaiveContainerConfigurationException{
		_klass = klass;
		Constructor<T>[] ctors = (Constructor<T>[]) klass.getConstructors();
		if(ctors.length != 1) throw new InvalidConstructorException(klass);
		_ctor = ctors[0];
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Dependency<?>[] getDependencies(){
		
		Annotation[][] annots = _ctor.getParameterAnnotations();
		Class<?>[] types = _ctor.getParameterTypes();
		Dependency<?>[] ret = new Dependency<?>[types.length];
		for(int i = 0 ; i<ret.length ; ++i){
			String name = null;
			for(int j = 0 ; j<annots[i].length ; ++j){
				if(annots[i][j].annotationType().equals(Named.class)){
					name = ((Named)annots[i][j]).value();
					break;
				}
			}
			ret[i] = name == null ? new Dependency(types[i]) : new NamedDependency(name, types[i]);
		}
		return ret;
	}
	
	public T newInstance(Object[] args) throws InvocationTargetException {
		try {
			return _ctor.newInstance(args);
		} catch (IllegalArgumentException e) {
			throw new UnexpectedException(e);
		} catch (InstantiationException e) {
			throw new UnexpectedException(e);
		} catch (IllegalAccessException e) {
			throw new UnexpectedException(e);
		} 
	}
}
