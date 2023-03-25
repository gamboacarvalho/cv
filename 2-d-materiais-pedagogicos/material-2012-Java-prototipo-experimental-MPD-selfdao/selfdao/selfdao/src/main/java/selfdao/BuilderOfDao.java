package selfdao;

public interface BuilderOfDao {

	<T> T of(Class<? extends T> src);
	
}
