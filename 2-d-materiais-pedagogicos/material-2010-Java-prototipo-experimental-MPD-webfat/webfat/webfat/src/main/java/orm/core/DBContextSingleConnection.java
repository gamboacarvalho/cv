package orm.core;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import orm.northwind.Product;

public class DBContextSingleConnection implements IDBContext {
	protected final Connection con;
	public DBContextSingleConnection(String connStr) throws SQLException {
	  super();
		// Establish the connection. 
	  con = DriverManager.getConnection(connStr);
	  con.setAutoCommit(false);
  }
	public <T> T exec(ICmd<T> query, Object...params)throws SQLException{
		// Declare the JDBC objects.
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			// Create and execute an SQL statement that returns some data.
			stmt = con.prepareStatement(query.getSql(), PreparedStatement.RETURN_GENERATED_KEYS);
			return query.exec(stmt,params);
		}
		finally {
			if (stmt != null) stmt.close();
		}
	}
	@Override
  public void close() throws SQLException{
		if (con != null){ 
			con.rollback();
			con.close();
		}
  }
}
