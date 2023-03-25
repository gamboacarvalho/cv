package selfdao.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class JdbcBinder<T> {
	
	abstract void bind(PreparedStatement stmt, int idx, T arg) throws SQLException;

	public static JdbcBinder<?> of(Class<?> klass){
		JdbcBinder<?> b = binders.get(klass);
		if(b == null) throw new UnsupportedOperationException("No binder for type " + klass);
		return b;
	}
	
	public static final JdbcBinder<Integer> BindInt = new JdbcBinder<Integer>() {
		public void bind(PreparedStatement stmt, int idx, Integer arg) throws SQLException {
			stmt.setInt(idx, arg);
		}
	};
	
	public static final JdbcBinder<Double> BindDouble = new JdbcBinder<Double>() {
		public void bind(PreparedStatement stmt, int idx, Double arg) throws SQLException {
			stmt.setDouble(idx, arg);
		}
	};
	
	public static final JdbcBinder<String> BindString= new JdbcBinder<String>() {
		public void bind(PreparedStatement stmt, int idx, String arg) throws SQLException {
			stmt.setString(idx, arg);
		}
	}; 
	public static final JdbcBinder<Date> BindDate= new JdbcBinder<Date>() {
		public void bind(PreparedStatement stmt, int idx, Date arg) throws SQLException {
			stmt.setDate(idx, new java.sql.Date(arg.getTime()));
		}
	};
	
	private static final Map<Class<?>, JdbcBinder<?>> binders;
	
	static{
		binders = new HashMap<Class<?>, JdbcBinder<?>>();
		binders.put(Integer.class, BindInt);
		binders.put(int.class, BindInt);
		binders.put(Double.class, BindDouble);
		binders.put(double.class, BindDouble);
		binders.put(String.class, BindString);
		binders.put(Date.class, BindDate);
	}

}
