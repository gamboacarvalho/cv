package selfdao.test;

import java.sql.SQLException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import selfdao.jdbc.JdbcExecutor;
import selfdao.jdbc.JdbcExecutorSingleConnection;
import selfdao.test.model.Product;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

public class ProductDaoTest {

	private ProductDao dao;
	private JdbcExecutor exec;
	
	@Before
	public void setUp(){
		SQLServerDataSource ds = new SQLServerDataSource();
		ds.setUser("myAppUser");
		ds.setPassword("fcp");
		exec = new JdbcExecutorSingleConnection(ds, false);
		dao = selfdao.Builder.with(exec).of(ProductDao.class);
	}
	
	@After
	public void tearDown() throws Exception{
		exec.close();
	}
	
	
	
	@Test
	public void test_product_load_by_all() throws SQLException{
		Iterable<Product> res = dao.getAll();
		int size = 0;
		for(Product e:res){size++;}
		Assert.assertEquals(77, size);
	}
	
	
	@Test
	public void test_product_load_by_id() throws SQLException{
		Product p = dao.getById(9);
		
		Assert.assertEquals("Mishi Kobe Niku", p.getProductName());
		Assert.assertEquals(97.0, p.getUnitPrice());
		Assert.assertEquals(29, p.getUnitsInStock());
		
		Assert.assertSame(p, dao.getById(9));
	}
	
	@Test
	public void test_update_product() throws SQLException{
		Product p = dao.getById(9);
		Assert.assertEquals("Mishi Kobe Niku", p.getProductName());
		
		p.setProductName("Casa de Cafe Bastos");
		dao.update(p.getProductName(), p.getUnitPrice(), p.getUnitsInStock(), p.getId());
		
		p = dao.getById(9);
		Assert.assertEquals("Casa de Cafe Bastos", p.getProductName());		
	}
	
	@Test
	public void test_insert_product() throws SQLException{
		Product p1 = dao.insert("Cafe da Alcobia", 100.0, 5550);
		
		Product p2  = dao.getById(p1.getId());
		Assert.assertNotSame(p1, p2);
		
		Assert.assertEquals(p1.getProductName(), p2.getProductName());
		Assert.assertEquals(p1.getUnitPrice(), p2.getUnitPrice());
		Assert.assertEquals(p1.getUnitsInStock(), p2.getUnitsInStock());		
	}
	
	@Test
	public void test_delete_product() throws SQLException{
		Product p1 = dao.insert("Cafe da Alcobia", 100.0, 5550);
		
		Product p2  = dao.getById(p1.getId());
		Assert.assertNotSame(p1, p2);
		
		
		dao.delete(p1.getId());
		
		Assert.assertNull(dao.getById(p1.getId()));
	}
}