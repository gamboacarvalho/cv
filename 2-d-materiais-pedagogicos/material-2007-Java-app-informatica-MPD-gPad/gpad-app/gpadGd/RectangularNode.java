package gpadGd;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import gpad.model.INode;

import javax.swing.JLabel;

public class RectangularNode implements INode{
	public Object clone(){
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
	public Point2D getConnectionPoint(Point2D exterior) {
		Rectangle2D bounds = getBounds();
		double xMax = bounds.getMaxX(), yMax = bounds.getMaxY();
		double xMed = (_x + xMax)/2;
		double yMed = (_y + yMax)/2;
		
		double dx = exterior.getX() - bounds.getCenterX();
		double dy = exterior.getY() - bounds.getCenterY();
		double tan = dx != 0? dy / dx : (dy > 0 ? Double.MAX_VALUE: Double.MIN_VALUE);
		tan = Math.abs(tan);
		Point2D p = null;
		if( tan > 1 && dy > 0) p = new Point2D.Double(xMed, yMax);
		if( tan < 1 && dx < 0) p = new Point2D.Double(_x, yMed);
		if( tan < 1 && dx > 0) p = new Point2D.Double(xMax, yMed);
		if( tan > 1 && dy < 0) p = new Point2D.Double(xMed, _y);
		return p;
	}
	public void translate(double dx, double dy) {
		_x += dx; _y += dy;
	}
	public boolean contains(Point2D aPoint) {
		return getBounds().contains(aPoint);
	}
	public void draw(Graphics2D g2) {
		g2.draw(getBounds());
		_label.setBounds(getBounds());
		g2.translate(_x, _y);
		_label.paint(g2);
		g2.translate(-_x, -_y);
	}
	public Rectangle getBounds() {
		Dimension d = _label.getPreferredSize();
		return new Rectangle((int)_x, (int)_y, (int)d.getWidth(), (int)d.getHeight());
	}
	public void setMemento(ShapeMemento m) {}
	public ShapeMemento createMemento() {return null;}
	public String getText(){return _label.getText();}
	public void setText(String value){_label.setText(value);}
	public RectangularNode(){
		_label = new JLabel();
		_label.setHorizontalAlignment(JLabel.LEFT);
	}
	private double _x, _y;
	private JLabel _label;
}
