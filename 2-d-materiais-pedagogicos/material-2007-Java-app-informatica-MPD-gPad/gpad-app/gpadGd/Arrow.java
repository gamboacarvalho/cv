package gpadGd;

import gpad.model.AbstractLink;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Arrow extends AbstractLink{
	public static final double AMPLITUDE = Math.PI/6;
	public static final int SIDE = 15;
	
	public void draw(Graphics2D g2){
		Line2D line = getLine();
		updateHead(line.getX1(), line.getY1(), line.getX2(), line.getY2());
		//
		// Draw arrow head
		//
		//g2.fillPolygon(cordXs, cordYs, 3); // Alternativa às duas linhas abaixo
		g2.drawLine(_cordXs[0], _cordYs[0], _cordXs[1], _cordYs[1]);
		g2.drawLine(_cordXs[0], _cordYs[0], _cordXs[2], _cordYs[2]);
		//
		// Draw arrow line
		//
		g2.draw(line);
		//
		// Draw arrow label at middle point
		//
		g2.drawString("" + getLabel(), _middle.x, _middle.y);
	}

	public boolean contains(Point2D aPoint){
		final double MAX_DIST = 2;
		return getLine().ptSegDist(aPoint) < MAX_DIST;
	}
	public String getLabel(){return _label;}
	public void setLabel(String l){_label = l;}
	
	protected double initX, initY, endX, endY;
	protected int [] _cordXs = new int[3];
	protected int [] _cordYs = new int[3];
	protected Point _middle = new Point();
	protected String _label = "";
	
	private void updateHead(double initX, double initY, double endX, double endY){
		//
		// Arrow points
		//
		_cordXs[0] = (int) endX;
		_cordYs[0] = (int) endY;
		//
		// Arrow points
		//
		double theta = Math.atan((endY-initY)/(endX-initX));
		if(endX < initX)
			theta += Math.PI;
		double sin = Math.sin(theta + (Math.PI - AMPLITUDE));
		double cos = Math.cos(theta + (Math.PI - AMPLITUDE));
		_cordXs[1] = (int)(endX + cos*SIDE);
		_cordYs[1] = (int)(endY + sin*SIDE);
		sin = Math.sin(theta + (Math.PI + AMPLITUDE));
		cos = Math.cos(theta + (Math.PI + AMPLITUDE));
		_cordXs[2] = (int)(endX + cos*SIDE);
		_cordYs[2] = (int)(endY + sin*SIDE);
		//
		// Middle point
		//
		_middle.x = (int)((endX + initX)/2 + (int)(15*sin));
		_middle.y = (int)((endY + initY)/2 - (int)(15*cos));
	}
}
