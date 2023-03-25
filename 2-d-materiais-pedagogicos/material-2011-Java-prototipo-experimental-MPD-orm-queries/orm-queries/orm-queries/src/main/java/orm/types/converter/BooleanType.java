package orm.types.converter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import orm.mapper.MapperFactory;
import orm.metadata.JdbcColumnInfo;
import orm.types.*;

public class BooleanType extends AbstractJdbcType<Boolean>{
	public Class<?>[] getHandledTypes(){
		return new Class<?>[]{Boolean.class, boolean.class};
	}
	public int bind(PreparedStatement stmt, int idx, Boolean arg) throws SQLException{
		stmt.setBoolean(idx++, arg);
		return idx;
	}
	public Boolean convert(ResultSet rs, int idx) throws SQLException{
		return rs.getBoolean(idx);
	}
	@Override
	public BooleanType getInstance(JdbcTypeManager typeManager, JdbcColumnInfo columnInfo, MapperFactory factory){	
		return this;
	}
	
	


}