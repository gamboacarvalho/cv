package orm;

import static org.junit.Assert.*;
import northwind.model.OrderDetails;
import northwind.model.Product;
import orm.JdbcSqlGenerator;

import orm.executors.JdbcExecutor;
import orm.executors.JdbcExecutorSingleConnection;
import orm.mapper.MapperFactory;
import orm.mapper.MapperFactoryImpl;
import orm.metadata.JdbcEntityInfo;
import orm.types.JdbcTypeFactory;
import orm.types.JdbcTypeManager;
import orm.types.JdbcTypeManagerImpl;
import orm.types.converter.BooleanType;
import orm.types.converter.DateType;
import orm.types.converter.DoubleType;
import orm.types.converter.FloatType;
import orm.types.converter.IntegerType;
import orm.types.converter.IterableTypeFactory;
import orm.types.converter.StringType;
import orm.types.converter.ValueHolderType;

import org.junit.Test;

import app.CfgModule;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.util.Modules;

public class TestJdbcSqlGenrator {
	Injector inj; 
	public TestJdbcSqlGenrator(){
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
	@Test
	public void test_get_insert_query(){
		JdbcEntityInfo pi = new JdbcEntityInfo(Product.class);
		JdbcSqlGenerator sg = new JdbcSqlGenerator(pi);
		assertEquals("INSERT INTO [Products] ([ProductName], [UnitPrice], [UnitsInStock]) VALUES (?, ?, ?)", sg.insertSql().trim());
	}
	@Test
	public void test_get_select_query(){
		JdbcEntityInfo pi = new JdbcEntityInfo(Product.class);
		JdbcSqlGenerator sg = new JdbcSqlGenerator(pi);
		assertEquals("SELECT [ProductId], [ProductName], [UnitPrice], [UnitsInStock], [SupplierId], [CategoryID] FROM [Products]", sg.selectSql().trim());
	}
	@Test
	public void test_get_delete_query(){
		JdbcEntityInfo pi = new JdbcEntityInfo(Product.class);
		JdbcSqlGenerator sg = new JdbcSqlGenerator(pi);
		assertEquals("DELETE FROM [Products] WHERE [ProductId] = ?", sg.deleteSql().trim());
	}

	@Test
	public void test_get_update_query(){
		JdbcEntityInfo pi = new JdbcEntityInfo(Product.class);
		JdbcSqlGenerator sg = new JdbcSqlGenerator(pi);
		assertEquals("UPDATE [Products] SET [ProductName] = ?, [UnitPrice] = ?, [UnitsInStock] = ? WHERE [ProductId] = ?", sg.updateSql().trim());
	}

	@Test
	public void test_get_select_by_id_query(){
		JdbcEntityInfo pi = new JdbcEntityInfo(Product.class);
		JdbcSqlGenerator sg = new JdbcSqlGenerator(pi);
		assertEquals("SELECT [ProductId], [ProductName], [UnitPrice], [UnitsInStock], [SupplierId], [CategoryID] FROM [Products] WHERE [ProductId] = ?", sg.selectByIdSql().trim());
	}
	
	@Test
	public void test_get_select_by_id_query_multi_pk(){
		JdbcEntityInfo pi = new JdbcEntityInfo(OrderDetails.class);
		JdbcSqlGenerator sg = new JdbcSqlGenerator(pi);
		assertEquals("SELECT [OrderId], [ProductId], [UnitPrice], [Quantity], [Discount], [OrderId], [ProductId] FROM [Order Details] WHERE [OrderId] = ? AND [ProductId] = ?", sg.selectByIdSql().trim());
	}
}
