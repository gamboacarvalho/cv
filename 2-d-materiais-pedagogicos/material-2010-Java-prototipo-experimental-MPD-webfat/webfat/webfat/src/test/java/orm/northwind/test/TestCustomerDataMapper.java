package orm.northwind.test;

import java.sql.SQLException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import orm.core.IDBContext;
import orm.northwind.Customer;
import orm.northwind.mapper.CustomerDataMapper;

public class TestCustomerDataMapper {
	final String connectionUrl = 
		"jdbc:sqlserver://localhost:1433;" +
		"databaseName=Northwind;" + 
		"user=myAppUser;password=fcp";

	private IDBContext db;
	private CustomerDataMapper prodMapper;
	@Before
	public void setUp() throws SQLException{
		db = new TestContext(connectionUrl);
		setUpMappers();
	}
	public void setUpMappers() throws SQLException{
		prodMapper = new CustomerDataMapper(db);
	}
	@After
	public void tearDown() throws SQLException{
		db.close();
	}
	@Test
	public void check_all_orders() throws SQLException{
			Iterable<Customer> prods= prodMapper.load();
			int count = 0;
			for (Customer o : prods) {count++;}
			Assert.assertEquals(91, count);
	}
	@Test
	public void check_order_by_id() throws SQLException{
		Customer o1  = prodMapper.loadById("MAISD");
		Assert.assertEquals(o1.getCompanyName(), "Maison Dewey");
		Customer o2  = prodMapper.loadById("MAISD");
		Assert.assertSame(o1, o2);
	}
	@Test
	public void check_order_where() throws SQLException{
			Iterable<Customer> os = prodMapper.where("ContactName = 'Giovanni Rovelli'");
			Customer p = os.iterator().next();
			Assert.assertEquals("MAGAA", p.getId());
	}
	@Test
	public void check_insert_new_product() throws SQLException{
		//
		// ACT
		//
		Customer p1 = new Customer("Companhia das Indias", "Mahue Catutur Ramses", "Super Fkin Doctor", "Babilonia das couves assadas", "Rabel", "Rabelinos", "13153-5135", "Egiptiocia", "6464654", "35135");
		p1.setId("XXYTU");
		prodMapper.insert(p1);
		//
		// ASSERT and clear identity Map;
		//
		setUpMappers();
		Customer p2 = prodMapper.loadById(p1.getId());
		Assert.assertEquals(p1.getCompanyName(), p2.getCompanyName());
		Assert.assertNotSame(p1, p2);
	}
}
