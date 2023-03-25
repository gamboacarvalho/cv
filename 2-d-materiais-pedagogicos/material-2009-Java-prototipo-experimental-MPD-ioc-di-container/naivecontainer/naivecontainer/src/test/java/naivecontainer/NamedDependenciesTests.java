package naivecontainer;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;

import naivecontainer.exceptions.NaiveContainerException;

import org.junit.Test;

public class NamedDependenciesTests {

	
	static public class Dependent{
		
		private final String _s1;
		private final String _s2;
		public String getS1(){return _s1;}
		public String getS2(){return _s2;}
		
		public Dependent(@Named("s1") String s1, @Named("s2") String s2){
			_s1 = s1;
			_s2 = s2;
		}
	}
	
	@Test
	public void test() throws InvocationTargetException, NaiveContainerException{
		
		Injector injector = new SimpleInjector(SimpleConfiguration.with()
				.bind(String.class).withName("s1").to("string1")
				.bind(String.class).withName("s2").to("string2"));
		
		Dependent dep = injector.getInstance(Dependent.class);
		assertEquals("string1", dep.getS1());
		assertEquals("string2", dep.getS2());		
	}
}
