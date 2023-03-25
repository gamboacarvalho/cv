package orm.northwind.test;

import static org.junit.Assert.*;

import junit.framework.Assert;

import northwind.model.OrderDetails;
import northwind.model.OrderDetails.Key;

import org.junit.Test;

import orm.mapper.MapperFactory;
import orm.InjectorHelper;
import orm.TemplateDataMapper;
import com.google.inject.Injector;

public class TestOrderDetails {

	Injector inj; 
	public TestOrderDetails(){
		inj = InjectorHelper.getInjector();
	}
	
	
	@Test
	public void test_load_all_with_where_OrderDetails_and_testValueHolder() throws Exception{
		try(TemplateDataMapper<OrderDetails.Key, OrderDetails> orderDetailsMapper = (TemplateDataMapper<Key, OrderDetails>) inj.getInstance(MapperFactory.class).make(OrderDetails.class)){
			Iterable<OrderDetails> res = orderDetailsMapper.loadAll().where("ProductID < 9");
			int[] orderId = new int[]{10255, 10258, 10258, 10262, 10262, 10264, 10285, 10289, 10290};
			int[] prodId = new int[]{2, 2, 5, 5, 7, 2, 1, 3, 5};
			int size = 0;
			for(@SuppressWarnings("unused") OrderDetails e:res){
				//testar somente os primeiro 9 registos em termos de chaves primarias e value holder
				if(size<orderId.length){
					Assert.assertEquals(e.getId().OrderId, orderId[size]);
					Assert.assertEquals(e.getId().ProductId, prodId[size]);
				}
				size++;
			}
			Assert.assertEquals(178, size);
		}
	}
	
	@Test
	public void test_delete_OrderDetails() throws Exception{
		try(TemplateDataMapper<OrderDetails.Key, OrderDetails> orderDetailsMapper = 
			(TemplateDataMapper<Key, OrderDetails>) inj.getInstance(MapperFactory.class).make(OrderDetails.class)){
			OrderDetails res = orderDetailsMapper.loadById(new OrderDetails.Key(11003, 1));
			assertNotNull(res);
			
			orderDetailsMapper.delete(res);
			
			res = orderDetailsMapper.loadById(new OrderDetails.Key(11003, 1));
			
			assertNull(res);
		}
	}
	
}
