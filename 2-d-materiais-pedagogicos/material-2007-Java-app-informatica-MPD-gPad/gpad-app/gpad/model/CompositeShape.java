package gpad.model;


import gpad.model.IShape.ShapeMemento;

import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import java.util.*;
import java.util.List;

/**
   A graph consisting of selectable nodes and edges.
 */
public class CompositeShape implements IShape
{
	/**
      Constructs a graph with no nodes or edges.
	 */
	public CompositeShape()
	{
		_shapes = new ArrayList<IShape>();
	}

	/**
      Adds a node to the graph so that the top left corner of
      the bounding rectangle is at the given point.
      @param n the node to add
      @param p the desired location
	 */
	public boolean add(IShape s){
		Point2D p = new Point2D.Double(
				s.getBounds().getCenterX(), 
				s.getBounds().getCenterY());
		if(findShape(p) == null){
			_shapes.add(s);
			return true;
		}
		else return false;
	}

	/**
      Finds a node containing the given point.
      @param p a point
      @return a node containing p or null if no nodes contain p
	 */
	public IShape findShape(Point2D p){
		for (IShape shape : _shapes) {
			if (shape.contains(p)) return shape;
		}
		return null;
	}
	/**
      Removes a node and all edges that start or end with that node
      @param n the node to remove
	 */
	public void removeShape(IShape sh){
		_shapes.remove(sh);
		if(sh instanceof INode){
			for (int i = _shapes.size() - 1; i >= 0; i--){
				IShape s = _shapes.get(i);
				if(s instanceof ILink){
					ILink e = (ILink) s;
					if (e.getStart() == sh || e.getEnd() == sh)
						_shapes.remove(i);
				}
			}
		}
	}

	/**
	 * Draws the shape
	 * @param g2 the graphics context
	 */
	public void draw(Graphics2D g2)
	{
		for (IShape n : _shapes)
			n.draw(g2);
	}
	/**
	 * Gets the smallest rectangle enclosing the shape
	 * @param g2 the graphics context
	 * @return the bounding rectangle
	 */
	public Rectangle2D getBounds()
	{
		Rectangle2D r = null;
		for (IShape n : _shapes){
			Rectangle2D b = n.getBounds();
			if (r == null) r = b;
			else r.add(b);
		}
		return r == null ? new Rectangle2D.Float() : r;
	}
	/**
	 * Tests whether the shape contains a point.
	 * @param aPoint the point to test
	 * @return true if this node contains aPoint
	 */
	public boolean contains(Point2D aPoint) {
		return getBounds().contains(aPoint);
	}
	public Object clone(){
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
	public ShapeMemento createMemento(){
		ShapeMemento mem = new ShapeMemento();
		mem._shapes = (ArrayList<IShape>) _shapes.clone();
		return mem;
	}
	public void setMemento(ShapeMemento mem){
		_shapes = mem._shapes;
	}
	   
	/** 
	 * @uml.property name="  "
	 * @uml.associationEnd multiplicity="(0 -1)" ordering="true" aggregation="shared"
	 */
	private ArrayList<IShape> _shapes;
}





