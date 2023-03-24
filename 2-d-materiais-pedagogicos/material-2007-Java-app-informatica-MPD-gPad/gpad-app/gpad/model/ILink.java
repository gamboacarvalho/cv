package gpad.model;

import java.awt.*;
import java.awt.geom.*;
import java.io.*;

/**
   An edge in a graph.
*/
public interface ILink extends IShape{
   /**
      Connects this edge to two nodes.
      @param aStart the starting node
      @param anEnd the ending node
   */
   void connect(INode aStart, INode anEnd);

   /**
      Gets the starting node.
      @return the starting node
   */
   INode getStart();

   /**
      Gets the ending node.
      @return the ending node
   */
   INode getEnd();

   /**
      Gets the points at which this edge is connected to
      its nodes.
      @return a line joining the two connection points
   */
   Line2D getLine();
}

