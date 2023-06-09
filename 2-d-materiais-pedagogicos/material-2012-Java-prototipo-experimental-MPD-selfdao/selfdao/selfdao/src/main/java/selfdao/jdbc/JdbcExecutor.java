package selfdao.jdbc;

import java.sql.SQLException;

public interface JdbcExecutor extends AutoCloseable{

	public <T> JdbcIterableQuery<T> executeQuery(JdbcCmd<T> cmd, Object...args) throws SQLException;
	
	public <T> void executeUpdate(JdbcCmd<T> cmd, Object...args) throws SQLException;
	
	public <K> K executeInsert(JdbcCmd<K> cmd, Object...args) throws SQLException;
}
