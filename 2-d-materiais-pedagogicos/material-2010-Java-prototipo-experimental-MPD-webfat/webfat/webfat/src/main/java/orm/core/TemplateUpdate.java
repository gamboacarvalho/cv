package orm.core;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class TemplateUpdate extends AbstractCmd<Integer>{
	public TemplateUpdate(String sql, ISqlBinder... binders) {
	  super(sql, binders);
  }
	@Override
  protected Integer performExec(PreparedStatement stmt, Object[] params) throws SQLException {
		return stmt.executeUpdate();
  }
}
