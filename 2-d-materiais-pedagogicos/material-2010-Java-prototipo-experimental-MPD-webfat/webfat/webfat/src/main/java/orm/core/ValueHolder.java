package orm.core;

import java.sql.SQLException;

public class ValueHolder<T> {
	private T cache;
	private final IValueLoader<T> loader;
	
	public ValueHolder(IValueLoader<T> loader) {
	  super();
	  this.loader = loader;
  }

	public T get() throws SQLException{
		if(cache == null){
			cache = loader.load();
		}
		return cache;
	}
}
