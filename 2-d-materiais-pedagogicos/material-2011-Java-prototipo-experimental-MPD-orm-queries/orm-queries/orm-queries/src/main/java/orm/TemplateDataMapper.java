package orm;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import orm.command.JdbcCmd;
import orm.command.JdbcCmdQuery;
import orm.executors.JdbcExecutor;
import orm.mapper.DataMapper;

public abstract class TemplateDataMapper<K, T extends Entity<K>> implements DataMapper<K, T>, AutoCloseable{
	final protected JdbcExecutor db;
	final Map<K, T> identityMap = new HashMap<K, T>();
	
	
	abstract protected JdbcCmdQuery<Iterable<T>> cmdLoadAll();
	abstract protected JdbcCmdQuery<Iterable<T>> cmdLoadById();
	abstract protected JdbcCmd<T> cmdUpdate();
	abstract protected JdbcCmdQuery<K> cmdInsert();
	abstract protected JdbcCmd<T> cmdDelete();
	
	public TemplateDataMapper(JdbcExecutor db) {
		this.db = db;
	}

	protected T getEntity(K id){
		return identityMap.get(id);
	} 
	protected void addEntity(K id, T e){
		identityMap.put(id, e);
	}
	
	private  JdbcQueryIterable<T> lazyLoad(final JdbcCmdQuery<Iterable<T>> cmd){
		return new JdbcQueryIterable<T>(db, cmd);
	}
	
	
	public JdbcQueryIterable<T> loadAll() throws SQLException{
		return lazyLoad(cmdLoadAll());
	}
	
	public JdbcQueryIterable<T> where(final String query) throws SQLException{
		return loadAll().where(query);
	}
	
	
	/*
	 Versao do where somente alerando o TemplateDataMapper
	
	public Iterable<T> where(final String query) throws SQLException{
		final JdbcCmdQuery<Iterable<T>> loadAll = cmdLoadAll();
		return db.executeQuery(new JdbcCmdQuery<Iterable<T>>(){

			@Override
			public String getSql() throws SQLException {
				return loadAll.getSql()+" WHERE "+query;
			}

			@Override
			public void bind(PreparedStatement stmt, Object... args)
					throws SQLException {
				loadAll.bind(stmt, args);
			}

			@Override
			public Iterable<T> loadRows(ResultSet rs) throws SQLException {
				return loadAll.loadRows(rs);
			}
			
		});
	}
	*/
	
	public Iterable<T> orderBy(final String order) throws SQLException{
		return loadAll().orderBy(order);
	}
	
	public T loadById(K id) throws SQLException{
		T value = identityMap.get(id);
		if(value != null) return value;
		Iterator<T> iter = db.executeQuery(cmdLoadById(), id).iterator();
		if(iter.hasNext()){
			value = iter.next();
			identityMap.put(value.getId(), value);
		}
		return value;
	}
	
	public int update(T value) throws SQLException{
		return db.executeUpdate(cmdUpdate(), value); 
	}

	@Override
	public void close() throws Exception {
		db.close();
	}

	@Override
	public void insert(T value) throws SQLException {
		K id = db.executeInsert(cmdInsert(), value);
		identityMap.put(id, value);
		value.setId(id);
	}

	@Override
	public void delete(T value) throws SQLException {
		db.executeUpdate(cmdDelete(), value);
		identityMap.remove(value.getId());
	}
}
