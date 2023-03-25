package orm.inspect.test;

import java.sql.SQLException;



import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.Assert;

import northwind.model.OrderDetails;
import northwind.model.Product;
import northwind.model.Supplier;

import org.junit.Test;

import orm.InjectorHelper;
import orm.mapper.DataMapper;
import orm.mapper.MapperFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.util.Modules;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

public class TestOrmInspectForProduct {

	Injector inj = InjectorHelper.getInjector();

	@Test
	public void testLoadAllProducts() throws Exception{
		DataMapper<Integer, Product> empMapper = inj.getInstance(MapperFactory.class).make(Product.class);
		Iterable<Product> res = empMapper.loadAll();
		int count = 0;
		for (Product p : res) {
			count++;
		}
		Assert.assertEquals(count, 77);
	}
	@Test
	public void testLoadByIdProducts()throws Exception{
		inj.getInstance(MapperFactory.class).make(Supplier.class);
		inj.getInstance(MapperFactory.class).make(OrderDetails.class);
		DataMapper<Integer, Product> empMapper = inj.getInstance(MapperFactory.class).make(Product.class);
		Product e = empMapper.loadById(9);
		Assert.assertEquals("Mishi Kobe Niku", e.getProductName());
		Assert.assertEquals(97.0, e.getUnitPrice());
		Assert.assertEquals(29, e.getUnitsInStock());
		Assert.assertEquals(4, e.getSupplier().getId().intValue());
		int [] ordersIds = {10420, 10515, 10687, 10693, 10848};
		int idx = 0;
		for (OrderDetails o : e.getOrders()) {
			Assert.assertEquals(ordersIds[idx++], o.getId().OrderId);
		}
	}
	@Test
	public void test_where() throws Exception{
		try(DataMapper<Integer, Product> mapper = inj.getInstance(MapperFactory.class).make(Product.class)){
			//
			// loadAll
			//
			Iterable<Product> res = mapper.loadAll();
			int i = 1;
			for (Product p : res) {
				Assert.assertEquals(i++, p.getId().intValue());
			}
		}
	}
	@Test
	public void testUpdateProduct()throws Exception{
		try(DataMapper<Integer, Product> empMapper = inj.getInstance(MapperFactory.class).make(Product.class)){
			Product e = empMapper.loadById(7);
			Assert.assertEquals("Uncle Bob's Organic Dried Pears", e.getProductName());
			Assert.assertEquals(30.0, e.getUnitPrice());
			Assert.assertEquals(15, e.getUnitsInStock());
			//
			// Arrange
			//
			e.setProductName("Cachecol do Campeao Nacional -FCP");
			e.setUnitPrice(1000);
			e.setUnitsInStock(2);
			//
			// Act
			//		
			empMapper.update(e);
			//
			// Assert
			//
			e = empMapper.loadById(7);
			Assert.assertEquals("Cachecol do Campeao Nacional -FCP", e.getProductName());
			Assert.assertEquals(1000.0, e.getUnitPrice());
			Assert.assertEquals(2, e.getUnitsInStock());
		}
	}

	@Test
	public void testInsertProduct()throws Exception{
		try(DataMapper<Integer, Product> empMapper = inj.getInstance(MapperFactory.class).make(Product.class)){

			//
			// Arrange
			//
			Product p = new Product(0, "cachecol do FCP", 1000.0, 7000, null, null,null);
			//
			// Act
			//
			empMapper.insert(p);
			//
			// Assert
			//
			Product newP = empMapper.loadById(p.getId());
			Assert.assertEquals(p.getProductName(), newP.getProductName());
			Assert.assertEquals(p.getUnitPrice(), newP.getUnitPrice());
			Assert.assertEquals(p.getUnitsInStock(), newP.getUnitsInStock());
			//
			// Delete
			// 
			empMapper.delete(p);
			//
			// Assert
			//
			newP = empMapper.loadById(p.getId());
			// Assert.assertEquals(null, newP);
			Assert.assertNull(newP);
		}
	}
	
}
