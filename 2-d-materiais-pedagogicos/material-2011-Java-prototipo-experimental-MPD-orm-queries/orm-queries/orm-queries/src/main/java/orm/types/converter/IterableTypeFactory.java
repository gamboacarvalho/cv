package orm.types.converter;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import orm.Entity;
import orm.mapper.DataMapper;
import orm.mapper.MapperFactory;
import orm.metadata.JdbcColumnInfo;
import orm.types.*;

public class IterableTypeFactory<E extends Entity<K>, K> implements JdbcTypeFactory<JdbcType<Iterable<E>>>{

	@Override
	public Class<?>[] getHandledTypes() {
		return new Class<?>[]{Iterable.class};
	}


	@Override
	public JdbcType<Iterable<E>> getInstance(JdbcTypeManager typeManager,final JdbcColumnInfo columnInfo, final MapperFactory factory) {
		return new JdbcType<Iterable<E>>(){
			
			private EntityIterable<E,K> lazyIterable;
			@Override
			public int bind(PreparedStatement stmt, int idx,Iterable<E> arg) throws SQLException {return idx;}

			@Override
			public EntityIterable<E, K> convert(ResultSet rs, int idx) throws SQLException {
				this.lazyIterable = new EntityIterable<E,K>(){
					private MapperFactory mapperFactory=factory;
					private JdbcColumnInfo cInfo=columnInfo;
					private Entity<?> entity;
					
					@Override
					public Iterator<E> iterator() {
						@SuppressWarnings({"unchecked"})
						DataMapper<K,E> mapper = (DataMapper<K, E>) mapperFactory.make((Class<E>)cInfo.getGenericType());
						try {
							
							
							return mapper.loadAll().where(cInfo.getJdbcCol().value().toString() + "=" + entity.getId().toString()).iterator();
							
						} catch (SQLException e) {
							throw new RuntimeException(e);
						}
					}

					@Override
					public void setEntity(Entity<?> entity) {
						this.entity=entity;
					}
				};
				return this.lazyIterable;
			}

			@Override
			public boolean requireEntityInstance(){
				return true;
			}

			@Override
			public void setEntity(Entity<?> entity) {
				this.lazyIterable.setEntity(entity);
			}
		};
	}


	
}