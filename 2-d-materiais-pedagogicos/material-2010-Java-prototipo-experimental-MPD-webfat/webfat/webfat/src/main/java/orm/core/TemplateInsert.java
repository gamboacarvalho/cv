package orm.core;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public abstract class TemplateInsert extends AbstractCmd<Integer>{
	public TemplateInsert(String sql, ISqlBinder... binders) {
	  super(sql, binders);
  }
	/**
	 * params Domain object to insert in the DB. The binder from AbstractCmd
	 * is responsible to fill the parameters of the SQL command. 
	 */
	@Override
  protected Integer performExec(PreparedStatement stmt, Object[] params) throws SQLException {
		if(params.length != 1) throw new IllegalArgumentException("The insert template just receives a unique parameter corresponding to a domain object");
		int res = stmt.executeUpdate();
		ResultSet rs = stmt.getGeneratedKeys();
		rs.next();
		updateKeyOnDomainObject(params[0], rs);
		return res;
  }
	protected abstract void updateKeyOnDomainObject(Object entity, ResultSet ids) throws SQLException ;
}
