package gpad.ui;

import gpad.model.ILink;
import gpad.model.INode;
import gpad.model.IShape;

import javax.swing.Icon;
import javax.swing.JToggleButton;

public class PadButton extends JToggleButton{
	public PadButton(Icon icon, Tool t){
		super(icon);
		_tool = t;
	}
	public PadButton(IShape shape, Tool t){
		_shape = shape;
		_tool = t;
		if(shape instanceof INode) setIcon(new PadToolIconNode(shape));
		if(shape instanceof ILink) setIcon(new PadToolIconLink((ILink)shape));
	}
	public IShape getShape(){
		return _shape;
	}
	public Tool getTool(){
		return _tool;
	}
	/**
	 * @uml.property   name="  "
	 * @uml.associationEnd   multiplicity="(1 0)" aggregation="shared" 
	 */
	private IShape _shape;
	/**
	 * @uml.property   name=" "
	 * @uml.associationEnd   multiplicity="(1 1)" aggregation="shared" 
	 */
	private Tool _tool;
    public static final int BUTTON_SIZE = 25;
    public static final int OFFSET = 4;
}
