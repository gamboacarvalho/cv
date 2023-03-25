package orm.types;

import orm.mapper.MapperFactory;
import orm.metadata.JdbcColumnInfo;

public interface JdbcTypeFactory<T extends JdbcType<?>> {
	T getInstance(JdbcTypeManager typeManager, JdbcColumnInfo columnInfo, MapperFactory factory);
	Class<?>[] getHandledTypes();
}
