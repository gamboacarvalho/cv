package orm.uow;

import java.sql.SQLException;

import orm.mapper.IDataMapper;
import orm.northwind.Employee;

public interface IRepository {
	<V extends Entity<?>> Iterable<V> load(Class<V> domainClass) throws SQLException;
	<K, V extends Entity<K>>  V loadById(Class<V> domainClass, K key) throws SQLException;
	<V extends Entity<?>> Iterable<V> where(Class<V> domainClass, String whereClause) throws SQLException;
	void add(Class<? extends Entity> domainClass, IDataMapper mapper);
	void save()throws SQLException;
	
	void insertNew(Iterable<Entity> newDomainobjects) throws SQLException;
	void updateDirty(Iterable<Entity> dirtyDomainobjects) throws SQLException;
	void deleteOld(Iterable<Entity> oldDomainobjects) throws SQLException;
}
