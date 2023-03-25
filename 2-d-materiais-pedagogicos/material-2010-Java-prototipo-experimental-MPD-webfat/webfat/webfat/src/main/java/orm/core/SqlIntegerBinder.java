package orm.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlIntegerBinder implements ISqlBinder<Integer>{

	public static final ISqlBinder SINGLETON = new SqlIntegerBinder();
	private SqlIntegerBinder(){}

	@Override
  public void bind(PreparedStatement stmt, int idx, Integer value) throws SQLException {
	  stmt.setInt(idx, value);
  }

}
