package orm.northwind.test;

import java.sql.SQLException;

import orm.core.DBContextSingleConnection;
import orm.core.ICmd;
import orm.core.IDBContext;

public class TestContext extends DBContextSingleConnection {

	
	public TestContext(String connStr) throws SQLException {
	  super(connStr);
	  con.setAutoCommit(false);
  }

	@Override
	public void close() throws SQLException {
		con.rollback();
		con.close();
	}

}
