package gpad.ui;

import gpad.model.CompositeShape;
import gpad.model.IShape;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

/**
 * @uml.dependency   supplier="gpad.IShape"
 */
public interface Tool {
	/**
	 * A specific application could made some operations on 
	 * selected shape like edit its properties. 
	 * @return selected shape
	 */
	IShape getSelected();
	void setSelected(IShape shape);
	/**
	 * Gives to applications the oportunity to remove
	 * the selection. 
	 */
	void removeSelected();
	/**
	 * @return shape.
	 */
	IShape getShape();
	/**
	 * @param shape A new shape to associate.
	 */
	void setShape(CompositeShape shape);
	IShape.ShapeMemento createMemento();
	void setMemento(IShape.ShapeMemento m);
	/**
	 * Draw some aditional artifact on graphics context
	 * @param g2 graphical context
	 */
	void draw(Graphics2D g2);
	void handleMousePressed(MouseEvent e);
	void handleMouseReleased(MouseEvent e);
	void handleMouseDragged(MouseEvent e);
}
