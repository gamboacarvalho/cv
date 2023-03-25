package orm;

import orm.executors.JdbcExecutor;
import orm.executors.JdbcExecutorSingleConnection;
import orm.mapper.MapperFactory;
import orm.mapper.MapperFactoryImpl;
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
import app.CfgModule;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.util.Modules;

public class InjectorHelper {
	private static Injector injector = null;
	
	public static Injector getInjector(){
		 if(injector == null){
			 injector = Guice.createInjector(Modules.override(new CfgModule()).with(new AbstractModule(){
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
						bind(MapperFactory.class).to(MapperFactoryImpl.class).in(Singleton.class);
					}
				}));
		 }
		 return injector;
	}
}
