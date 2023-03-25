package orm.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import com.google.inject.Inject;

import orm.Entity;
import orm.JdbcBinder;
import orm.JdbcConverter;
import orm.JdbcSqlGenerator;
import orm.TemplateDataMapper;
import orm.command.JdbcCmd;
import orm.command.JdbcCmdQuery;
import orm.command.JdbcCmdQueryTemplate;
import orm.command.JdbcCmdTemplate;
import orm.executors.JdbcExecutor;
import orm.metadata.JdbcColumnInfo;
import orm.metadata.JdbcEntityInfo;
import orm.types.JdbcType;
import orm.types.JdbcTypeManager;

public class JdbcGenericDataMapper<K, T extends Entity<K>> extends TemplateDataMapper<K, T>{
	
	private final JdbcEntityInfo ei;
	private final JdbcTypeManager typeManager;
	private final MapperFactory mapper;
	private final JdbcSqlGenerator sqlGen;

	@Inject
	public JdbcGenericDataMapper(JdbcExecutor db, Class<T> cls, MapperFactory mapper, JdbcTypeManager typeManager) {
		super(db);
		this.mapper=mapper;
		this.typeManager=typeManager;
		ei = new JdbcEntityInfo(cls);
		sqlGen = new JdbcSqlGenerator(ei);
	}

	@Override
	protected JdbcCmdQuery<Iterable<T>> cmdLoadAll() {
		return new JdbcCmdQueryTemplate<Iterable<T>>(sqlGen.selectSql(), iterableConverter);
	}

	@Override
	protected JdbcCmdQuery<Iterable<T>> cmdLoadById() {
		return new JdbcCmdQueryTemplate<Iterable<T>>(sqlGen.selectByIdSql(),iterableConverter,pkBinderById);
	}

	@Override
	protected JdbcCmd<T> cmdUpdate() {
		return new JdbcCmdTemplate<T>( sqlGen.updateSql(), updatableBinder, pkBinder);
	}

	@Override
	protected JdbcCmdQuery<K> cmdInsert() {
		return new JdbcCmdQueryTemplate<K>( sqlGen.insertSql(), pkConverter, updatableBinder);
	}

	@Override
	protected JdbcCmd<T> cmdDelete() {
		return new JdbcCmdTemplate<T>( sqlGen.deleteSql(), pkBinder);
	}
	
	/**
	 * Converters and Binders
	 */
	
	private final JdbcConverter<T> entityConverter = new JdbcConverter<T>(){
		@Override
		public T convert(ResultSet rs) throws SQLException {		
			Object[] arguments = new Object[ei.size()];
			List<JdbcType<?>> needEntity=new LinkedList<JdbcType<?>> ();
			int idx=0, idx1=0;
			
			for (final JdbcColumnInfo column : ei){	
					 JdbcType<?> converter=typeManager.get(column.getType(), column, mapper);
					 if (converter.requireEntityInstance()) 
						 needEntity.add(converter);
					 else{
						 //so obter o campo do ResultSet no caso de nao consultar a informacao na Entity
						 idx1++;;
					 }
					 arguments[idx++] = converter.convert(rs, idx1);
					 
			}
			
			try {
				@SuppressWarnings("unchecked")
				T o = (T) ei.getConstructor().newInstance(arguments);
				for (JdbcType<?> c : needEntity)
					c.setEntity((Entity<?>) o);
				
				return o;
			} catch (InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException e) {
				throw new IllegalStateException("Erro ao instanciar novo objeto.",e);
			}
		}	
	};
	
	private final JdbcConverter<Iterable<T>> iterableConverter= new JdbcConverter<Iterable<T>>(){

		@Override
		public Iterable<T> convert(ResultSet rs) throws SQLException {
			LinkedList<T> list = new LinkedList<T>();
			while(rs.next())
				list.add(entityConverter.convert(rs));
			
			return list;
		}	
	};
	
	private final JdbcConverter<K> pkConverter = new JdbcConverter<K>(){
		
		@SuppressWarnings("unchecked")
		@Override
		public K convert(ResultSet rs) throws SQLException {
			for (JdbcColumnInfo column : ei.identityColumns())
				return (K) typeManager.get(column.getType(), column, mapper).convert(rs, 1);
			
			return null;
		}
		
	};
	
	private final JdbcBinder<T> pkBinder= new JdbcBinder<T>(){
		@SuppressWarnings("unchecked")
		@Override
		public int bind(PreparedStatement stmt, int idx, T arg) throws SQLException {
			for (JdbcColumnInfo colInfo : ei.pkColumns()){
				Class<?> getterRetType = colInfo.getGetter().getReturnType();	
				
				if(!typeManager.containsType(getterRetType)){
					try {
						final Field field = getterRetType.getField(colInfo.toString());
						Object o = field.get(arg.getId());
						idx=typeManager.get(o.getClass(),colInfo,mapper).bind(stmt, idx, o);
					} catch (SecurityException | NoSuchFieldException | IllegalArgumentException
							| IllegalAccessException e) {
						throw new RuntimeException("Chave primaria mal configurada: " + colInfo.toString());
					}
				}else{
					try {
						idx=typeManager.get(getterRetType,colInfo,mapper).bind(stmt, idx, colInfo.getGetter().invoke(arg));
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			}
			
			return idx;
		}	
	};
	
	private final JdbcBinder<T> updatableBinder= new JdbcBinder<T>(){
			@SuppressWarnings("unchecked")
			@Override
			public int bind(PreparedStatement stmt, int idx, T arg) throws SQLException {
				try {
					for (JdbcColumnInfo columnInfo: ei.updatableColumns())
						idx=typeManager
								.get(columnInfo.getType(),columnInfo, mapper)
								.bind(stmt, idx, columnInfo.getGetter().invoke(arg));
							
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				return idx;
			}
	};
	
	private final JdbcBinder<K> pkBinderById = new JdbcBinder<K>(){

		@SuppressWarnings("unchecked")
		@Override
		public int bind(PreparedStatement stmt, int idx, K arg) throws SQLException {
			for (JdbcColumnInfo colInfo : ei.pkColumns()){
				Class<?> getterRetType = colInfo.getGetter().getReturnType();	
				
				if(!typeManager.containsType(getterRetType)){
					try {
						final Field field = getterRetType.getField(colInfo.toString());
						Object o = field.get(arg);
						idx=typeManager.get(o.getClass(),colInfo,mapper).bind(stmt, idx, o);
					} catch (SecurityException | NoSuchFieldException | IllegalArgumentException
							| IllegalAccessException e) {
						throw new RuntimeException("Chave primaria mal configurada: " + colInfo.toString());
					}
				}else{
					try {
						idx=typeManager.get(getterRetType,colInfo,mapper).bind(stmt, idx, arg);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			}
			
			return idx;
		}
		
	};
}
