package orm.core;

import java.sql.SQLException;

public interface IDBContext {
	<T> T exec(ICmd<T> query, Object... params) throws SQLException;
	void close() throws SQLException;
}
