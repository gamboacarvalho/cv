package orm.northwind.test;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import northwind.model.Order;
import northwind.model.OrderDetails;
import orm.mapper.MapperFactory;
import org.junit.Test;

import junit.framework.Assert;


import orm.InjectorHelper;
import orm.TemplateDataMapper;


import com.google.inject.Injector;

public class TestOrders {
	TemplateDataMapper<Integer, Order> orderMapper;
	Injector inj; 
	public TestOrders(){
		inj = InjectorHelper.getInjector();
		orderMapper = (TemplateDataMapper<Integer, Order>) inj.getInstance(MapperFactory.class).make(Order.class);
	}

	@Test
	public void test_load_all_orders() throws SQLException{
		Iterable<Order> res = orderMapper.loadAll();
		int size = 0;
		for(@SuppressWarnings("unused") Order e:res){size++;}
		Assert.assertEquals(831, size);
	}
	
	@Test
	public void s2_ex2_test_cach_datamaper() throws SQLException{
		TemplateDataMapper<Integer, Order> orderMapper2 = (TemplateDataMapper<Integer, Order>) inj.getInstance(MapperFactory.class).make(Order.class);
		
		Assert.assertEquals(orderMapper2, orderMapper);
		
	}
	
	@Test
	public void s2_ex2_test_cache_entity() throws SQLException{
		Order o = orderMapper.loadById(10248);
		Order o2 = orderMapper.loadById(10248);
		
		Assert.assertEquals(System.identityHashCode(o), System.identityHashCode(o2));
		
	}
	
	@Test
	public void test_load_byid_order() throws SQLException, ParseException{
		Order e = orderMapper.loadById(10248);
		Assert.assertEquals(10248, e.getId()+0);
		Assert.assertEquals("59 rue de l'Abbaye", e.getShipAddress());
		Assert.assertEquals("Vins et alcools Chevalier", e.getShipName());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Assert.assertEquals(sdf.parse("1996-07-04 00:00:00").getTime(), e.getOrderDate().getTime());
		Iterable<OrderDetails> ordersDetails = e.getOrderDetails();
		
		int [] orderDetailsId = {11, 42, 72};
		int i = 0;
		for(OrderDetails o : ordersDetails){
			Assert.assertEquals(orderDetailsId[i++], o.getId().ProductId);
		}
	}
}
