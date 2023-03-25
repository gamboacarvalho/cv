package orm.northwind.test;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import orm.core.IDBContext;
import orm.mapper.IDataMapper;
import orm.northwind.Employee;
import orm.northwind.Order;
import orm.northwind.mapper.EmployeeDataMapper;
import orm.northwind.mapper.OrdersMapper;

public class TestOrdersDataMapper {
	final String connectionUrl = 
		"jdbc:sqlserver://localhost:1433;" +
		"databaseName=Northwind;" + 
		"user=myAppUser;password=fcp";

	private IDBContext db;
	private OrdersMapper ordersMapper;
	private IDataMapper<Integer, Employee> mapperEmployee;
	@Before
	public void setUp() throws SQLException{
		db = new TestContext(connectionUrl);
		setUpMappers();
	}
	public void setUpMappers() throws SQLException{
		ordersMapper = new OrdersMapper(db);
		mapperEmployee = new EmployeeDataMapper(db, ordersMapper);
		ordersMapper.setEmployeeMapper(mapperEmployee);
	}
	@After
	public void tearDown() throws SQLException{
		db.close();
	}
	@Test
	public void check_all_orders() throws SQLException{
			Iterable<Order> orders = ordersMapper.load();
			int count = 0;
			for (Order o : orders) {count++;}
			Assert.assertEquals(830, count);
	}
	@Test
	public void check_order_by_id() throws SQLException{
			Order o1  = ordersMapper.loadById(10251);
			Assert.assertEquals(o1.getShipCity(), "Lyon");
			Order o2  = ordersMapper.loadById(10251);
			Assert.assertSame(o1, o2);
	}
	@Test
	public void check_order_where() throws SQLException{
			Iterable<Order> os = ordersMapper.where("ShipCity= 'Caracas'");
			int count = 0;
			for (Order o: os) {
	      count++;
      }
			Assert.assertSame(2, count);
	}
	@Test
	public void check_new_order_with_employee() throws SQLException{
		//
		// Act
		//
		Employee emp = mapperEmployee.loadById(7);
		Order o1 = new Order("Oleluia", "Bacalhaus secos da Noruega", "Bacalhoeira", emp);
		ordersMapper.insert(o1);
		//
		// Assert
		//
		setUpMappers(); // clean identity maps
		Order o2 =ordersMapper.loadById( o1.getId());
		Assert.assertNotNull(o2);
		Assert.assertNotSame(o1, o2);
		Assert.assertEquals(o1.getShipName(), o2.getShipName());
		Assert.assertEquals(o1.getShipAddress(), o2.getShipAddress());
		Assert.assertEquals(o1.getShipCity(), o2.getShipCity());
	}
	@Test
	public void check_new_order_without_employee() throws SQLException{
		//
		// Act
		//
		Order o1 = new Order("Oleluia", "Bacalhaus secos da Noruega", "Bacalhoeira", null);
		ordersMapper.insert(o1);
		//
		// Assert
		//
		setUpMappers(); // clean identity maps
		Order o2 =ordersMapper.loadById( o1.getId());
		Assert.assertNotNull(o2);
		Assert.assertNotSame(o1, o2);
		Assert.assertEquals(o1.getShipName(), o2.getShipName());
		Assert.assertEquals(o1.getShipAddress(), o2.getShipAddress());
		Assert.assertEquals(o1.getShipCity(), o2.getShipCity());
	}
	@Test(expected= NullPointerException.class)
	public void check_new_order_with_uncommitted_new_employee() throws SQLException, ParseException{
		//
		// Act
		//
		Employee emp = new Employee("Jacinto", "Oliveira", "Prof", "Porto", dateFormat.parse("11-06-1987"));
		Order o1 = new Order("Oleluia", "Bacalhaus secos da Noruega", "Bacalhoeira", emp);
		// The employee was not inserted =>So EmployId is null
		ordersMapper.insert(o1);
	}	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
}
