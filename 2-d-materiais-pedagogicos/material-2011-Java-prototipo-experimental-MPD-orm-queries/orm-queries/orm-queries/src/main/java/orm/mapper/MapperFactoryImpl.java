package orm.mapper;

import java.util.HashMap;
import java.util.Map;

import orm.Entity;
import orm.executors.JdbcExecutor;
import orm.types.*;

import com.google.inject.Inject;

public class MapperFactoryImpl implements MapperFactory {
	private static final Map<Class<?>, DataMapper<?, ?>> mapperMap = new HashMap<Class<?>, DataMapper<?, ?>>();
	private JdbcExecutor db;
	private JdbcTypeManager typeManager;
	
	@Inject
	public MapperFactoryImpl(JdbcExecutor db, JdbcTypeManager typeManager) {
		this.db = db;
		this.typeManager=typeManager;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <K, T extends Entity<K>> DataMapper<K, T> make(Class<T> entityClass){
		DataMapper<K, T> mapper = (DataMapper<K, T>) mapperMap.get(entityClass);
		if(mapper==null){
			mapperMap.put(entityClass, mapper= new JdbcGenericDataMapper(db, entityClass,this,typeManager));
		}
		return mapper;
	}
}
