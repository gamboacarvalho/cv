package orm.types;
import orm.Entity;

public abstract class AbstractJdbcType<T> implements JdbcType<T>, JdbcTypeFactory<JdbcType<T>> {

	@Override
	public boolean requireEntityInstance() {
		return false;
	}
	
	@Override
	public void setEntity(Entity<?> entity){
		throw new UnsupportedOperationException("Illegal setEntity invocation");
	}


}
