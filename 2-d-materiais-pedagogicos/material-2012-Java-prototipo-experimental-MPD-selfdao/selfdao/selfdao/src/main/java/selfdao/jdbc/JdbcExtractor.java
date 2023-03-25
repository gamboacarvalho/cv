package selfdao.jdbc;

import java.lang.reflect.Constructor;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import selfdao.KeyGetter;

public abstract class JdbcExtractor<T>{

	public abstract T extract(ResultSet rs, int idx) throws SQLException;
	
	public static final JdbcExtractor<Integer> ExtractInt = new JdbcExtractor<Integer>() {
		@Override
		public Integer extract(ResultSet rs, int idx) throws SQLException {
			return rs.getInt(idx);
		}
	};
	
	public static final JdbcExtractor<Double> ExtractDouble = new JdbcExtractor<Double>() {
		@Override
		public Double extract(ResultSet rs, int idx) throws SQLException {
			return rs.getDouble(idx);
		}
	};
	
	public static final JdbcExtractor<String> ExtractString = new JdbcExtractor<String>() {
		@Override
		public String extract(ResultSet rs, int idx) throws SQLException {
			return rs.getString(idx);
		}
	};

	public static final JdbcExtractor<Date> ExtractDate = new JdbcExtractor<Date>() {
		@Override
		public Date extract(ResultSet rs, int idx) throws SQLException {
			return rs.getDate(idx);
		}
	};
	
	private static final Map<Class<?>, JdbcExtractor<?>> extractors;
	
	static{
		extractors = new HashMap<Class<?>, JdbcExtractor<?>>();
		extractors.put(Integer.class, ExtractInt);
		extractors.put(int.class, ExtractInt);
		extractors.put(Double.class, ExtractDouble);
		extractors.put(double.class, ExtractDouble);
		extractors.put(String.class, ExtractString);
		extractors.put(Date.class, ExtractDate);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> JdbcExtractor<T> get(Class<T> klass){
		JdbcExtractor<T> ext = (JdbcExtractor<T>) extractors.get(klass);
		if(ext == null) throw new UnsupportedOperationException("No extractor for type " + klass);
		return ext;
	}
	
	public static <T> JdbcConverter<T> build(final Constructor<T> ctor, final KeyGetter key, final Map<Object, Object> identityMap) {
		Class<?> [] ctorArgTyes = ctor.getParameterTypes();
		final JdbcExtractor<?>[] res = new JdbcExtractor<?>[ctorArgTyes.length];
		for (int i = 0; i < res.length; i++) {
			res[i] = JdbcExtractor.get(ctorArgTyes[i]);
		}
		return new JdbcConverter<T>() {
			@Override
			public T convert(ResultSet rs) throws SQLException {
				Object[] args = new Object[res.length];
				for (int i = 0; i < res.length; i++) {
					args[i] = res[i].extract(rs, i + 1);
				}
				try {
					T val = ctor.newInstance(args);
					T cache = (T) identityMap.get(key.get(val));
					if(cache != null)
						return cache;
					identityMap.put(key.get(val), val);
					return val;
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					throw new RuntimeException(e);
				}
			}
		};
	}

}
