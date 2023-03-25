package orm.command;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import orm.JdbcBinder;

public class JdbcCmdTemplate<T> implements JdbcCmd<T>{

	final String sql;
	final JdbcBinder [] binders;
	
	public JdbcCmdTemplate(String sql, JdbcBinder...binders) {
		this.sql = sql;
		this.binders = binders;
	}

	@Override
	public String getSql() throws SQLException {
		return sql;
	}

	@Override
	public void bind(PreparedStatement stmt, Object... args) throws SQLException {
		int bindIdx=1;
		for (int i=0; i<args.length;i++)
			for(JdbcBinder binder:binders){
				bindIdx=binder.bind(stmt, bindIdx, args[i]);
			}
	}
}
