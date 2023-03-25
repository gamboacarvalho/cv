package selfdao.jdbc;

public interface JdbcIterableQuery<T> extends Iterable<T>{
	
	Iterable<T> orderBy(String col);
	
	JdbcIterableQuery<T> where(String clause);
}
