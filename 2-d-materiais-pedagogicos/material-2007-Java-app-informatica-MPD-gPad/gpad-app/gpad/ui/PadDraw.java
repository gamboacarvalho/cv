package gpad.ui;

import gpad.model.IShape;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.Stack;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.*;

/**
   A panel to draw a graph
 */
public class PadDraw extends JComponent implements ToolStateObserver
{
	/**
      Constructs a graph panel.
      @param aToolBar the tool bar with the node and edge tools
      @param aGraph the graph to be displayed and edited
	 */
	public PadDraw(PadToolBar aToolBar){
		_currentTool = aToolBar.getSelectedTool();
		_mems = new Stack<IShape.ShapeMemento>();
		_mems.push(_currentTool.createMemento());
		
		aToolBar.addToolStateListener(this);
		//setBackground(Color.WHITE);
		setBorder(new BevelBorder(BevelBorder.LOWERED));
		MouseHandler h = new MouseHandler();
		addMouseListener(h);
		addMouseMotionListener(h);
	}
	@Override
	public void paintComponent(Graphics g){
		_currentTool.draw((Graphics2D) g);
	}
	@Override
	public Dimension getPreferredSize(){
		Rectangle2D bounds
		= _currentTool.getShape().getBounds();
		return new Dimension(
				(int) bounds.getMaxX(),
				(int) bounds.getMaxY());
	}
	public void undo(){
		if(_currentTool != null && _mems.size() > 1){
			_currentTool.setMemento(_mems.pop());
			_currentTool.setSelected(null);
			repaint();
		}
	}
	public void toolChanged(Tool tool) {
		_currentTool = tool;
	}
	/**
	 * @uml.property   name=" "
	 * @uml.associationEnd   multiplicity="(1 1)" aggregation="shared"
	 */
	private PadToolBar _toolbar;

	/**
	 * @uml.property   name="  "
	 * @uml.associationEnd   multiplicity="(1 1)" aggregation="shared" inverse="padPanel:gpad.IShape"
	 */
	private IShape _shape;
	private Stack<IShape.ShapeMemento> _mems;
	/**
	 * @uml.property   name=""
	 * @uml.associationEnd   multiplicity="(1 1)" aggregation="shared"
	 */
	private Tool _currentTool;
	/**
	 * MouseListener and MouseMotionListener implementation
	 */
	private class MouseHandler extends MouseAdapter implements MouseMotionListener{
		public void mousePressed(MouseEvent e){
			_currentTool.handleMousePressed(e);
			repaint();
		}
		public void mouseReleased(MouseEvent e){
			_currentTool.handleMouseReleased(e);
			_mems.push(_currentTool.createMemento());
			repaint();
		}
		public void mouseDragged(MouseEvent e){
			_currentTool.handleMouseDragged(e);
			repaint();
		}
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub
		}
	}
}
