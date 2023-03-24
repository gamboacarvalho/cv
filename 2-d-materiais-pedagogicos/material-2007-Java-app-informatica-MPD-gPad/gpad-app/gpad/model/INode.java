package gpad.model;

import java.awt.*;
import java.awt.geom.*;
import java.io.*;

/**
   A node in a graph.
*/
public interface INode extends IShape{
   /**
      Translates the node by a given amount.
      @param dx the amount to translate in the x-direction
      @param dy the amount to translate in the y-direction
   */
   void translate(double dx, double dy);

   /**
      Get the best connection point to connect this node 
      with another node. This should be a point on the boundary
      of the shape of this node.
      @param aPoint an exterior point that is to be joined
      with this node
      @return the recommended connection point
   */
   Point2D getConnectionPoint(Point2D exterior);
}
