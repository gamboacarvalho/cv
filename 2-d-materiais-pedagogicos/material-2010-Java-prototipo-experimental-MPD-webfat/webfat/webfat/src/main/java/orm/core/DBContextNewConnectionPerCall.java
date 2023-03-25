package orm.core;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import orm.northwind.Product;

public class DBContextNewConnectionPerCall implements IDBContext {
	final String connStr;
	private Connection con = null;
	public DBContextNewConnectionPerCall(String connStr) {
	  super();
	  this.connStr = connStr;
  }
	public <T> T exec(ICmd<T> query, Object...params) throws SQLException{
	// Declare the JDBC objects.
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			// Establish the connection. 
			con = DriverManager.getConnection(connStr);
			// Create and execute an SQL statement that returns some data.
			stmt = con.prepareStatement(query.getSql());
			return query.exec(stmt, params);
		}
		finally {
			if (stmt != null) stmt.close();
			this.close();
		}
	}
	@Override
  public void close() throws SQLException{
		if (con != null){
			con.close();
			con = null;
		}
  }
}
