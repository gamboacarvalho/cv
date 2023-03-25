package selfdao.test;

import java.sql.ResultSet;
import java.sql.SQLException;

import junit.framework.Assert;

import org.junit.Test;

import selfdao.jdbc.JdbcCmd;
import selfdao.jdbc.JdbcCmdTemplate;
import selfdao.jdbc.JdbcConverter;
import selfdao.jdbc.JdbcExecutorSingleConnection;
import selfdao.test.model.Product;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

public class ProductQueryTest {

	JdbcExecutorSingleConnection exec; 
	JdbcCmd<Product> cmdProductLoadAll;
	
	public ProductQueryTest() {
		SQLServerDataSource ds = new SQLServerDataSource();
		ds.setUser("myAppUser");
		ds.setPassword("fcp");
		exec = new JdbcExecutorSingleConnection(ds, false);
		cmdProductLoadAll = new JdbcCmdTemplate<Product>(
				"SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM Products", 
				new JdbcConverter<Product>() {
					public Product convert(ResultSet rs) throws SQLException {
						return new Product(
								rs.getInt(1), 
								rs.getString(2),
								rs.getDouble(3), 
								rs.getInt(4));
					}
				});
	}

	@Test
	public void test_load_all() throws SQLException{
		Iterable<Product> res = exec.executeQuery(cmdProductLoadAll);
		int i = 1;
		for (Product p : res) {
			Assert.assertEquals(i++, p.getId());
		}
	}
	
	@Test
	public void test_load_all_and_order_by() throws SQLException{
		Iterable<Product> res = exec.executeQuery(cmdProductLoadAll).orderBy("ProductName");
		int[] expected = new int[]{17, 3, 40, 60, 18, 1, 2, 39, 4};
		int i = 0;
		for (Product p : res) {
			Assert.assertEquals(expected[i++], p.getId());
			if(i >= expected.length) break;
		}
	}
	
	@Test
	public void test_load_all_and_where() throws SQLException{
		Iterable<Product> res = exec.executeQuery(cmdProductLoadAll).where("UnitPrice >= 50");
		int[]expected = {9,	18,	20,	29,	38,	51,	59};
		int i = 0;
		for (Product p : res) {
			Assert.assertEquals(expected[i++], p.getId());
		}

	}
	
	@Test
	public void test_load_all_and_where_and_orderby() throws SQLException{
		Iterable<Product> res = exec.executeQuery(cmdProductLoadAll).where("UnitPrice >= 50").orderBy("ProductName");
		int[] expected = new int[]{7, 18, 38, 51, 9, 59, 20, 29};
		int i = 0;
		for (Product p : res) {
			Assert.assertEquals(expected[i++], p.getId());
		}


	}
}
