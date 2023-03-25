package orm.northwind.test;

import java.sql.SQLException;
import java.text.ParseException;
import northwind.model.Category;
import northwind.model.Product;
import orm.mapper.MapperFactory;
import org.junit.Test;

import junit.framework.Assert;


import orm.InjectorHelper;
import orm.TemplateDataMapper;


import com.google.inject.Injector;

public class TestCategories {
	TemplateDataMapper<Integer, Category> categoryMapper;
	Injector inj; 
	public TestCategories(){
		inj = InjectorHelper.getInjector();
		categoryMapper = (TemplateDataMapper<Integer, Category>) inj.getInstance(MapperFactory.class).make(Category.class);
	}

	@Test
	public void test_load_all_Categorys() throws SQLException{
		Iterable<Category> res = categoryMapper.loadAll();
		int size = 0;
		for(@SuppressWarnings("unused") Category e:res){size++;}
		Assert.assertEquals(8, size);
	}
	
	@Test
	public void test_load_byid_Category() throws SQLException, ParseException{
		Category e = categoryMapper.loadById(2);
		Assert.assertEquals(2, e.getId()+0);
		Assert.assertEquals("Condiments", e.getCategoryName());
		Assert.assertEquals("Sweet and savory sauces, relishes, spreads, and seasonings", e.getDescription());
		Iterable<Product> products = e.getProducts();
		
		int [] CategoryDetailsId = {3, 4, 5, 6, 8, 15, 44, 61, 63, 65, 66, 77};
		int i = 0;
		for(Product o : products){
			Assert.assertEquals(CategoryDetailsId[i++], o.getId()+0);
		}
	}
}
