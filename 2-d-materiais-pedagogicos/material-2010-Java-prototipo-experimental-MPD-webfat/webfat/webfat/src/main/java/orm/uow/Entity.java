package orm.uow;

import java.util.Collection;
import java.util.LinkedList;

public abstract class Entity<K>{
	public enum EntityState{Clean, New, Updated, ToDelete}
	private static final Collection<EntityObserver> observers = new LinkedList<EntityObserver>();
	protected K _id;
	public Entity(){
		onUpdate(this, EntityState.New);
	}
	public Entity(K id) {
		this._id = id;
		onUpdate(this, EntityState.Clean);
  }
	public static void onUpdate(Entity e, EntityState st){
		for (EntityObserver o : observers) {
	    o.update(e, st);
    }
	}
	public static void addObserver(EntityObserver o){
		observers.add(o);
	}
	public final K getId(){
		return _id;
	}
	public final void setId(K id){
		this._id = id; 
	}
}
