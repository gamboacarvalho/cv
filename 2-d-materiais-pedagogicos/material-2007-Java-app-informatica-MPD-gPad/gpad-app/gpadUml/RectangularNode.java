package gpadUml;

import gpad.model.INode;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
   A node that has a rectangular shape.
*/
public abstract class RectangularNode implements INode
{
   public Object clone()
   {
      try
      {
         RectangularNode cloned = (RectangularNode) super.clone();
         cloned._bounds = (Rectangle2D) _bounds.clone();
         return cloned;      
      }
      catch (CloneNotSupportedException exception)
      {
         return null;
      }    
   }

   public void translate(double dx, double dy)
   {
      _bounds.setFrame(_bounds.getX() + dx,
         _bounds.getY() + dy, 
         _bounds.getWidth(), 
         _bounds.getHeight());
   }

   public boolean contains(Point2D p)
   {
      return _bounds.contains(p);
   }

   public Rectangle2D getBounds()
   {
      return (Rectangle2D) _bounds.clone();
   }

   public void setBounds(Rectangle2D newBounds)
   {
      _bounds = newBounds;
   }

   public Point2D getConnectionPoint(Point2D aPoint)
   {
      double slope = _bounds.getHeight() / _bounds.getWidth();
      double x = _bounds.getCenterX();
      double y = _bounds.getCenterY();
      double ex = aPoint.getX() - x;
      double ey = aPoint.getY() - y;
      
      if (ex != 0 && -slope <= ey / ex && ey / ex <= slope)
      {  
         // intersects at left or right boundary
         if (ex > 0) 
         {
            x = _bounds.getMaxX();
            y += (_bounds.getWidth() / 2) * ey / ex;
         }
         else
         {
            x = _bounds.getX();
            y -= (_bounds.getWidth() / 2) * ey / ex;
         }
      }
      else if (ey != 0)
      {  
         // intersects at top or bottom
         if (ey > 0) 
         {
            x += (_bounds.getHeight() / 2) * ex / ey;
            y = _bounds.getMaxY();
         }
         else
         {
            x -= (_bounds.getHeight() / 2) * ex / ey;
            y = _bounds.getY();
         }
      }
      return new Point2D.Double(x, y);
   }

   private void writeObject(ObjectOutputStream out)
      throws IOException
   {
      out.defaultWriteObject();
      writeRectangularShape(out, _bounds);
   }

   /**
      A helper method to overcome the problem that the 2D shapes
      aren't serializable. It writes x, y, width and height
      to the stream.
      @param out the stream
      @param s the shape      
   */
   private static void writeRectangularShape(
      ObjectOutputStream out, 
      RectangularShape s)
      throws IOException
   {
      out.writeDouble(s.getX());
      out.writeDouble(s.getY());
      out.writeDouble(s.getWidth());
      out.writeDouble(s.getHeight());
   }

   private void readObject(ObjectInputStream in)
      throws IOException, ClassNotFoundException
   {
      in.defaultReadObject();
      _bounds = new Rectangle2D.Double();
      readRectangularShape(in, _bounds);
   }
   
   /**
      A helper method to overcome the problem that the 2D shapes
      aren't serializable. It reads x, y, width and height
      from the stream.
      @param in the stream
      @param s the shape whose frame is set from the stream values
   */
   private static void readRectangularShape(ObjectInputStream in,
      RectangularShape s)
      throws IOException
   {
      double x = in.readDouble();
      double y = in.readDouble();
      double width = in.readDouble();
      double height = in.readDouble();
      s.setFrame(x, y, width, height);
   }

   private transient Rectangle2D _bounds;
}
