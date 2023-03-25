package webfast.routing;

/**
 * Represents part of a non case-sensitive path.
 * @author mcarvalho
 */
public class  PathPartConstant implements PathPart{
	final String part;
	
	public PathPartConstant(String part) {
	  this.part = part.toLowerCase();
  }

	public boolean match(String part){
		if(part == null) return false;
		return this.part.equals(part.toLowerCase());
	}
}
