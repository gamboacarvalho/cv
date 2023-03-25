package orm.northwind.test;

import static org.junit.Assert.*;
import northwind.model.Supplier;

import org.junit.Test;

import orm.mapper.MapperFactory;
import orm.InjectorHelper;
import orm.TemplateDataMapper;
import com.google.inject.Injector;

public class TestSupplier {
	MapperFactory map;
	Injector inj; 
	public TestSupplier(){
		inj = InjectorHelper.getInjector();
		map= inj.getInstance(MapperFactory.class);
	}
	
	@Test
	public void test_insert_Suplier() throws Exception{
		try(TemplateDataMapper<Integer, Supplier> supplierMp = (TemplateDataMapper<Integer, Supplier>) map.make(Supplier.class)){
			Supplier res = supplierMp.loadById(10);
			assertNotNull(res);
			assertEquals(10, res.getId()+0);
			res.setCompanyName("company insert 1");
			res.setContactTitle("Title insert");
			
			supplierMp.insert(res);
			assertTrue(res.getId()+0 != 10);
			
			res = supplierMp.loadById(res.getId());
			
			assertEquals("company insert 1", res.getCompanyName());
			assertEquals("Title insert", res.getContactTitle());
		}
		
	}
	
}
