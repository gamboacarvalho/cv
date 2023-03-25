package orm.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import orm.core.IConverter;
import orm.core.IDBContext;
import orm.core.ISqlBinder;
import orm.core.TemplateInsert;
import orm.core.TemplateQuery;
import orm.core.TemplateUpdate;
import orm.northwind.Employee;
import orm.uow.Entity;

public abstract class AbstractDataMapper<K, V extends Entity> implements IDataMapper<K, V>{
	
	// Hook methods
	protected abstract TemplateQuery<V> getQueryCmd();
	protected abstract TemplateQuery<V> getQueryByIdCmd();
	protected abstract TemplateUpdate getUpdateCmd();
	protected abstract TemplateUpdate getDeleteCmd();
	protected abstract TemplateInsert getInsertCmd();
	protected abstract K doGetKeyFrom(ResultSet rs) throws SQLException;
	
	
	private final Map<K, V> identityMap = new HashMap<K, V>();
	private final IDBContext ctx;
	private Map<TemplateQuery<V>, TemplateQuery<V>> cacheQueryCheckingIdentityMap = new HashMap<TemplateQuery<V>, TemplateQuery<V>>(); 
	
	public AbstractDataMapper(IDBContext ctx) {
	  super();
	  this.ctx = ctx;
  }
	/**
	 * A new converter that checks the identityMap before converting 
	 * the resultset into a new identity object. 
	 */
	private IConverter<V> makeConverterCheckingIdentityMap(final IConverter<V> conv){
		return new IConverter<V>() {
      public V convert(ResultSet rs) throws SQLException {
	    	K key = doGetKeyFrom(rs);
	    	V value = identityMap.get(key);
	    	if(value == null){
	    		value = conv.convert(rs);
	    		identityMap.put(key, value);
	    	}
    		return value;
      }
		};
	}
	/**
	 * Return a new query that checks the identity map 
	 * before invoking the converter.
	 */
	private final TemplateQuery<V> getQueryCmdCheckingIdentityMap(TemplateQuery<V> q){
		TemplateQuery<V> qCached = cacheQueryCheckingIdentityMap.get(q);
		if(qCached == null){
			qCached = new TemplateQuery<V>(
					q.getSql(), 
					makeConverterCheckingIdentityMap(q.conv), 
					q.binders.toArray(new ISqlBinder[q.binders.size()]));
			cacheQueryCheckingIdentityMap.put(q, qCached);
		}
		return qCached;
	}
	@Override
  public Iterable<V> load() throws SQLException {
	  return ctx.exec(getQueryCmdCheckingIdentityMap(getQueryCmd()));
  }
	@Override
  public V loadById(K key) throws SQLException{
		Iterable<V> res = ctx.exec(
				getQueryCmdCheckingIdentityMap(getQueryByIdCmd()), 
				key);
		Iterator<V> iter = res.iterator();
		return iter.hasNext()?iter.next():null;
	}
  @Override
  public Iterable<V> where(String whereClause) throws SQLException {
	  return ctx.exec(
	  		getQueryCmdCheckingIdentityMap(
	  				getQueryCmd().where(whereClause)));
  }
	@Override
  public void update(V value) throws SQLException {
	  ctx.exec(getUpdateCmd(), value);
  }
	@Override
  public void insert(V value) throws SQLException {
		ctx.exec(getInsertCmd(), value);
  }
	@Override
  public void delete(V value) throws SQLException {
		ctx.exec(getDeleteCmd(), value);
		identityMap.remove(value.getId());
  }
}
