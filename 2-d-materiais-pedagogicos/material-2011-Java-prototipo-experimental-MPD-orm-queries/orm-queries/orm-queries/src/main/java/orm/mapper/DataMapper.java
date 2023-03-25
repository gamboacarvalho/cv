package orm.mapper;

import java.sql.SQLException;

import orm.Entity;
import orm.JdbcQueryIterable;

public interface DataMapper<K, T extends Entity<K>> extends AutoCloseable{
	JdbcQueryIterable<T> loadAll() throws SQLException;
	T loadById(K id) throws SQLException;
	int update(T value) throws SQLException;
	void insert(T value) throws SQLException;
	void delete(T value) throws SQLException;
}
