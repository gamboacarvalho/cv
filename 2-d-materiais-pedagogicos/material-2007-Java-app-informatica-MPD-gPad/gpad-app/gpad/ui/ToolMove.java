package gpad.ui;

import gpad.model.CompositeShape;
import gpad.model.INode;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

public class ToolMove extends ToolSelection{
	public ToolMove(CompositeShape s){
		super(s);
	}
	@Override
	public void handleMousePressed(MouseEvent e) {
		super.handleMousePressed(e);
		_lastMousePoint = e.getPoint();
	}
	public void handleMouseReleased(MouseEvent e) {
		_lastMousePoint = null;		
	}
	public void handleMouseDragged(MouseEvent e) {
		Point2D newLocation = e.getPoint();
		if(_selected instanceof INode){
			INode node = (INode) _selected;
			double dx = newLocation.getX() - _lastMousePoint.getX();
			double dy = newLocation.getY() - _lastMousePoint.getY();
			node.translate(dx,dy);
		}
		_lastMousePoint = newLocation;
	}
	protected Point2D _lastMousePoint;
}
