package orm.northwind.test;

import java.sql.SQLException;

import junit.framework.Assert;

import org.junit.Test;

import orm.core.IDBContext;
import orm.mapper.IDataMapper;
import orm.northwind.Employee;
import orm.northwind.Order;
import orm.northwind.mapper.EmployeeDataMapper;
import orm.northwind.mapper.OrdersMapper;

public class TestEmployeedDataMapper {
	final String connectionUrl = 
		"jdbc:sqlserver://localhost:1433;" +
		"databaseName=Northwind;" + 
		"user=myAppUser;password=fcp";

	@Test
	public void check_all_employees() throws SQLException{
		IDBContext db = null;
		try{
			db = new TestContext(connectionUrl);
			IDataMapper<Integer, Employee> empsMapper = new EmployeeDataMapper(db, new OrdersMapper(db)); 
			Iterable<Employee> emps = empsMapper.load();
			int count = 0;
			for (Employee employee : emps) {count++;}
			Assert.assertEquals(9, count);
		}finally{
			if(db != null) db.close();
		}		

	}
	@Test
	public void check_employee_by_id() throws SQLException{
		IDBContext db = null;
		try{
			db = new TestContext(connectionUrl);
			IDataMapper<Integer, Employee> empsMapper = new EmployeeDataMapper(db, new OrdersMapper(db)); 
			Employee emp1 = empsMapper.loadById(8);
			Assert.assertEquals(emp1.getFirstName(), "Laura");
			Employee emp2 = empsMapper.loadById(8);
			// Assert.assertTrue(emp1 == emp2);
			Assert.assertSame(emp1, emp2);

		}finally{
			if(db != null) db.close();
		}		

	}
	@Test
	public void check_employee_where() throws SQLException{
		IDBContext db = null;
		try{
			db = new TestContext(connectionUrl);
			IDataMapper<Integer, Employee> empsMapper = new EmployeeDataMapper(db, new OrdersMapper(db)); 
			Iterable<Employee> es = empsMapper.where("City = 'London'");
			int count = 0;
			for (Employee employee : es) {
	      count++;
      }
			Assert.assertSame(4, count);

		}finally{
			if(db != null) db.close();
		}		
	}
	@Test
	public void check_employee_reportsto()throws SQLException{
		IDBContext db = null;
		try{
			db = new TestContext(connectionUrl);
			IDataMapper<Integer, Employee> empsMapper = new EmployeeDataMapper(db, new OrdersMapper(db)); 
			Employee e = empsMapper.loadById(3);
			Assert.assertEquals("Fuller", e.reportsTo().getLastName());
		}finally{
			if(db != null) db.close();
		}		
	}
	@Test
	public void check_employee_orders()throws SQLException{
		IDBContext db = null;
		try{
			db = new TestContext(connectionUrl);
			OrdersMapper ordersMapper = new OrdersMapper(db);
			IDataMapper<Integer, Employee> empsMapper = new EmployeeDataMapper(db, ordersMapper);
			ordersMapper.setEmployeeMapper(empsMapper);
			Employee e = empsMapper.loadById(3);
			Assert.assertEquals("Fuller", e.reportsTo().getLastName());
			int count = 0;
			for (Order o : e.getOrders()) {
	      count++;
      }
			Assert.assertEquals(127, count);
		}finally{
			if(db != null) db.close();
		}		
	}
}
