package selfdao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface JdbcConverter<T> {

	T convert(ResultSet rs) throws SQLException ;

}
