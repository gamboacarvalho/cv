package webfast.routing;

public interface PathPart {
	boolean match(String part) throws Exception;
}
