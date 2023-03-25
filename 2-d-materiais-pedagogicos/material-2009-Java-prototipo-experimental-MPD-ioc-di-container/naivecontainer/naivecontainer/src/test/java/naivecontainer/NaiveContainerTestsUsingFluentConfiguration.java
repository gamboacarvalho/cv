package naivecontainer;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;

import naivecontainer.exceptions.NaiveContainerException;

import org.junit.Test;

public class NaiveContainerTestsUsingFluentConfiguration {
	public interface Service1{}
	public interface Service2{}
	
	public static class Service1Impl1 implements Service1{}
	public static class Service2Impl1 implements Service2{}
	
	static class Dependant{		
		private final Service1 _srv1;		
		private final Service2 _srv2;
		
		public Dependant(Service1 srv1, Service2 srv2){
			_srv1 = srv1;
			_srv2 = srv2;
		}
		public Service1 getService1(){ return _srv1;}
		public Service2 getService2(){ return _srv2;}
	}	
	
	@Test
	public void can_inject() throws Exception {
		Injector injector = new SimpleInjector(SimpleConfiguration.with()
			.bind(Service1.class).to(Service1Impl1.class)
			.bind(Service2.class).to(Service2Impl1.class));
		
		Dependant dep = injector.getInstance(Dependant.class);
		assertEquals(dep.getService1().getClass(), Service1Impl1.class);
		assertEquals(dep.getService2().getClass(), Service2Impl1.class);
	}
	
	@Test
	public void can_inject_singletons() throws Exception {
		Service2 srv2 = new Service2Impl1();
		Injector injector = new SimpleInjector(SimpleConfiguration.with()
				.bind(Service1.class).to(Service1Impl1.class)
				.bind(Service2.class).to(srv2));
		
		Dependant dep = injector.getInstance(Dependant.class);
		assertEquals(dep.getService1().getClass(), Service1Impl1.class);
		assertEquals(dep.getService2(), srv2);
	}
	
	static class DependantWithValueDependency{		
		private final int _i;
		private final int _j;
				
		public DependantWithValueDependency(int i, int j){
			_i = i;		
			_j = j;
		}
		public int getInt1(){ return _i;}		
		public int getInt2(){ return _j;}
	}
	
	@Test
	public void can_inject_value_types() throws Exception {		
		Injector injector = new SimpleInjector(SimpleConfiguration.with()
			.bind(int.class).to(13));
		DependantWithValueDependency dep = injector.getInstance(DependantWithValueDependency.class);
		assertEquals(dep.getInt1(), 13);
		assertEquals(dep.getInt2(), 13);
	}
	
	@Test
	public void can_inject_strings() throws Exception {
		Injector injector = new SimpleInjector(SimpleConfiguration.with()
				.bind(String.class).to("hello"));
		
		assertEquals("hello",injector.getInstance(String.class));
		assertEquals("hello",injector.getInstance(String.class));		
	}
	
	public static class ClassWithSingletonInstance{}
	
	@Test
	public void singleton_binding_always_produces_the_same_instance() throws InvocationTargetException, NaiveContainerException{
		Injector injector = new SimpleInjector(SimpleConfiguration.with()
				.bind(ClassWithSingletonInstance.class).toSingleton(ClassWithSingletonInstance.class));
		
		assertEquals(
				injector.getInstance(ClassWithSingletonInstance.class),
				injector.getInstance(ClassWithSingletonInstance.class)
				);
	}
}
