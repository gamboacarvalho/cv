package gpad.ui;

import gpad.model.CompositeShape;
import gpad.model.INode;
import gpad.model.IShape;
import gpad.model.ShapePrototypeFactory;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

/**
   A tool bar that contains node and edge prototype icons.
   Exactly one icon is selected at any time.
 */
public class PadToolBar extends JPanel implements ActionListener, ToolSubject
{
	/**
      Constructs a tool bar with no icons.
	 */
	public PadToolBar(IShape [] prototypes, CompositeShape shape){
		_factory = new ShapePrototypeFactory();
		_toolNode = new ToolNode(shape, _factory);
		_toolLink = new ToolLink(shape, _factory);
		_toolMove = new ToolMove(shape);
		//
		// grabberButton
		//
		_group = new ButtonGroup();
		initialize(prototypes, shape);
	}
	public void initialize(IShape [] prototypes, CompositeShape shape){
		if(shape == null) shape = new CompositeShape();
		_toolNode.setShape(shape);
		_toolLink.setShape(shape);
		_toolMove.setShape(shape);
		//
		// Clean toolbar
		//
		for(Component c : getComponents()){
			AbstractButton b = (AbstractButton) c;
			_group.remove(b);
			remove(c);
		}
		//
		// grabberButton
		//
		_grabberButton = new PadToolButtonGrabber(_toolMove);
		_group.add(_grabberButton);
		add(_grabberButton);
		_grabberButton.setSelected(true);
		_grabberButton.addActionListener(this);
		//
		// PadToolButtons
		// 
		for (IShape s : prototypes) {
			add(s);
		}
		//
		// Make selection
		//
		_selectedBt = _grabberButton;
		_selectedTool = _toolMove;
		notifyToolHandlers();
	}
	/**
	 * Adds a node to the tool bar.
	 * @param n the node to add
	 */
	private void add(IShape s)
	{
		JToggleButton button = null;
		if(s instanceof INode)
			button = new PadButton(s, _toolNode);
		else 
			button = new PadButton(s, _toolLink);
		_group.add(button);
		add(button);
		button.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if(source != _selectedBt){
			_selectedBt = (PadButton) source;
			_selectedTool = _selectedBt.getTool();
			notifyToolHandlers();
		}
	}
	public Tool getSelectedTool(){
		return _selectedTool;
	}
	public void notifyToolHandlers(){
		_factory.changeShape(_selectedBt.getShape());
		for (ToolStateObserver handler : _toolStateHandlers) {
			handler.toolChanged(_selectedTool);
		}
	}
	public void addToolStateListener(ToolStateObserver l){
		_toolStateHandlers.add(l);
	}
	public void removeToolStateListener(ToolStateObserver l){
		_toolStateHandlers.remove(l);
	}
	/**
	 * @uml.property   name=""
	 * @uml.associationEnd   multiplicity="(1 1)" aggregation="shared" inverse="context:gpad.ToolStateObserver"
	 */
	private List<ToolStateObserver> _toolStateHandlers = new ArrayList<ToolStateObserver>();
	private ButtonGroup _group;
	private PadButton _grabberButton;
	private PadButton _selectedBt;
	private Tool _selectedTool;
	private ShapePrototypeFactory _factory;
	/**
	 * @uml.property   name=""
	 * @uml.associationEnd   multiplicity="(1 1)" aggregation="shared"
	 */
	private ToolNode _toolNode;
	/**
	 * @uml.property   name=" "
	 * @uml.associationEnd   multiplicity="(1 1)" aggregation="shared"
	 */
	private ToolLink _toolLink;
	/**
	 * @uml.property   name="   "
	 * @uml.associationEnd   multiplicity="(1 1)" aggregation="shared"
	 */
	private ToolMove _toolMove;
	/** 
	 * @uml.property name="_bts"
	 * @uml.associationEnd multiplicity="(1 -1)" aggregation="shared" inverse="padToolBar:gpad.ui.PadToolButton"
	 */
	private Collection<PadButton> toolButton;
}
