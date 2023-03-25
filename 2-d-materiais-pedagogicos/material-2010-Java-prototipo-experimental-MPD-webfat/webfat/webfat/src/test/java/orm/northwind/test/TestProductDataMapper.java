package orm.northwind.test;

import java.sql.SQLException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import orm.core.IDBContext;
import orm.mapper.IDataMapper;
import orm.northwind.Employee;
import orm.northwind.Order;
import orm.northwind.Product;
import orm.northwind.mapper.EmployeeDataMapper;
import orm.northwind.mapper.OrdersMapper;
import orm.northwind.mapper.ProductDataMapper;

public class TestProductDataMapper {
	final String connectionUrl = 
		"jdbc:sqlserver://localhost:1433;" +
		"databaseName=Northwind;" + 
		"user=myAppUser;password=fcp";

	private IDBContext db;
	private ProductDataMapper prodMapper;
	@Before
	public void setUp() throws SQLException{
		db = new TestContext(connectionUrl);
		setUpMappers();
	}
	public void setUpMappers() throws SQLException{
		prodMapper = new ProductDataMapper(db);
	}
	@After
	public void tearDown() throws SQLException{
		db.close();
	}
	@Test
	public void check_all_orders() throws SQLException{
			Iterable<Product> prods= prodMapper.load();
			int count = 0;
			for (Product o : prods) {count++;}
			Assert.assertEquals(77, count);
	}
	@Test
	public void check_order_by_id() throws SQLException{
			Product o1  = prodMapper.loadById(8);
			Assert.assertEquals(o1.get_productName(), "Northwoods Cranberry Sauce");
			Product o2  = prodMapper.loadById(8);
			Assert.assertSame(o1, o2);
	}
	@Test
	public void check_order_where() throws SQLException{
			Iterable<Product> os = prodMapper.where("ProductName = 'Queso Manchego La Pastora'");
			int count = 0;
			Product p = os.iterator().next();
			Assert.assertEquals(12, (int) p.getId());
	}
	@Test
	public void check_insert_new_product() throws SQLException{
		//
		// ACT
		//
		Product p1 = new Product("Bacalhau da Noruega", "5 lombos por caixa", "Meat/Poultry", 10.56, (short) 560, (short)5, (short)5, false);
		prodMapper.insert(p1);
		//
		// ASSERT and clear identity Map;
		//
		setUpMappers();
		Product p2 = prodMapper.loadById(p1.getId());
		Assert.assertEquals(p1.get_productName(), p2.get_productName());
		Assert.assertNotSame(p1, p2);
	}
}
