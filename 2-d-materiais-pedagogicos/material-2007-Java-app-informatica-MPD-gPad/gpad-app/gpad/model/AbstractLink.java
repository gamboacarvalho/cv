package gpad.model;

import java.awt.*;
import java.awt.geom.*;

/**
   A class that supplies convenience implementations for 
   a number of methods in the Edge interface type.
*/
public abstract class AbstractLink implements ILink{  
   public void connect(INode s, INode e){  
      _start = s;
      _end = e;
   }
   public INode getStart(){
      return _start;
   }
   public INode getEnd(){
      return _end;
   }

   public Rectangle2D getBounds(){
      Line2D conn = getLine();      
      Rectangle2D r = new Rectangle2D.Double();
      r.setFrameFromDiagonal(conn.getX1(), conn.getY1(),
            conn.getX2(), conn.getY2());
      return r;
   }
   public Line2D getLine(){
      Rectangle2D startBounds = _start.getBounds();
      Rectangle2D endBounds = _end.getBounds();
      Point2D startCenter = new Point2D.Double(
            startBounds.getCenterX(), startBounds.getCenterY());
      Point2D endCenter = new Point2D.Double(
            endBounds.getCenterX(), endBounds.getCenterY());
      return new Line2D.Double(
            _start.getConnectionPoint(endCenter),
            _end.getConnectionPoint(startCenter));
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
   
	/**
	 * @uml.property   name=""
	 * @uml.associationEnd   multiplicity="(1 1)" aggregation="shared"
	 */
   protected INode _start;
   protected INode _end;
}
