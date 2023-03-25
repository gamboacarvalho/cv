package orm.northwind.test;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import northwind.model.Employee;
import northwind.model.Order;
import orm.mapper.MapperFactory;
import org.junit.Test;

import junit.framework.Assert;


import orm.InjectorHelper;
import orm.TemplateDataMapper;


import com.google.inject.Injector;

public class TestEmploees {
	TemplateDataMapper<Integer, Employee> employeesMapper;
	Injector inj; 
	public TestEmploees(){
		inj = InjectorHelper.getInjector();
		employeesMapper = (TemplateDataMapper<Integer, Employee>) inj.getInstance(MapperFactory.class).make(Employee.class);
	}

	@Test
	public void test_load_all_Employees() throws SQLException{
		Iterable<Employee> res = employeesMapper.loadAll();
		int size = 0;
		for(@SuppressWarnings("unused") Employee e:res){size++;}
		Assert.assertEquals(9, size);
	}
	
	@Test
	public void test_load_byid_Employee() throws SQLException, ParseException{
		Employee e = employeesMapper.loadById(3);
		Assert.assertEquals(3, e.getId()+0);
		Assert.assertEquals("Janet", e.getFirstName());
		Assert.assertEquals("Leverling", e.getLastName());
		Assert.assertEquals("Sales Representative", e.getTitle());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Assert.assertEquals(sdf.parse("1963-08-30 00:00:00").getTime(), e.getBirthDate().getTime());
		Assert.assertEquals(2, e.getReportsTo().getId()+0);
		
		Iterable<Order> orders = e.getOrders();
		
		int [] ordersId = {10251, 10253, 10256, 10266, 10273, 10283, 10309, 10321};
		int i = 0;
		for(Order o : orders){
			if(i<ordersId.length)
				Assert.assertEquals(ordersId[i++], o.getId()+0);
			else
				i++;
		}
		Assert.assertEquals(127, i);
	}
}
