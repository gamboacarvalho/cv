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
import orm.uow.IRepository;
import orm.uow.RepositoryOrdered;
import orm.uow.UnitOfWork;

public class TestEmployeedUnitOfWork {
	final String connectionUrl = 
		"jdbc:sqlserver://localhost:1433;" +
		"databaseName=Northwind;" + 
		"user=myAppUser;password=fcp";

	private IRepository repo;
	private IDBContext db; 
	@Before
	public void setUp() throws SQLException{
		db = new TestContext(connectionUrl);
		setUpRepo();
	}
	public void setUpRepo() throws SQLException{
		OrdersMapper ordersMapper = new OrdersMapper(db); 
		IDataMapper<Integer, Employee> mapperEmployee = new EmployeeDataMapper(db, ordersMapper);
		ordersMapper.setEmployeeMapper(mapperEmployee);
		UnitOfWork uow = new UnitOfWork();
		repo = new RepositoryOrdered(uow);
		uow.setRepository(repo);
		repo.add(Employee.class, mapperEmployee);
		repo.add(Order.class, ordersMapper);
	}
	@After
	public void tearDown()throws SQLException{
		if(db != null) db.close();
	}
	@Test
	public void check_employee_by_id_and_update() throws SQLException{
			Employee emp1 = repo.loadById(Employee.class, 8);
			Assert.assertEquals(emp1.getFirstName(), "Laura");
			emp1.set_City("Porto");
			repo.save();
			//
			// Create a new Repository with new DataMappers and identity maps
			// 
			setUpRepo();
			Employee emp2 = repo.where(Employee.class, "City = 'Porto'").iterator().next();
			Assert.assertNotNull(emp2);
			Assert.assertEquals(emp2.getCity(), "Porto");
			Assert.assertEquals(emp2.getId().intValue(), 8);
	}
	@Test
	public void add_new_employee_and_order() throws SQLException, ParseException{
		Employee emp1 = new Employee("Jacinto", "Oliveira", "Prof", "Porto", dateFormat.parse("5-9-1986"));
		Order o1 = new Order("Oleluia", "Bacalhaus secos da Noruega", "Bacalhoeira", emp1);
		repo.save();
		//
		// Create a new Repository with new DataMappers and identity maps
		// 
		setUpRepo();
		Employee emp2 = repo.loadById(Employee.class, emp1.getId());
		Order o2 =  emp2.getOrders().iterator().next();
		// 
		// Assert
		// 
		Assert.assertNotSame(emp1, emp2);
		Assert.assertNotSame(o1, o2);
		Assert.assertNotSame(o1.getId(), o2.getId());
	}
	@Test
	public void check_employee_with_new_orders() throws SQLException{
		//
		// Arrange
		//
		Employee emp1 = repo.loadById(Employee.class, 9);
		Iterable<Order> os = emp1.getOrders();
		int count = 0;
		for (Order o : os) {count++;}
		Assert.assertEquals(43, count);
		//
		// Act
		//
		Order o1 = new Order("Oleluia", "Bacalhaus secos da Noruega", "Bacalhoeira", emp1);
		repo.save();
		//
		// Assert
		//
		os = emp1.getOrders();
		count = 0;
		for (Order o : os) {count++;}
		Assert.assertEquals(44, count);
	}
	@Test
	public void check_employee_deete() throws SQLException{
		//
		// Act
		//
		Employee emp1 = repo.loadById(Employee.class, 9);
		emp1.delete();
		repo.save();
		//
		// Assert
		//
		emp1 = repo.loadById(Employee.class, 9);
		Assert.assertNull(emp1);
		for (Order o : repo.load(Order.class)) {
	    Assert.assertFalse(o.getEmployee() != null && o.getEmployee().getId() == 9);
    }
	}
	@Test
	public void check_clear_orders_employee() throws SQLException{
		//
		// Act
		//
		for (Order o  : repo.load(Order.class)) {
	    o.setEmployee(null);
    }
		repo.save();
		//
		// Assert
		//
		for (Order o  : repo.load(Order.class)) {
	    Assert.assertNull(o.getEmployee());
    }
	}
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
}
