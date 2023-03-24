package gpadUml;

import gpad.model.INodeFactory;
import gpad.model.IShape;
import gpad.ui.PadFrame;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import javax.swing.JFrame;

/**
   A program for editing UML diagrams.
 */
public class UMLEditor
{
	public static void main(String[] args)
	{
		IShape [] a = UMLTools.getShapePrototypes();
		JFrame frame = new PadFrame(a);
		frame.setVisible(true);
	}
}

