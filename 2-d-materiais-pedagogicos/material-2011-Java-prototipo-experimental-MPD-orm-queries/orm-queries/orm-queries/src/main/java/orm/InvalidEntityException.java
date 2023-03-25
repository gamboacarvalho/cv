package orm;

public class InvalidEntityException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6574197196105840061L;
	public InvalidEntityException(String msg, Exception e){
		super(msg, e);
	}
	public InvalidEntityException(Exception e){
		super(e);
	}
	public InvalidEntityException(String e){
		super(e);
	}
}
