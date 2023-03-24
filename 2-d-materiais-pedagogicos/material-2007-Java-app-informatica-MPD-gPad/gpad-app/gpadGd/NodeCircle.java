package gpadGd;

import gpad.model.INode;
import gpad.model.IShape.ShapeMemento;

import java.awt.*;
import java.awt.geom.*;

/**
   A circular node that is filled with a color.
*/
public class NodeCircle implements INode{
   /**
      Construct a circle node with a given size and color.
      @param aColor the fill color
   */
   public NodeCircle(Color aColor)
   {
      _size = DEFAULT_SIZE;
      _centerX = 0;
      _centerY = 0;
      _color = aColor;
   }
   public void draw(Graphics2D g2)
   {
      Color oldColor = g2.getColor();
      g2.setColor(_color);
      g2.fillOval(_centerX, _centerY, _size, _size);
      g2.setColor(oldColor);
      g2.drawOval(_centerX, _centerY, _size, _size);
   }

   public void translate(double dx, double dy)
   {
      _centerX += dx;
      _centerY += dy;
   }

   public boolean contains(Point2D p)
   {
      Ellipse2D circle = new Ellipse2D.Double(
            _centerX, _centerY, _size, _size);
      return circle.contains(p);
   }

   public Rectangle2D getBounds()
   {
      return new Rectangle2D.Double(
            _centerX, _centerY, _size, _size);
   }

   public Point2D getConnectionPoint(Point2D other)
   {
      double centerX = _centerX + _size / 2;
      double centerY = _centerY + _size / 2;
      double dx = other.getX() - centerX;
      double dy = other.getY() - centerY;
      double distance = Math.sqrt(dx * dx + dy * dy);
      if (distance == 0) return other;
      else return new Point2D.Double(
            centerX + dx * (_size / 2) / distance,
            centerY + dy * (_size / 2) / distance);
   }
   public Color getColor(){
	   return _color;   
   }
   public void setColor(Color aColor)
   {
      _color = aColor;
   }

   public Object clone(){
	   try {
		return super.clone();
	} catch (CloneNotSupportedException e) {
		e.printStackTrace();
		return null;
	}
   }
   public ShapeMemento createMemento(){throw new UnsupportedOperationException();}
   public void setMemento(ShapeMemento m){throw new UnsupportedOperationException();}
   
   private int _centerX;
   private int _centerY;
   private int _size;
   private Color _color;  
   private static final int DEFAULT_SIZE = 20;
}
