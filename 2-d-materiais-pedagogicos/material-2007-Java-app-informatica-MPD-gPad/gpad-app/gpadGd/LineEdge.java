package gpadGd;

import gpad.model.AbstractLink;

import java.awt.*;
import java.awt.geom.*;

/**
 *  An edge that is shaped like a straight line.
 */
public class LineEdge extends AbstractLink{
   public void draw(Graphics2D g2)
   {
      g2.draw(getLine());
   }

   public boolean contains(Point2D aPoint)
   {
      final double MAX_DIST = 2;
      return getLine().ptSegDist(aPoint) 
         < MAX_DIST;
   }
}
