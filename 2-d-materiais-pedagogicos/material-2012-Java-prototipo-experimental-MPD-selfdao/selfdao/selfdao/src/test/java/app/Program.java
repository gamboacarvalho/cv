package app;

import java.text.DecimalFormat;

import selfdao.jdbc.JdbcExecutorSingleConnection;
import selfdao.test.ProductDao;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

public class Program {

	public static void main(String[] args) {
		SQLServerDataSource ds = new SQLServerDataSource();
		ds.setUser("myAppUser");
		ds.setPassword("fcp");
		JdbcExecutorSingleConnection exec = new JdbcExecutorSingleConnection(ds, false);
		ProductDao dao = selfdao.Builder.with(exec).of(ProductDao.class);
		
		long begin = System.nanoTime();
		dao.getById(9);
		printElapsedNanos(begin, System.nanoTime());
		
		begin = System.nanoTime();
		double total = 0;
		for (int i = 3; i < 72; i++) {
			dao = selfdao.Builder.with(exec).of(ProductDao.class);
			total += dao.getById(i).getUnitPrice();
		}
		System.out.println("TOTAL = " + total);
		printElapsedNanos(begin, System.nanoTime());
		
		begin = System.nanoTime();
		total = 0;
		for (int i = 1; i < 70; i++) {
			total += dao.getById(i).getUnitPrice();
		}
		System.out.println("TOTAL = " + total);
		printElapsedNanos(begin, System.nanoTime());
		
	}
	
	private static final DecimalFormat format = new DecimalFormat("###,###.###");
	
	private static void printElapsedNanos(long start, long end){
		double timeInMilis = (end-start)/1000000.0;
		System.out.println("Elapsed: " + format.format(timeInMilis) + "ms");
	} 

}
