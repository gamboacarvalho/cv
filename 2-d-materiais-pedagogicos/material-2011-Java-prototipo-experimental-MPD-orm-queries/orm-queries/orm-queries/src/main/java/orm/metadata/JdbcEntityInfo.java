package orm.metadata;

import orm.annotations.JdbcCol;
import orm.annotations.JdbcMapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import orm.Entity;
import orm.InvalidEntityException;

import app.helpers.Iterables;
import app.helpers.Predicate;




public class JdbcEntityInfo implements Iterable<JdbcColumnInfo>{

	
	private final List<JdbcColumnInfo> columns = new LinkedList<JdbcColumnInfo>();
	
	private String tableName;
	
	private Constructor<?> constructor = null;
		public <K, T extends Entity<K>> JdbcEntityInfo(Class<T> entity){
		
		//find constructor
		for(Constructor<?> c : entity.getConstructors()){
			JdbcMapper a;
			if((a=c.getAnnotation(JdbcMapper.class))!=null){
				constructor = c;
				tableName = a.table();
				break;
			}
		}
		if(constructor==null)
			throw new InvalidEntityException("Nao existe um contrutor anotado com \"JdbcMapper\"!");
		
		
		Annotation[][] parameterAnotations = constructor.getParameterAnnotations();
		Type [] parameterTypes = constructor.getGenericParameterTypes();
		
		//find parameters annotations and types
		for(int i =0; i<parameterTypes.length; i++){
			boolean hasAnnotation = false;
			for(Annotation a : parameterAnotations[i]){
				if(a instanceof JdbcCol){
					
					columns.add(new JdbcColumnInfo(entity, (JdbcCol)a, parameterTypes[i]));
					
					hasAnnotation=true;
					break;
				}
			}
			if(!hasAnnotation)
				throw new InvalidEntityException("Nem todas as colunas do construtor estao anotadas!");
		}
		
	}
	public Constructor<?> getConstructor() {
		return constructor;
	}
	
	public String getTableName() {
		return tableName;
	}
	
	public Iterable<JdbcColumnInfo> updatableColumns(){
		return Iterables.filter(columns, new Predicate<JdbcColumnInfo>() {
			@Override
			public boolean apply(JdbcColumnInfo object) {
				//TODO permitir gravar ValueHolder para bd
				return !object.getJdbcCol().isIdentity() && object.isColumn() && !object.isEntityValueHolder();
			}
		});
	}
	public Iterable<JdbcColumnInfo> columns(){
		return Iterables.filter(columns, new Predicate<JdbcColumnInfo>() {
			@Override
			public boolean apply(JdbcColumnInfo object) {
				return object.isColumn();
			}
		});
	}
	public Iterable<JdbcColumnInfo> pkColumns(){
		return Iterables.filter(columns, new Predicate<JdbcColumnInfo>() {
			@Override
			public boolean apply(JdbcColumnInfo object) {
				return object.getJdbcCol().isPk();
			}
		});
	}
	public Iterable<JdbcColumnInfo> identityColumns(){
		return Iterables.filter(columns, new Predicate<JdbcColumnInfo>() {
			@Override
			public boolean apply(JdbcColumnInfo object) {
				return object.getJdbcCol().isIdentity();
			}
		});
	}
	public int size(){
		return columns.size();
	}
	@Override
	public Iterator<JdbcColumnInfo> iterator() {
		return columns.iterator();
	}
}
