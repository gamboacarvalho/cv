package orm.types;

import orm.Entity;

public interface EntityIterable<E extends Entity<K>, K> extends Iterable<E> {
	 void setEntity(Entity<?> entity);
}
