package gpad.ui;

import gpad.model.CompositeShape;
import gpad.model.ILink;
import gpad.model.INode;
import gpad.model.IShape;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public abstract class ToolSelection implements Tool{

	public ToolSelection(CompositeShape s ){
		_shape = s;
	}
	public IShape getSelected() {
		return _selected;
	}
	public void setSelected(IShape shape) {
		_selected = shape;
	}

	public void removeSelected() {
		_shape.removeShape(_selected);
		_selected = null;
		
	}
	public IShape getShape() {
		return _shape;
	}
	public void setShape(CompositeShape shape){
		_shape = shape;
	}
	public 	IShape.ShapeMemento createMemento(){
		return _shape.createMemento();
	}
	public void setMemento(IShape.ShapeMemento m){
		_shape.setMemento(m);
	}

	public void draw(Graphics2D g2) {
		_shape.draw(g2);
		if (_selected instanceof INode)
		{
			Rectangle2D grabberBounds = ((INode) _selected).getBounds();
			drawGrabber(g2, grabberBounds.getMinX(), grabberBounds.getMinY());
			drawGrabber(g2, grabberBounds.getMinX(), grabberBounds.getMaxY());
			drawGrabber(g2, grabberBounds.getMaxX(), grabberBounds.getMinY());
			drawGrabber(g2, grabberBounds.getMaxX(), grabberBounds.getMaxY());
		}
		if (_selected instanceof ILink)
		{
			Line2D line = ((ILink) _selected).getLine();
			drawGrabber(g2, line.getX1(), line.getY1());
			drawGrabber(g2, line.getX2(), line.getY2());
		}
	}
	public void handleMousePressed(MouseEvent e){
		Point2D mousePoint = e.getPoint();
		_selected = _shape.findShape(mousePoint);
	}
	/**
    Draws a single "grabber", a filled square
    @param g2 the graphics context
    @param x the x coordinate of the center of the grabber
    @param y the y coordinate of the center of the grabber
	 */
	public static void drawGrabber(Graphics2D g2, double x, double y){
		final int SIZE = 5;
		Color oldColor = g2.getColor();
		g2.setColor(PURPLE);
		g2.fill(new Rectangle2D.Double(x - SIZE / 2,
				y - SIZE / 2, SIZE, SIZE));
		g2.setColor(oldColor);
	}
	static final Color PURPLE = Color.MAGENTA;
	/**
	 * @uml.property   name="_shape"
	 * @uml.associationEnd   multiplicity="(1 1)" aggregation="shared" inverse="padPanel:gpad.IShape"
	 */
	protected IShape _selected;
	protected CompositeShape _shape;
}
