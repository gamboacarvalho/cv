package orm.core;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractCmd<T> implements ICmd<T>{
	public final Collection<ISqlBinder> binders;
	protected final String sql;
	/**
	 * @param binders The ISqlBinder order must respect the parameters order in Sql query.
	 */
	public AbstractCmd(String sql, ISqlBinder... binders) {
	  super();
	  this.binders = Arrays.asList(binders);
	  this.sql = sql;
  }
	@Override
  public final T exec(PreparedStatement stmt, Object... params) throws SQLException {
		fillStmt(stmt, params);
	  return performExec(stmt, params);
  }
	protected abstract T performExec(PreparedStatement stmt, Object[] params) throws SQLException;
	protected final void fillStmt(PreparedStatement stmt, Object...params) throws SQLException{
		if(params.length != binders.size()) throw new IllegalArgumentException("The parameters are not according to sepecified binders!");
		int idx = 1;
		for (ISqlBinder b: binders) {
			b.bind(stmt, idx, params[idx-1]);
			idx++;
    }
	}
	@Override
  public String getSql() {
    return sql;
  }
}
