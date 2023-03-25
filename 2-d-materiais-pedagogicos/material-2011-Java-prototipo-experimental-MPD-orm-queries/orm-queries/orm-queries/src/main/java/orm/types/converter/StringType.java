package orm.types.converter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import orm.mapper.MapperFactory;
import orm.metadata.JdbcColumnInfo;
import orm.types.AbstractJdbcType;
import orm.types.JdbcTypeManager;

public class StringType extends AbstractJdbcType<String> {

	public int bind(PreparedStatement stmt, int idx, String arg) throws SQLException{
		stmt.setString(idx++, arg);
		return idx;
	}
	public String convert(ResultSet rs, int idx) throws SQLException{
		return rs.getString(idx);
	}
	
	@Override
	public StringType getInstance(JdbcTypeManager typeManager, JdbcColumnInfo columnInfo, MapperFactory factory) {
		return this;
	}
	
	@Override
	public Class<?>[] getHandledTypes() {
		return new Class<?>[]{String.class};
	}
}