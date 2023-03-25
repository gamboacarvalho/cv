package orm;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import orm.command.JdbcCmdQuery;
import orm.executors.JdbcExecutor;

public final class JdbcQueryIterable<T> implements Iterable<T>{
	private String where = null;
	private String orderBy = null;
	private final JdbcExecutor db;
	private final JdbcCmdQuery<Iterable<T>> cmd;
	public JdbcQueryIterable(JdbcExecutor db, JdbcCmdQuery<Iterable<T>> cmd){
		this.db = db;
		this.cmd = cmd;
	}
	private JdbcQueryIterable(JdbcExecutor db, JdbcCmdQuery<Iterable<T>> cmd, String where, String orderBy){
		this.db = db;
		this.cmd = cmd;
		this.where = where;
		this.orderBy = orderBy;
	}
	
	public JdbcQueryIterable<T> where(String where){
		return new JdbcQueryIterable<T>(db, cmd, this.where == null ? where : this.where+" AND "+where, orderBy);
	}
	public Iterable<T> orderBy(String orderBy){
		return new JdbcQueryIterable<T>(db, cmd, this.where, this.orderBy == null ? orderBy : this.orderBy + ", " + orderBy);
	}
	@Override
	public Iterator<T> iterator() {
		try {
			return db.executeQuery(new JdbcCmdQuery<Iterable<T>>(){
				@Override
				public String getSql() throws SQLException {
					return cmd.getSql()+(where == null ? "" : " WHERE " + where)+(orderBy == null ? "" : " ORDER BY " + orderBy);
				}

				@Override
				public void bind(PreparedStatement stmt, Object... args)
						throws SQLException {
					cmd.bind(stmt, args);
				}

				@Override
				public Iterable<T> loadRows(ResultSet rs) throws SQLException {
					return cmd.loadRows(rs);
				}
				
			}).iterator();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} 
	}
}
