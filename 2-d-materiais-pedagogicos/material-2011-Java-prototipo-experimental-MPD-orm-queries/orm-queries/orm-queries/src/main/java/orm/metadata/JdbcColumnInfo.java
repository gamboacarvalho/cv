package orm.metadata;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import orm.Entity;
import orm.InvalidEntityException;
import orm.annotations.JdbcCol;
import orm.types.ValueHolder;


/**
* Associa uma JdbcCol com o return type 
**/
public class JdbcColumnInfo {
	final JdbcCol col;
	final Type retType;
	final Class<?> parent;
	
	<T extends Entity<?>> JdbcColumnInfo(Class<T> parent, JdbcCol col, Type returnType){
		this.col = col;
		this.retType = returnType;
		this.parent = parent;
	}
	
	public JdbcCol getJdbcCol(){
		return col;
	}
	/**
	 * Column type
	 * 
	 * @return
	 */
	public Class<?> getType(){
		if(retType instanceof ParameterizedType){
			return (Class<?>) ((ParameterizedType)retType).getRawType();
		}
		return (Class<?>) retType;
	}
	/**
	 * Column type
	 * 
	 * @return
	 */
	public Class<?> getGenericType(){
		if(retType instanceof ParameterizedType){
			return (Class<?>) ((ParameterizedType) retType).getActualTypeArguments()[0];
		}else{
			return null;
		}
	}
	
	public boolean isEntityValueHolder(){
		return retType instanceof ParameterizedType 
				&& ((ParameterizedType)retType).getRawType() == ValueHolder.class
				&& Entity.class.isAssignableFrom(getGenericType());
	}
	
	public boolean isIterable(){
		return retType instanceof ParameterizedType && ((ParameterizedType)retType).getRawType() == Iterable.class;
	}
	
	public boolean isColumn() {
		return !isIterable();
	}

	public String toString(){
		return col.value();
	}
	
	public Method getGetter(){
		String colName = col.isPk() ? "Id" : col.value();
		try {
			return parent.getMethod("get"+colName);
		} catch (SecurityException | NoSuchMethodException e) {
			throw new InvalidEntityException("O metodo "+"get"+colName+" nao estao corretamente definido", e);
		}
		
	}
	
}
