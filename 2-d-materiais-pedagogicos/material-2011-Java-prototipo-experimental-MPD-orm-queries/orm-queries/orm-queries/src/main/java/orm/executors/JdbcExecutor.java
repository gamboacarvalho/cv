package orm.executors;

import java.sql.SQLException;

import orm.Entity;
import orm.command.JdbcCmd;
import orm.command.JdbcCmdQuery;

public interface JdbcExecutor extends AutoCloseable{
	<T> T executeQuery(JdbcCmdQuery<T> cmd, Object...args) throws SQLException;
	public <T> int executeUpdate(JdbcCmd<T> cmd, Object...args) throws SQLException;
	<K, T extends Entity<K>> K executeInsert(JdbcCmdQuery<K> cmd, T arg) throws SQLException;
}
