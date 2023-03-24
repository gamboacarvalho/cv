package gpad.ui;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Icon;

public class PadToolButtonGrabber extends PadButton{
	public PadToolButtonGrabber(Tool tool){
		super(new GrabberIcon(), tool);
	}
	static class GrabberIcon implements Icon{
		public int getIconHeight() { return PadButton.BUTTON_SIZE; }
		public int getIconWidth() { return PadButton.BUTTON_SIZE; }
		public void paintIcon(Component c, Graphics g,
				int x, int y)
		{
			Graphics2D g2 = (Graphics2D) g;
			ToolSelection.drawGrabber(g2, x + PadButton.OFFSET, y + PadButton.OFFSET);
			ToolSelection.drawGrabber(g2, x + PadButton.OFFSET, y + PadButton.BUTTON_SIZE - PadButton.OFFSET);
			ToolSelection.drawGrabber(g2, x + PadButton.BUTTON_SIZE - PadButton.OFFSET, y + PadButton.OFFSET);
			ToolSelection.drawGrabber(g2, x + PadButton.BUTTON_SIZE - PadButton.OFFSET, y + PadButton.BUTTON_SIZE - PadButton.OFFSET);
		}		
	}
}
