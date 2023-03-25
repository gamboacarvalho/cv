package orm.types.converter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import orm.Entity;
import orm.mapper.DataMapper;
import orm.mapper.MapperFactory;
import orm.metadata.JdbcColumnInfo;
import orm.types.JdbcType;
import orm.types.JdbcTypeFactory;
import orm.types.JdbcTypeManager;
import orm.types.ValueHolder;

public class ValueHolderType<T extends Entity<K>, K> implements JdbcTypeFactory<JdbcType<ValueHolder<T>>> {


	@Override
	public Class<?>[] getHandledTypes() {
		return new Class<?>[]{ValueHolder.class};
	}

	@Override
	public JdbcType<ValueHolder<T>> getInstance(final JdbcTypeManager typeManager,final JdbcColumnInfo columnInfo, final MapperFactory factory) {
		@SuppressWarnings("unchecked")
		final DataMapper<K,T> map = factory.make((Class<T>) columnInfo.getGenericType());
		return new JdbcType<ValueHolder<T>>(){

			@Override
			public int bind(PreparedStatement stmt, int idx, ValueHolder<T> arg) throws SQLException {
				//nao deve ser gravado em BD
				return idx;
			}

			@Override
			public ValueHolder<T> convert(ResultSet rs, int idx) throws SQLException {

				@SuppressWarnings("unchecked")
				final K value = (K) typeManager.get(columnInfo.getJdbcCol().referencedKeyClass(),columnInfo, factory).convert(rs, idx);
				return new ValueHolder<T>(){
					DataMapper<K,T> mapper=map;
					K key = value;
					
					@Override
					public T get() {	
						try {
							return mapper.loadById(key);
						} catch (SQLException e) {
							throw new RuntimeException(e);
						}
					}
				};
			}

			@Override
			public boolean requireEntityInstance() {
				return false;
			}

			@Override
			public void setEntity(Entity<?> entity) {}
		};
	}

	
}
