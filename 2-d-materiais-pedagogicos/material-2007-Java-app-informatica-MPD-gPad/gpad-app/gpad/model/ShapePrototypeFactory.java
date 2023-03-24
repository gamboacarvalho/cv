package gpad.model;


public class ShapePrototypeFactory implements INodeFactory, ILinkFactory{
	
	public void changeShape(IShape shape){
		_prototype = shape;
	}
	/**
    Gets the node prototype associated with
    the currently selected button
    @return a Node or Edge prototype
	 */
	public INode makeINode(){
		if(_prototype instanceof INode) 
			return (INode) _prototype.clone();
		else 
			return null;
	}
	/**
  Gets the link prototype associated with
  the currently selected button
  @return a Node or Edge prototype
	 */
	public ILink makeILink() {
		if(_prototype instanceof ILink) 
			return (ILink) _prototype.clone();
		else 
			return null;
	}

	private IShape _prototype;
}
