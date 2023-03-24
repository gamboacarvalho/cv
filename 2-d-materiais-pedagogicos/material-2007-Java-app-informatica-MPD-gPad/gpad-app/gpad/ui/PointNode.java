package gpad.ui;

import gpad.model.INode;
import gpad.model.IShape.ShapeMemento;

import java.awt.*;
import java.awt.geom.*;

/**
   An inivisible node that is used in the toolbar to draw an
   edge.
*/
public class PointNode implements INode
{
   /**
      Constructs a point node with coordinates (0, 0)
   */
   public PointNode()
   {
      _point = new Point2D.Double();
   }

   public void draw(Graphics2D g2)
   {
   }

   public void translate(double dx, double dy)
   {
      _point.setLocation(_point.getX() + dx,
         _point.getY() + dy);
   }

   public boolean contains(Point2D p)
   {
      return false;
   }

   public Rectangle2D getBounds()
   {
      return new Rectangle2D.Double(_point.getX(), 
         _point.getY(), 0, 0);
   }

   public Point2D getConnectionPoint(Point2D other)
   {
      return _point;
   }

   public Object clone()
   {
      try
      {
         return super.clone();
      }
      catch (CloneNotSupportedException exception)
      {
         return null;
      }
   }
   public ShapeMemento createMemento(){throw new UnsupportedOperationException();}
   public void setMemento(ShapeMemento m){throw new UnsupportedOperationException();}
   private Point2D _point;
}
