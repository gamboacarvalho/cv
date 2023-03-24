package gpad.ui;

import gpad.model.CompositeShape;
import gpad.model.INode;
import gpad.model.INodeFactory;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

public class ToolNode extends ToolSelection{
	public ToolNode(CompositeShape s, INodeFactory f){
		super(s);
		_factory = f;
	}
	@Override
	public void handleMousePressed(MouseEvent e) {
		super.handleMousePressed(e);
		Point2D newLocation = e.getPoint();
		INode node = _factory.makeINode();
		double cX = newLocation.getX() - node.getBounds().getWidth()/2;
		double cY = newLocation.getY() - node.getBounds().getHeight()/2;
		node.translate(cX, cY);
		if(_shape.add(node)) _selected = node;
	}
	public void handleMouseReleased(MouseEvent e) {
	    /* empty implementation */	
	}
	public void handleMouseDragged(MouseEvent e) {
		/* empty implementation */
	}
	/**
	 * @uml.property   name=" "
	 * @uml.associationEnd   aggregation="shared" multiplicity="(1 1)" 
	 */
	private INodeFactory _factory;
}
