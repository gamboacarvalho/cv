package orm.inspect.test;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import junit.framework.Assert;

import northwind.model.Category;
import northwind.model.Employee;

import org.junit.Test;

import orm.InjectorHelper;
import orm.mapper.DataMapper;
import orm.mapper.MapperFactory;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class TestOrmInspectForEmployee {
	Injector inj = InjectorHelper.getInjector();
	@Test
	public void test_load_all_for_employees() throws SQLException{
		DataMapper<Integer, Employee> empMapper = inj.getInstance(MapperFactory.class).make(Employee.class);
		int size = 0;
		for (Employee  emp : empMapper.loadAll()) {
			size++;
		}
		Assert.assertEquals(9, size);
	}
	
	private static DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	
	@Test
	public void test_load_by_id_for_employees() throws SQLException, ParseException{
		DataMapper<Integer, Employee> empMapper = inj.getInstance(MapperFactory.class).make(Employee.class);
		Employee e = empMapper.loadById(5);
		Assert.assertEquals("Sales Manager", e.getTitle());
		Assert.assertEquals("Steven", e.getFirstName());
		Assert.assertEquals("Buchanan", e.getLastName());
		Assert.assertEquals(formatter.parse("04-03-1955"), e.getBirthDate());
		Assert.assertEquals(2, e.getReportsTo().getId().intValue());
		int[] empsId = {6, 7, 9};
		int i = 0;
		for (Employee emp : e.getEmployees()) {
		  Assert.assertEquals(empsId[i++], emp.getId().intValue());
		}
		Assert.assertEquals(3, i);
	}
	
	@Test
	public void test_WHERE_employees() throws SQLException, ParseException{
		DataMapper<Integer, Employee> empMapper = inj.getInstance(MapperFactory.class).make(Employee.class);
		int size = 0;
		
		for (Employee  emp : empMapper.loadAll().where("Title = 'Sales Representative'")) {
			size++;
		}
		Assert.assertEquals(6, size);

	}
}
