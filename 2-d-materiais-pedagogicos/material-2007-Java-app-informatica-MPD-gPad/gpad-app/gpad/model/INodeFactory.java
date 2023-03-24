package gpad.model;

/**
 * @uml.dependency   supplier="gpad.IShape" stereotypes="Standard::Create"
 */
public interface INodeFactory{
	/**
	 * Create a new instance of an INode subclass. 
	 * @return An IShape object.
	 */	
	INode makeINode();
}
