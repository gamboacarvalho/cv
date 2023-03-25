package orm;

import static org.junit.Assert.*;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import northwind.model.OrderDetails;
import northwind.model.Product;
import orm.executors.JdbcExecutor;
import orm.executors.JdbcExecutorSingleConnection;
import orm.mapper.DataMapper;
import orm.mapper.MapperFactory;
import orm.mapper.MapperFactoryImpl;
import orm.metadata.JdbcColumnInfo;
import orm.metadata.JdbcEntityInfo;
import orm.types.JdbcType;
import orm.types.JdbcTypeFactory;
import orm.types.JdbcTypeManager;
import orm.types.JdbcTypeManagerImpl;
import orm.types.ValueHolder;

import org.junit.Test;

import app.CfgModule;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.util.Modules;
import com.google.inject.multibindings.Multibinder;
import orm.types.converter.*;

public class TestDataMapper {
	Injector inj; 
	public TestDataMapper(){
		inj = Guice.createInjector(Modules.override(new CfgModule()).with(new AbstractModule(){
			@Override
			protected void configure() {
				bind(JdbcExecutor.class).to(JdbcExecutorSingleConnection.class).in(Singleton.class);
				bind(boolean.class).annotatedWith(DbRollback.class).toInstance(true);
				Multibinder<JdbcTypeFactory> supportedTypes = Multibinder.newSetBinder(binder(), JdbcTypeFactory.class);
				supportedTypes.addBinding().to(BooleanType.class);
				supportedTypes.addBinding().to(DateType.class);
				supportedTypes.addBinding().to(DoubleType.class);
				supportedTypes.addBinding().to(FloatType.class);
				supportedTypes.addBinding().to(IntegerType.class);
				supportedTypes.addBinding().to(IterableTypeFactory.class);
				supportedTypes.addBinding().to(StringType.class);
				supportedTypes.addBinding().to(ValueHolderType.class);
				
				bind(JdbcTypeManager.class).to(JdbcTypeManagerImpl.class);
				bind(MapperFactory.class).to(MapperFactoryImpl.class);
			}
		}));
	}
	@SuppressWarnings("deprecation")
	@Test
	public void test_entity_info(){
		JdbcEntityInfo cols = new JdbcEntityInfo(Product.class);
		Class<?>[] ar = new Class<?>[]{int.class, String.class, double.class, int.class, ValueHolder.class, Iterable.class, ValueHolder.class};
		ArrayList<Class<?>> arRes = new ArrayList<Class<?>>();
		for(JdbcColumnInfo c : cols)
			arRes.add(c.getType());
		
		assertEquals(ar, arRes.toArray());
	}
	
	@Test
	public void test_simple_select_without_iterable_and_valueholder() throws SQLException{
		MapperFactory map = inj.getInstance(MapperFactory.class);
		DataMapper<Integer, Product> dm = map.make(Product.class);
		Iterable<Product> products = dm.loadAll().where(" ProductId = 10").orderBy("ProductId");
		Iterator<Product> it = products.iterator();
		assertTrue(it.hasNext());
		Product p = it.next();
		
		assertEquals("Ikura", p.getProductName());
		assertEquals(31.0, p.getUnitPrice(), 0);
		assertEquals(31, p.getUnitsInStock()+0);
		assertEquals(10, p.getId()+0);
		
	}
	@Test
	public void test_simple_update_without_iterable_and_valueholder() throws Exception{
		MapperFactory map = inj.getInstance(MapperFactory.class);
		
		
		try(TemplateDataMapper<Integer, Product> dm = (TemplateDataMapper<Integer, Product>) map.make(Product.class)){
			Iterable<Product> products = dm.loadAll().where(" ProductId = 10").orderBy("ProductId");
			Iterator<Product> it = products.iterator();
			assertTrue(it.hasNext());
			Product p = it.next();
			
			assertEquals("Ikura", p.getProductName());
			
			p.setProductName("new Name");
			dm.update(p);
			
			p = dm.loadById(10);
			
			//System.out.println(p.getSupplier());
			
			assertEquals("new Name", p.getProductName());
			
			assertEquals(10, p.getId()+0);
		}
	
	}
	@Test
	public void test_multikey_update_without_iterable_and_valueholder() throws Exception{
		MapperFactory map = inj.getInstance(MapperFactory.class);
		
		try(TemplateDataMapper<OrderDetails.Key, OrderDetails> dm = (TemplateDataMapper<OrderDetails.Key, OrderDetails>) map.make(OrderDetails.class)){
			/*Iterable<OrderDetails> ordersdetail = dm.loadAll().where("OrderId = 10248 AND ProductId = 11");
			
			for(OrderDetails order : ordersdetail){
				System.out.println(order.getId().OrderId + " - " + order.getId().ProductId);
	
			}*/
			OrderDetails order = dm.loadById(new OrderDetails.Key(10248, 11));
			assertEquals(12,order.getQuantity());
			assertEquals(0.0,order.getDiscount(), 0);
			assertEquals(14.0,order.getUnitPrice(), 0);
			assertEquals(10248,order.getId().OrderId);
			assertEquals(11,order.getId().ProductId);
		}
		
		
	
	}
}
