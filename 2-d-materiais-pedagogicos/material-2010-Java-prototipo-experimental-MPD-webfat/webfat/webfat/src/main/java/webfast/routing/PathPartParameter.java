package webfast.routing;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Represents an optional parameter of a path.
 * @author mcarvalho
 * @param <T>
 */
public class PathPartParameter<T> implements PathPart {
	final Method paramValueOf; 
	T paramValue;

	public PathPartParameter(Class<T> paramType) throws SecurityException, NoSuchMethodException {
	  if(paramType != String.class){
	  	paramValueOf = paramType.getMethod("valueOf", String.class);
	  	if((paramValueOf.getModifiers() & Modifier.STATIC) == 0)
	  		throw new IllegalArgumentException("ValueOf method of " + paramType + " class is not static");
	  }else{
	  	paramValueOf = null;
	  }
  }
	/**
	 * Even if the part argument is null this method returns true
	 * because this class represents an optional parameter. In that case
	 * the paramValue will be null too.
	 */
	@SuppressWarnings("unchecked")
  @Override
	public boolean match(String part) throws IllegalAccessException{
		if(part != null){
			try {
				if(paramValueOf != null){
					paramValue = (T) paramValueOf.invoke(null, part);
				}else{
					paramValue = (T) part;
				}
			} catch (InvocationTargetException e) {
				return false;
			}catch (IllegalArgumentException e) {
				return false;
			}
		}
		return true;
	}
	public T getParamValue(){
		return paramValue;
	}
}
