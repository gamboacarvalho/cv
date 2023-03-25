package orm.types;

import orm.mapper.MapperFactory;
import orm.metadata.JdbcColumnInfo;

public interface JdbcTypeManager {
	public JdbcType get(Class<?> clazz, JdbcColumnInfo columnInfo, MapperFactory factory);	
	public boolean containsType(Class<?> clazz);
}