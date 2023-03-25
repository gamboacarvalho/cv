package naivecontainer;

import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

import naivecontainer.exceptions.InvalidConstructorException;
import naivecontainer.exceptions.NaiveContainerConfigurationException;
import naivecontainer.exceptions.UnbindedTypeException;

public class ExceptionTests {
	
	public static class DependentWithTwoConstructors{
		public DependentWithTwoConstructors(){}
		public DependentWithTwoConstructors(int i){}		
	}
	
	@Test(expected=InvalidConstructorException.class)
	public void resolving_a_class_with_more_than_one_constructor_throws_invalid_constructor_exception() throws NaiveContainerConfigurationException, InvocationTargetException{
		new SimpleInjector().getInstance(DependentWithTwoConstructors.class);		
	}
	
	public interface UnbindedDependency{}
	
	public static class DependentWithUnbindedDependency{
		public DependentWithUnbindedDependency(UnbindedDependency d){}				
	}
	
	@Test(expected=UnbindedTypeException.class)
	public void resolving_a_class_with_an_unbinded_dependency_throws_unbinded_type_exception() throws NaiveContainerConfigurationException, InvocationTargetException{
		new SimpleInjector().getInstance(DependentWithUnbindedDependency.class);
	}
	
	public static class DependentThatThrowsOnConstructor{
		public DependentThatThrowsOnConstructor(){
			throw new IllegalArgumentException();
		}
	}
	
	@Test(expected=InvocationTargetException.class)
	public void resolving_a_class_that_throws_on_constructor_throws_invocation_target_exception() throws NaiveContainerConfigurationException, InvocationTargetException{
		new SimpleInjector().getInstance(DependentThatThrowsOnConstructor.class);
	}

}
