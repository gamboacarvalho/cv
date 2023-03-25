package orm.northwind.test;

import java.sql.SQLException;

import northwind.model.OrderDetails;
import northwind.model.Product;
import orm.mapper.MapperFactory;
import org.junit.Test;

import junit.framework.Assert;


import orm.InjectorHelper;
import orm.TemplateDataMapper;


import com.google.inject.Injector;

public class TestProducts {
	TemplateDataMapper<Integer, Product> prodMapper;
	Injector inj; 
	public TestProducts(){
		inj = InjectorHelper.getInjector();
		prodMapper = (TemplateDataMapper<Integer, Product>) inj.getInstance(MapperFactory.class).make(Product.class);
	}

	@Test
	public void test_load_all_products() throws SQLException{
		Iterable<Product> res = prodMapper.loadAll();
		int size = 0;
		for(@SuppressWarnings("unused") Product e:res){size++;}
		Assert.assertEquals(77, size);
	}
	@Test
	public void test_load_byid_products() throws SQLException{
		Product e = prodMapper.loadById(9);
		Assert.assertEquals("Mishi Kobe Niku", e.getProductName());
		Assert.assertEquals(97.00, e.getUnitPrice());
		Assert.assertEquals(29, e.getUnitsInStock());
		Iterable<OrderDetails> orders = e.getOrders();
		int [] ordersIds = {10420, 10515, 10687, 10693, 10848};
		int i = 0;
		for(OrderDetails o : orders){
			Assert.assertEquals(ordersIds[i++], o.getId().OrderId);
		}
	}
	@Test
	public void s2_ex1_teste_where_unitPrice() throws SQLException{
		Iterable<Product> res = prodMapper.loadAll().where("UnitPrice >= 50");
		int count = 0;
		for (@SuppressWarnings("unused") Product p : res) {count++;}
		Assert.assertEquals(count, 7);
	}
	@Test
	public void s2_ex2_teste_ValueHolder_getSuplier() throws SQLException{
		Product res = prodMapper.loadById(9);
		Assert.assertEquals(9, res.getId()+0);
		Assert.assertEquals(4, res.getSupplier().getId()+0);
	}
	
	@Test
	public void s2_ex2_teste_order_by() throws SQLException{
		Iterable<Product> res = prodMapper.loadAll().where("UnitPrice >= 50").orderBy("UnitsInStock");
		int [] expected = {29, 38, 51, 9, 20, 18, 59};
		int i = 0;
		for (Product p : res) {
			Assert.assertEquals(expected[i++], p.getId().intValue());
		}
	}

	@Test
	public void s2_ex3_lazy_loadAll() throws SQLException{
		//
		// loadAll
		//
		Iterable<Product> res = prodMapper.loadAll();
		int i = 1;
		for (Product p : res) {
		Assert.assertEquals(i++, p.getId().intValue());
		}
		//
		// loadAll + where
		//
		res = prodMapper.loadAll().where("UnitPrice >= 50");
		int[]expected = {9, 18, 20, 29, 38, 51, 59};
		i = 0;
		for (Product p : res) {
		Assert.assertEquals(expected[i++], p.getId().intValue());
		}
		//
		// loadAll + orderBy
		//
		res = prodMapper.loadAll().orderBy("ProductName");
		expected = new int[]{17, 3, 40, 60, 18, 1, 2, 39, 4};
		i = 0;
		for (Product p : res) {
		Assert.assertEquals(expected[i++], p.getId().intValue());
		if(i >= expected.length) break;
		}
		//
		// loadAll + where + orderBy
		//
		res = prodMapper.loadAll().where("UnitPrice >= 50").orderBy("ProductName");
		expected = new int[]{18, 38, 51, 9, 59, 20, 29};
		i = 0;
		for (Product p : res) {
		Assert.assertEquals(expected[i++], p.getId().intValue());
		}
	}
}
