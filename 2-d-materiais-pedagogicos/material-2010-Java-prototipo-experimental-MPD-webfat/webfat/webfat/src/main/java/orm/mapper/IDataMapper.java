package orm.mapper;

import java.sql.SQLException;

public interface IDataMapper<K, T>{
	Iterable<T> load() throws SQLException;
	T loadById(K key) throws SQLException;
	Iterable<T> where(String whereClause) throws SQLException;
	void insert(T value) throws SQLException;
	void update(T value)throws SQLException;
	void delete(T value)throws SQLException;
}
