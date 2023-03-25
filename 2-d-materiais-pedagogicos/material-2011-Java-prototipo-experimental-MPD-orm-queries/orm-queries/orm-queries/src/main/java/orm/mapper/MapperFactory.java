package orm.mapper;

import orm.Entity;


public interface MapperFactory {
	public <K, T extends Entity<K>> DataMapper<K, T> make(Class<T> entityClass);
}
