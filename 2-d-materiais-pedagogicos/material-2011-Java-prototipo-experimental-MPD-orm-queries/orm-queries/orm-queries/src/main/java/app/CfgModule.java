package app;

import javax.sql.DataSource;

import orm.DbPasswd;
import orm.DbUser;
import orm.executors.JdbcExecutor;
import orm.executors.JdbcExecutorMultipleConnection;

import com.google.inject.AbstractModule;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

public class CfgModule extends AbstractModule{

	@Override
	protected void configure() {
		SQLServerDataSource ds = new SQLServerDataSource();
		// ds.setPortNumber(1434);
		
		bind(JdbcExecutor.class).to(JdbcExecutorMultipleConnection.class);
		
		bind(DataSource.class).toInstance(ds);
		 
		bind(String.class).annotatedWith(DbUser.class).toInstance("myAppUser");
		bind(String.class).annotatedWith(DbPasswd.class).toInstance("fcp");
	}

}
