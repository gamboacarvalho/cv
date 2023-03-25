package naivecontainer;

import static org.junit.Assert.*;

import org.junit.Test;

public class NaiveContainerTests {
	
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
		Injector injector = new SimpleInjector()
			.withBinding(new TypeBinding<Service1>(Service1.class, Service1Impl1.class))
			.withBinding(new TypeBinding<Service2>(Service2.class, Service2Impl1.class));
		
		Dependant dep = injector.getInstance(Dependant.class);
		assertEquals(dep.getService1().getClass(), Service1Impl1.class);
		assertEquals(dep.getService2().getClass(), Service2Impl1.class);
	}
	
	@Test
	public void can_inject_singletons() throws Exception {
		Service2 srv2 = new Service2Impl1();
		Injector injector = new SimpleInjector()
			.withBinding(new TypeBinding<Service1>(Service1.class, Service1Impl1.class))
			.withBinding(new InstanceBinding<Service2>(Service2.class, srv2));
		
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
		Injector injector = new SimpleInjector()
			.withBinding(new InstanceBinding<Integer>(int.class, 13));
		DependantWithValueDependency dep = injector.getInstance(DependantWithValueDependency.class);
		assertEquals(dep.getInt1(), 13);
		assertEquals(dep.getInt2(), 13);
	}
	
	@Test
	public void can_inject_self() throws Exception {		
		Injector injector = new SimpleInjector();
		Injector dep = injector.getInstance(Injector.class);
		assertNotNull(dep);
	}
}
