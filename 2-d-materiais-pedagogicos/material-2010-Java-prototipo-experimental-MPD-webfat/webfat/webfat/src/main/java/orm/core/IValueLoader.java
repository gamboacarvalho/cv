package orm.core;

import java.sql.SQLException;

public interface IValueLoader<T> {

	T load() throws SQLException;

}
