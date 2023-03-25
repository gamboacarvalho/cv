package orm.types.converter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import orm.mapper.MapperFactory;
import orm.metadata.JdbcColumnInfo;
import orm.types.AbstractJdbcType;
import orm.types.JdbcTypeManager;

public class DoubleType extends AbstractJdbcType<Double>{
	public Class<?>[] getHandledTypes(){
		return new Class<?>[]{Double.class, double.class};
	}
	public int bind(PreparedStatement stmt, int idx, Double arg) throws SQLException{
		stmt.setDouble(idx++, arg);
		return idx;
	}
	public Double convert(ResultSet rs, int idx) throws SQLException{
		return rs.getDouble(idx);
	}
	
	@Override
	public DoubleType getInstance(JdbcTypeManager typeManager, JdbcColumnInfo columnInfo, MapperFactory factory) {
		return this;
	}
}