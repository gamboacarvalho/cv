package orm.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface ISqlBinder<T>{
	void bind(PreparedStatement stmt, int idx, T value) throws SQLException;
}
