package naivecontainer.exceptions;

@SuppressWarnings("serial")
public class UnexpectedException extends RuntimeException {

	public UnexpectedException(Exception e) {
		super(e);
	}
}
