package orm.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlStringBinder implements ISqlBinder<String>{
	public static final SqlStringBinder SINGLETON = new SqlStringBinder();
	
	private SqlStringBinder (){}
	@Override
  public void bind(PreparedStatement stmt, int idx, String value) throws SQLException {
	  stmt.setString(idx, value);
  }

}
