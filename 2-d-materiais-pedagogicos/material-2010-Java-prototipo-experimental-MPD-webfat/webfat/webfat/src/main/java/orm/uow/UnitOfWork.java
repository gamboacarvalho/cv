package orm.uow;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import collect.IterUtils;
import collect.Predicate;
import collect.Projection;
import collect.Func;

import orm.mapper.IDataMapper;
import orm.uow.Entity.EntityState;
import orm.uow.Tracker.State;

public class UnitOfWork implements EntityObserver{
	private final Map<Entity, Tracker> trackers = new HashMap<Entity, Tracker>();
	private IRepository repo;
	public UnitOfWork() {
	  Entity.addObserver(this);
  }
	public void setRepository(IRepository r){
		repo = r;
	}
	
	public <K> void registerNew(Entity<K> e){
		Tracker<K> t = new Tracker<K>(e, State.New);
		Tracker<K> old = trackers.put(e, t);
		if(old != null) throw new IllegalStateException("This entity was already tracked by the unit of work!!!");
	}
	/**
	 * Invoked by query methods such as: loadAll, loadById and where.
	 */
	public  <K> void registerClean(Entity<K> e){
		Tracker<K> t = trackers.get(e);
		if(t == null){
			t = new Tracker<K>(e, State.Clean);
			trackers.put(e, t);
		}
		t.commit();		
	}
	@Override
  public void update(Entity e, EntityState st) {
		if(st == EntityState.New)
			registerNew(e);
		if(st == EntityState.Clean)
			registerClean(e);
		if(st == EntityState.Updated)
			registerDirty(e);
		if(st == EntityState.ToDelete)
			registerDelete(e);
  }
	public <K> void registerDirty(Entity<K> e){
		Tracker<K> old = trackers.get(e);
		if(old == null) throw new IllegalStateException("This entity is not tracked by unit of work !!!!!");
		old.update();
	}
	public <K> void registerDelete(Entity<K> e){
		Tracker<K> old = trackers.get(e);
		if(old == null) throw new IllegalStateException("This entity is not tracked by unit of work !!!!!");
		old.delete();
	}
	public void commit() throws SQLException{
		repo.insertNew(getEntitiesInState(State.New));
		repo.updateDirty(getEntitiesInState(State.Dirty));
		repo.deleteOld(getEntitiesInState(State.ToDelete));
		commitTrackers();
	}
	private Iterable<Entity> getEntitiesInState(final State st){
		Iterable<Tracker> filtered = IterUtils.where(trackers.values(), new Predicate<Tracker>() {
      public boolean invoke(Tracker item) {
	      return item.getState() == st;
      }
		});
		return IterUtils.select(filtered, new Projection<Tracker, Entity>() {
      public Entity invoke(Tracker item) {
	      return item.entity;
      }			
		});
	}
	private void commitTrackers(){
		IterUtils.forEach(trackers.values(), new Func<Tracker>() {
			public void invoke(Tracker item){
				if(item.getState() == State.New || item.getState() == State.Dirty || item.getState() == State.ToDelete)
					item.commit();
			}
		});
	}
}
