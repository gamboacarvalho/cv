

import image.PGImage;

import javax.swing.*;
import java.awt.*;


class ImagePanel extends JPanel {
	public static final int WIDTH = 750;
	public static final int HEIGHT = 500;
	private Dimension maxSize;
	private PGImage img = null;
	
	// Constructor
	public ImagePanel() {
		maxSize = new Dimension(WIDTH, HEIGHT);
		setPreferredSize(maxSize);
		this.setBackground(Color.LIGHT_GRAY);
	}
	
	public void setImage(PGImage image) {
		if (image == null)
			return;
		
		img = image;
		Dimension d = new Dimension(img.getWidth(), img.getHeight());
		setPreferredSize(d);
		revalidate(); // To actualize scroll bars
		repaint();
		
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (img != null) {
			g.drawImage (img.getBufferedImage(), 0, 0, this);
		}
	}

	public PGImage getImage() {
		return img;
	}
}