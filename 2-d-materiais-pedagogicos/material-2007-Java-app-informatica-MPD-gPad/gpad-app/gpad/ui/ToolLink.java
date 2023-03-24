package gpad.ui;

import gpad.model.CompositeShape;
import gpad.model.ILink;
import gpad.model.ILinkFactory;
import gpad.model.INode;
import gpad.model.IShape;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class ToolLink extends ToolSelection{
	public ToolLink(CompositeShape s, ILinkFactory f){
		super(s);
		_factory = f;
	}
	@Override
	public void draw(Graphics2D g2) {
		super.draw(g2);
		if (_rubberBandStart != null)
		{
			Color oldColor = g2.getColor();
			g2.setColor(PURPLE);
			g2.draw(new Line2D.Double(_rubberBandStart, _lastMousePoint));
			g2.setColor(oldColor);
		}
	}
	@Override
	public void handleMousePressed(MouseEvent e) {
		super.handleMousePressed(e);
		if (_selected instanceof INode)
			_lastMousePoint = _rubberBandStart = e.getPoint();
	}
	public void handleMouseDragged(MouseEvent e) {
		_lastMousePoint = e.getPoint();
	}
	public void handleMouseReleased(MouseEvent e) {
		if (_rubberBandStart != null){
			IShape endShape = _shape.findShape(e.getPoint());
			if(_selected != endShape && endShape instanceof INode){
				ILink newEdge = _factory.makeILink();
				newEdge.connect((INode) _selected, (INode) endShape);
				_shape.add(newEdge);
				_selected = newEdge;
			}
			_rubberBandStart = null;
			_lastMousePoint = null;
		}
	}
	protected Point2D _rubberBandStart;
	protected Point2D _lastMousePoint;
	/**
	 * @uml.property   name="  "
	 * @uml.associationEnd   aggregation="shared" multiplicity="(1 1)" 
	 */
	private ILinkFactory _factory;
}
