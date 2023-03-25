package orm.uow;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import collect.IterUtils;
import collect.Predicate;

import orm.mapper.IDataMapper;
import orm.northwind.Employee;

public class RepositoryOrdered implements IRepository{
	Map<Class<? extends Entity>, IDataMapper> mappers = new LinkedHashMap<Class<? extends Entity>, IDataMapper>();
	private final UnitOfWork uow;
	public RepositoryOrdered(UnitOfWork uuow) {
	  this.uow = uuow;
  }	
	public void add(Class<? extends Entity> domainClass, IDataMapper mapper){
	  IDataMapper old  = mappers.put(domainClass, mapper);
	  if(old != null) throw new IllegalArgumentException("There is already a DataMapper for domain class " + domainClass);
  }

	@Override
  public <V extends Entity<?>> Iterable<V> load(Class<V> domainClass) throws SQLException {
	  IDataMapper mapper = mappers.get(domainClass);
	  if(mapper == null) throw new IllegalArgumentException("There is no DataMapper for domain class " + domainClass);
	  Iterable<V> res = mapper.load();
	  return wrapIterable(res);
  }
	public <V extends Entity<?>> Iterable<V> where(Class<V> domainClass, String whereClause) throws SQLException{
		IDataMapper mapper = mappers.get(domainClass);
	  if(mapper == null) throw new IllegalArgumentException("There is no DataMapper for domain class " + domainClass);
	  Iterable<V> res = mapper.where(whereClause);
	  return wrapIterable(res);
	}
	@Override
  public <K, V extends Entity<K>> V loadById(Class<V> domainClass, K key)
      throws SQLException {
	  IDataMapper<K, V> mapper = mappers.get(domainClass);
	  if(mapper == null) throw new IllegalArgumentException("There is no DataMapper for domain class " + domainClass);
	  V res = mapper.loadById(key);
	  uow.registerClean(res);
	  return res;
  }
	
	public <V extends Entity> Iterable<V> wrapIterable(final Iterable<V> values){
		return new Iterable<V>() {
      public Iterator<V> iterator() {
	      return new Iterator<V>() {
	      	Iterator<V> iter = values.iterator();
          public boolean hasNext() {return iter.hasNext();}
          public V next() {
	          V res = iter.next();
	          uow.registerClean(res);
	          return res;
          }
          public void remove() {
	          throw new UnsupportedOperationException();
          }
				};
      }			
		};
	}
	@Override
  public void save() throws SQLException {
	  uow.commit();
  }
	public void insertNew(Iterable<Entity> newDomainobjects) throws SQLException{
		for (Map.Entry<Class<? extends Entity>, IDataMapper> node : mappers.entrySet()) {			 
	    for (Entity entity : whereDomainClassEqualsTo(newDomainobjects, node.getKey())) {
	    	node.getValue().insert(entity);
      }
    }
	}
	@Override
  public void updateDirty(Iterable<Entity> dirtyDomainobjects)
      throws SQLException {
		for (Map.Entry<Class<? extends Entity>, IDataMapper> node : mappers.entrySet()) {
	    for (Entity entity : whereDomainClassEqualsTo(dirtyDomainobjects, node.getKey())) {
	    	node.getValue().update(entity);
      }
    }
  }
	@Override
  public void deleteOld(Iterable<Entity> oldDomainobjects) throws SQLException {
		for (Map.Entry<Class<? extends Entity>, IDataMapper> node : mappers.entrySet()) {			 
	    for (Entity entity : whereDomainClassEqualsTo(oldDomainobjects, node.getKey())) {
	    	node.getValue().delete(entity);
      }
    }
  }
	private static Iterable<Entity> whereDomainClassEqualsTo(
			Iterable<Entity> source, 
			final Class<? extends Entity> domainClass){
		return IterUtils.where(source, new Predicate<Entity>() {
      public boolean invoke(Entity item) {
        return item.getClass() == domainClass;
      }	    	
		});
	}
}
