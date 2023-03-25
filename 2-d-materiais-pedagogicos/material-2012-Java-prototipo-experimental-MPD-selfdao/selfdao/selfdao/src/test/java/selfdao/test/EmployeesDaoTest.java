package selfdao.test;

import java.sql.SQLException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import selfdao.jdbc.JdbcExecutorSingleConnection;
import selfdao.test.model.Employee;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

public class EmployeesDaoTest {

	private EmployeeDao dao;

	@Before
	public void setUp(){
		SQLServerDataSource ds = new SQLServerDataSource();
		ds.setUser("myAppUser");
		ds.setPassword("fcp");
		JdbcExecutorSingleConnection exec = new JdbcExecutorSingleConnection(ds, false);
		dao = selfdao.Builder.with(exec).of(EmployeeDao.class);
	}

	@After
	public void tearDown() throws Exception{
		if(dao != null) dao.close();
	}


	@Test
	public void test_update() throws Exception{
		Employee e = dao.getById(7);
		Assert.assertEquals("King", e.getLastName());
		Assert.assertEquals("Robert", e.getFirstName());
		Assert.assertEquals("Sales Representative", e.getTitle());
		//
		// Update
		//
		e.setFirstName("Jose");
		e.setLastName("Manel");
		e.setTitle("Engenheiro");
		dao.update(e.getTitle(), e.getFirstName(), e.getLastName(), e.getBirthDate(), e.getId());
		//
		// Assert
		//
		e = dao.getById(7);
		Assert.assertEquals("Jose", e.getFirstName());
		Assert.assertEquals("Manel", e.getLastName());
		Assert.assertEquals("Engenheiro", e.getTitle());
	}

	@Test
	public void test_load_all_employees() throws SQLException{
		Iterable<Employee> res = dao.getAll();
		int size = 0;
		for(Employee e:res){size++;}
		Assert.assertEquals(9, size);
	}
	@Test
	public void test_load_byid_employees() throws SQLException{
		Employee e = dao.getById(7);
		Assert.assertEquals("King", e.getLastName());
		Assert.assertEquals("Robert", e.getFirstName());
		Assert.assertEquals("Sales Representative", e.getTitle());
	}
}
