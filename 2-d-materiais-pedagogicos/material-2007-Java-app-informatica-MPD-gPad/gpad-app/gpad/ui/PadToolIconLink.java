package gpad.ui;

import gpad.model.ILink;
import gpad.model.INode;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import javax.swing.Icon;

public class PadToolIconLink  implements Icon{

	public PadToolIconLink(ILink l){
		_link = l;
        _link.connect(START, END);
	}
    public int getIconHeight() { return PadButton.BUTTON_SIZE; }
    public int getIconWidth() { return PadButton.BUTTON_SIZE; }
    public void paintIcon(Component c, Graphics g,
       int x, int y)
    {
    	Graphics2D g2 = (Graphics2D) g;
        g2.translate(x, y);
        g2.setColor(Color.black);
        _link.draw(g2);
        g2.translate(-x, -y);
    }
    private ILink _link;
    private static final INode START = new PointNode();
    private static final INode END = new PointNode();
    static {
    	START.translate(PadButton.OFFSET, PadButton.OFFSET);
        END.translate(PadButton.BUTTON_SIZE - PadButton.OFFSET, 
        		PadButton.BUTTON_SIZE - PadButton.OFFSET);
    }
}
