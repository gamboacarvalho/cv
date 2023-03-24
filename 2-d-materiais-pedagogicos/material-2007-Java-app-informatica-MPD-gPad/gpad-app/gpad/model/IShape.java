package gpad.model;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public interface IShape extends Serializable, Cloneable{
	/**
     * Draw shape.
     * @param g2 the graphics context
	 */
	void draw(Graphics2D g2);
	/**
	 * Gets the smallest rectangle that bounds this edge.
     * The bounding rectangle contains all labels.
	 * @return the bounding rectangle
	 */
	Rectangle2D getBounds();
	/**
	 * Tests whether the shape contains a point.
	 * @param aPoint the point to test
	 * @return true if this node contains aPoint
	 */
	boolean contains(Point2D aPoint);
	
	Object clone();
	
	ShapeMemento createMemento();
	void setMemento(ShapeMemento m);
	static class ShapeMemento{
		protected ArrayList<IShape> _shapes;
	}
}
