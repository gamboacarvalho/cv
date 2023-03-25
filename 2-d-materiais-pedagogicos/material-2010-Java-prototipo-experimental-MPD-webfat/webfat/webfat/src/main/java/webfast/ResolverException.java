package webfast;

public class ResolverException extends Exception {
	final int httCode;
	final String message;
	public ResolverException(int httCodE, String message) {
	  super();
	  this.httCode = httCodE;
	  this.message = message;
  }
	
}
