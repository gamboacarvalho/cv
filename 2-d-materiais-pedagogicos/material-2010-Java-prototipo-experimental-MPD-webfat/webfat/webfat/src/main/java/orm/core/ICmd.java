package orm.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface ICmd<T> {
	String getSql();
	T exec(PreparedStatement stmt, Object...params) throws SQLException;
}
