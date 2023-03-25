package orm.inspect.test;

import java.sql.SQLException;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import junit.framework.Assert;

import northwind.model.Supplier;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import orm.InjectorHelper;
import orm.JdbcConverter;
import orm.mapper.DataMapper;
import orm.mapper.MapperFactory;

public class TestOrmInspectForSupplier {
	Injector inj = InjectorHelper.getInjector();

	@Test
	public void test_load_all_for_suppliers() throws SQLException{
		DataMapper<Integer, Supplier> empMapper = inj.getInstance(MapperFactory.class).make(Supplier.class);
		int size = 0;
		for (Supplier emp : empMapper.loadAll()) {
			size++;
		}
		Assert.assertEquals(29, size);
	}
	
	private static DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	
	@Test
	public void test_load_by_id_for_suppliers() throws SQLException, ParseException{
		DataMapper<Integer, Supplier> empMapper = inj.getInstance(MapperFactory.class).make(Supplier.class);
		Supplier e = empMapper.loadById(5);
		Assert.assertEquals("Cooperativa de Quesos 'Las Cabras'", e.getCompanyName());
		Assert.assertEquals("Calle del Rosal 4", e.getAddress());
		Assert.assertEquals("Oviedo", e.getCity());
	}	
}
