package selfdao.jdbc;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

public class JdbcExecutorSingleConnection implements JdbcExecutor, AutoCloseable{
	final private DataSource ds;
	final private boolean autocommit;
	private Connection con;
	
	public JdbcExecutorSingleConnection(DataSource ds, boolean autocommit) {
		this.ds = ds;
		this.autocommit = autocommit;
	}
	
	public JdbcExecutorSingleConnection(DataSource ds) {
		this.ds = ds;
		autocommit = true;
	}

	private Connection initConnection() throws SQLException{
		if(con == null){
			con = ds.getConnection(); // Establish the connection.
			con.setAutoCommit(autocommit);
		}
		return con;
	}
	
	
	private static <T> Iterable<T> buildIterableOrdered(Connection con, String sql, JdbcCmd<T> cmd, Object[] args) {
		return new JdbcIterableQueryBuilder<T>(con, sql, cmd, args){
			@Override
			public Iterable<T> orderBy(String col) {
				throw new UnsupportedOperationException();
			}
			@Override
			public JdbcIterableQuery<T> where(String clause) {
				throw new UnsupportedOperationException();
			}			
		};
	}
	
	private static <T> JdbcIterableQuery<T> buildIterableOrderBy(Connection con, String sql, JdbcCmd<T> cmd, Object[] args) {
		return new JdbcIterableQueryBuilder<T>(con, sql, cmd, args){
			@Override
			public Iterable<T> orderBy(String col) {
				return buildIterableOrdered(super.con, 
						super.sql + " ORDER BY " + col, 
						super.cmd, 
						super.args);
			}
			@Override
			public JdbcIterableQuery<T> where(String clause) {
				throw new UnsupportedOperationException();
			}			
		};
	}
	
	
	private static <T> JdbcIterableQuery<T> buildIterableQuery(Connection con, String sql, JdbcCmd<T> cmd, Object[] args) {
		return new JdbcIterableQueryBuilder<T>(con, sql, cmd, args){
			@Override
			public Iterable<T> orderBy(String col) {
				return buildIterableOrdered(super.con, 
						super.sql + " ORDER BY " + col, 
						super.cmd, 
						super.args);
			}

			@Override
			public JdbcIterableQuery<T> where(String clause) {
				return buildIterableOrderBy(
						super.con, 
						super.sql + " WHERE " + clause, 
						super.cmd, 
						super.args);
			}
			
		};
	}
	
	public <T> JdbcIterableQuery<T> executeQuery(JdbcCmd<T> cmd, Object...args) throws SQLException{
		return buildIterableQuery(initConnection(), cmd.getSql(), cmd, args);
	}
	
	public <T> void executeUpdate(JdbcCmd<T> cmd, Object...args) throws SQLException{
		final String SQL = cmd.getSql();
		try (PreparedStatement stmt = initConnection().prepareStatement(SQL)) {
			con.setAutoCommit(autocommit);
			cmd.bind(stmt, args);
			stmt.executeUpdate();
		}		
	}

	@Override
	public <K> K executeInsert(JdbcCmd<K> cmd, Object... args) throws SQLException {
		final String SQL = cmd.getSql();
		try (PreparedStatement stmt = initConnection().prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)	
				) {
			con.setAutoCommit(autocommit);
			cmd.bind(stmt, args);
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			rs.next();
			return cmd.convert(rs);
		}		
	}
	
	@Override
	public void close() throws Exception {
		if(con != null){
			if(!autocommit) con.rollback();
			con.close();
			con = null;
		}
	}
}






