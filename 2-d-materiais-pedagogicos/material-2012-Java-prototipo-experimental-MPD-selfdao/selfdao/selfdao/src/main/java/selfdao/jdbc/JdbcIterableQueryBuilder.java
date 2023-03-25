package selfdao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public abstract class JdbcIterableQueryBuilder<T> implements JdbcIterableQuery<T>{

	protected final Connection con;
	protected final JdbcCmd<T> cmd;
	protected final Object[] args;
	protected final String sql; 
	
	
	public JdbcIterableQueryBuilder(Connection con, String sql, JdbcCmd<T> cmd, Object[] args) {
		this.con = con;
		this.cmd = cmd;
		this.args = args;
		this.sql = sql;
	}

	@Override
	public Iterator<T> iterator() {
		try (PreparedStatement stmt = con.prepareStatement(sql)) {
			cmd.bind(stmt, args);
			ResultSet rs = stmt.executeQuery();
			List<T> res = new LinkedList<T>();
			/*
			 * Iterate through the data in the result set and display it.
			 */
			while (rs.next()) {
				T elem = cmd.convert(rs);
				res.add(elem);
			}
			return res.iterator();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
