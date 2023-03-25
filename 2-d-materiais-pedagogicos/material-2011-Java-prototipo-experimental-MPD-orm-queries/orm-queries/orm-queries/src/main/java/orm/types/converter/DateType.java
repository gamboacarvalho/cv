package orm.types.converter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import orm.mapper.MapperFactory;
import orm.metadata.JdbcColumnInfo;
import orm.types.AbstractJdbcType;
import orm.types.JdbcTypeManager;

public class DateType extends AbstractJdbcType<Date>{
	public Class<?>[] getHandledTypes(){
		return new Class<?>[]{Date.class};
	}
	
	public int bind(PreparedStatement stmt, int idx, Date arg) throws SQLException{
		stmt.setDate(idx++, new java.sql.Date(arg.getTime()));
		return idx;
	}
	
	public Date convert(ResultSet rs, int idx) throws SQLException{
		return rs.getDate(idx);
		
	}

	@Override
	public DateType getInstance(JdbcTypeManager typeManager, JdbcColumnInfo columnInfo, MapperFactory factory) {
		return this;
	}
}