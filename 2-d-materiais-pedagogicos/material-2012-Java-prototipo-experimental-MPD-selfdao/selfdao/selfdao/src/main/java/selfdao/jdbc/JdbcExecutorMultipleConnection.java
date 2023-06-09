package selfdao.jdbc;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

public class JdbcExecutorMultipleConnection implements JdbcExecutor{
	final private DataSource ds;
	final private boolean autocommit;

	public JdbcExecutorMultipleConnection(DataSource ds, boolean autocommit) {
		this.ds = ds;
		this.autocommit = autocommit;
	}
	
	public JdbcExecutorMultipleConnection(DataSource ds) {
		this.ds = ds;
		autocommit = true;
	}

	public <T> JdbcIterableQuery<T> executeQuery(JdbcCmd<T> cmd, Object...args) throws SQLException{
			return null;
	}
	
	public <T> void executeUpdate(JdbcCmd<T> cmd, Object...args) throws SQLException{
		final String SQL = cmd.getSql();
		try (Connection con = ds.getConnection(); // Establish the connection.
				PreparedStatement stmt = con.prepareStatement(SQL)	
				) {
			con.setAutoCommit(autocommit);
			cmd.bind(stmt, args);
			stmt.executeUpdate();
		}		
	}

	@Override
	public void close() throws Exception {
	}

	@Override
	public <K> K executeInsert(JdbcCmd<K> cmd, Object... args) throws SQLException {
		final String SQL = cmd.getSql();
		try (Connection con = ds.getConnection(); // Establish the connection.
				PreparedStatement stmt = con.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)	
				) {
			con.setAutoCommit(autocommit);
			cmd.bind(stmt, args);
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			return cmd.convert(rs);
		}		
	}
}






