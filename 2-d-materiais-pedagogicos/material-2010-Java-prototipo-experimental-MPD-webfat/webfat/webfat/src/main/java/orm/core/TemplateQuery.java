package orm.core;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class TemplateQuery<R> extends AbstractCmd<Iterable<R>>{
	public final IConverter<R> conv;
	public TemplateQuery(String sql, IConverter<R> conv, ISqlBinder... binders) {
	  super(sql, binders);
	  this.conv = conv;
  }
	@Override
  protected Iterable<R> performExec(PreparedStatement stmt, Object[] params) throws SQLException {
		ResultSet rs = stmt.executeQuery();
		LinkedList<R> res = new LinkedList<R>();
		// Iterate through the data in the result set.
		while (rs.next()) {
			res.add(conv.convert(rs));
		}
		return res;
  }
	public TemplateQuery<R> where(String whereClause) {
		if(sql.toUpperCase().indexOf(" WHERE ") >= 0)
			return new TemplateQuery<R>(
		  		sql + " AND " + whereClause, 
		  		conv, 
		  		binders.toArray(new ISqlBinder[binders.size()]));
		else
			return new TemplateQuery<R>(
	  		sql + " WHERE " + whereClause, 
	  		conv, 
	  		binders.toArray(new ISqlBinder[binders.size()]));
  }

}
