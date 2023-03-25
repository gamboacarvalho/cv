package orm.types;

import java.sql.ResultSet;
import java.sql.SQLException;
import orm.Entity;
import orm.JdbcBinder;

public interface JdbcType<T> extends JdbcBinder<T>{
	T convert(ResultSet rs, int idx) throws SQLException;
	boolean requireEntityInstance();
	void setEntity(Entity<?> entity);
}