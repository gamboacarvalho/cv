package orm.uow;

import orm.uow.Entity.EntityState;

public interface EntityObserver {
	public void update(Entity e, EntityState st);
}
