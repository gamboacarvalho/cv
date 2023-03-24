package gpad.ui;

import gpad.model.IShape;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import javax.swing.Icon;

public class PadToolIconNode implements Icon{

	public PadToolIconNode(IShape s){
		_shape = s;
		double width = _shape.getBounds().getMaxX(); //.getWidth();
		double height = _shape.getBounds().getMaxY();//.getHeight();
		double scaleX = (PadButton.BUTTON_SIZE)/ width;
		double scaleY = (PadButton.BUTTON_SIZE)/ height;
		_scale = Math.min(scaleX, scaleY);

	}
	public int getIconHeight() { return PadButton.BUTTON_SIZE; }
	public int getIconWidth() { return PadButton.BUTTON_SIZE; }
	public void paintIcon(Component c, Graphics g,
			int x, int y)
	{
		Graphics2D g2 = (Graphics2D) g;
		AffineTransform oldTransform = g2.getTransform();
		g2.translate(x, y);
		g2.scale(_scale , _scale );
		g2.setColor(Color.black);
		_shape.draw(g2);
		g2.setTransform(oldTransform);
	}
	private IShape _shape;
	private double _scale;
}
