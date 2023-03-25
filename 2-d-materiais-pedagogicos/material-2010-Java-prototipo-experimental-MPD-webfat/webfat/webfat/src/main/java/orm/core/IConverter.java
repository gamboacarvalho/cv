package orm.core;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IConverter<T> {
	T convert(ResultSet rs) throws SQLException;
}
