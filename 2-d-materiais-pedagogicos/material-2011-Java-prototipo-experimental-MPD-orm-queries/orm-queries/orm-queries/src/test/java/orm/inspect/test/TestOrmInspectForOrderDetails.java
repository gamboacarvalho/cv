package orm.inspect.test;

import java.sql.SQLException;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.Assert;

import northwind.model.Employee;
import northwind.model.OrderDetails;
import northwind.model.OrderDetails.Key;

import org.junit.Test;

import orm.InjectorHelper;
import orm.mapper.DataMapper;
import orm.mapper.MapperFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.util.Modules;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

public class TestOrmInspectForOrderDetails {
	Injector inj = InjectorHelper.getInjector();
	@Test
	public void testLoadAllOrderDetails() throws Exception{
		DataMapper<OrderDetails.Key, OrderDetails> empMapper = inj.getInstance(MapperFactory.class).make(OrderDetails.class);
		Iterable<OrderDetails> res = empMapper.loadAll();
		int count = 0;
		for (OrderDetails p : res) {
			count++;
		}
		Assert.assertEquals(count, 2155);
	}
	@Test
	public void testLoadByIdOrderDetails()throws Exception{
		DataMapper<OrderDetails.Key, OrderDetails> empMapper = inj.getInstance(MapperFactory.class).make(OrderDetails.class);
		OrderDetails e = empMapper.loadById(new Key(10250, 65));
		Assert.assertEquals(16.80, e.getUnitPrice());
		Assert.assertEquals(15, e.getQuantity());
	}
	@Test
	public void test_update_OrderDetails()throws Exception{
		try(DataMapper<OrderDetails.Key, OrderDetails> empMapper = inj.getInstance(MapperFactory.class).make(OrderDetails.class)){
			OrderDetails e = empMapper.loadById(new Key(10250, 65));
			Assert.assertEquals(16.80, e.getUnitPrice());
			Assert.assertEquals(15, e.getQuantity());
			//
			// Act
			//
			e.setUnitPrice(19);
			e.setQuantity(20);
			empMapper.update(e);
			//
			// Assert
			//
			e = empMapper.loadById(new Key(10250, 65));
			Assert.assertEquals(19.0, e.getUnitPrice());
			Assert.assertEquals(20, e.getQuantity());
		}
	}
	@Test
	public void test_insert_and_delete_OrderDetails()throws Exception{
		try(DataMapper<OrderDetails.Key, OrderDetails> empMapper = inj.getInstance(MapperFactory.class).make(OrderDetails.class)){
			OrderDetails e = new OrderDetails(10408, 1, 54.0, 71, 0.3, null, null);
			empMapper.insert(e);
			//
			//	Assert
			//
			OrderDetails newE = empMapper.loadById(new Key(10408, 1));
			Assert.assertNotSame(e, newE);
			Assert.assertEquals(54.0, e.getUnitPrice());
			Assert.assertEquals(71, e.getQuantity());
			Assert.assertEquals(0.3, e.getDiscount());
			//
			// Act again
			//
			empMapper.delete(e);
			//
			//	Assert
			//
			Assert.assertNull(empMapper.loadById(new Key(10408, 1)));
			
		}
	}
}
