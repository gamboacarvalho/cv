package orm.types.converter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import orm.mapper.MapperFactory;
import orm.metadata.JdbcColumnInfo;
import orm.types.AbstractJdbcType;
import orm.types.JdbcTypeManager;


public class IntegerType extends AbstractJdbcType<Integer>{
	public Class<?>[] getHandledTypes(){
		return new Class<?>[]{Integer.class, int.class};
	}
	public int bind(PreparedStatement stmt, int idx, Integer arg) throws SQLException{
		stmt.setInt(idx++, arg);
		return idx;
	}
	public Integer convert(ResultSet rs, int idx) throws SQLException{
		return rs.getInt(idx);
	}
	@Override
	public IntegerType getInstance(JdbcTypeManager typeManager,
			JdbcColumnInfo columnInfo, MapperFactory factory) {
		return this;
	}
}