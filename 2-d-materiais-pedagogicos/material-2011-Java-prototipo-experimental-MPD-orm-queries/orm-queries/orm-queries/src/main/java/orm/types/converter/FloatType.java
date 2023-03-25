package orm.types.converter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import orm.mapper.MapperFactory;
import orm.metadata.JdbcColumnInfo;
import orm.types.AbstractJdbcType;
import orm.types.JdbcTypeManager;

public class FloatType extends AbstractJdbcType<Float> {
	public Class<?>[] getHandledTypes(){
		return new Class<?>[]{Float.class, float.class};
	}
	public int bind(PreparedStatement stmt, int idx, Float arg) throws SQLException{
		stmt.setFloat(idx++, arg);
		return idx;
	}
	public Float convert(ResultSet rs, int idx) throws SQLException{
		return rs.getFloat(idx);
	}
	@Override
	public FloatType getInstance(JdbcTypeManager typeManager, JdbcColumnInfo columnInfo, MapperFactory factory) {
		return this;
	}

}