package gpadGd;

import java.awt.Color;

import gpad.model.ILink;
import gpad.model.INode;
import gpad.model.INodeFactory;
import gpad.model.IShape;
import gpad.ui.PadFrame;

import javax.swing.*;

/**
   A program for editing UML diagrams.
 */
public class SimpleGraphEditor
{
	public static void main(String[] args){
		RectangularNode rect = new RectangularNode();
		rect.setText("<html>Ola<br>Isel</html>");
		IShape [] a  = {
				new NodeCircle(Color.BLUE), 
				new NodeCircle(Color.WHITE),
				new Arrow(),
				new LineEdge(),
				rect}; 
		JFrame frame = new PadFrame(a);
		frame.setTitle("Simple Graph Editor");
		frame.setVisible(true);
	}
}

