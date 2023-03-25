package orm.command;

import java.sql.ResultSet;
import java.sql.SQLException;


public interface JdbcCmdQuery<T> extends JdbcCmd<T>{
	T loadRows(ResultSet rs) throws SQLException;
}
