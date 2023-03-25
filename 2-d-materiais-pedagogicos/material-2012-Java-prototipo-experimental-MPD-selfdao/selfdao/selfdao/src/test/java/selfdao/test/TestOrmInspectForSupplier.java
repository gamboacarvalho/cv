package selfdao.test;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import junit.framework.Assert;

import org.junit.Test;

import selfdao.test.model.Supplier;

public class TestOrmInspectForSupplier {
	
	private SupplierDao dao;
	
	@Test
	public void test_load_all_for_suppliers() throws SQLException{
		int size = 0;
		for (Supplier emp : dao.getAll()) {
			size++;
		}
		Assert.assertEquals(29, size);
	}
	
	private static DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	
	@Test
	public void test_load_by_id_for_suppliers() throws SQLException, ParseException{
		Supplier e = dao.getById(5);
		Assert.assertEquals("Cooperativa de Quesos 'Las Cabras'", e.getCompanyName());
		Assert.assertEquals("Calle del Rosal 4", e.getAddress());
		Assert.assertEquals("Oviedo", e.getCity());
	}	
}
