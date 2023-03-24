package image;

import java.io.*;
import java.awt.image.*;
import javax.swing.*;
import javax.imageio.*;


public class PGImage  {
	private BufferedImage image;
	// Constructor
	public PGImage(String imageName) {
		try {
			File f = new File(imageName);
			image = ImageIO.read(f);
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Error loading image");
		}
	}
	
	public PGImage(BufferedImage img) {
		image = img;
	}
	
	public PGImage(int width, int height, int type) {
		image = new BufferedImage(width, height, type);
	}
	
	public PGImage(int[][] pixels) {
		image = new BufferedImage(pixels[0].length, pixels.length, BufferedImage.TYPE_3BYTE_BGR);
		setPixels(pixels);
	}
	
	public int[][] getPixels() {
		int width = image.getWidth(), height=image.getHeight();
		int [][] pixels= new int[height][width];
	 	
		for (int y=0; y < height; ++y) {
			for (int x=0; x < width; ++x) {
				pixels[y][x] = image.getRGB(x,y);
			}
		}
		return pixels;
	}
	
	public void setPixels(int[][] pixels) {
		int width = image.getWidth(), height = image.getHeight();
	 	
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				image.setRGB(x, y, 0xff000000 | pixels[y][x]);
			}
		}
	}
	
	public BufferedImage getBufferedImage() {
		return image;
	}
	public int getType() {
		return image.getType();
	}
	public int getWidth() {
		return image.getWidth();
	}
	public int getHeight() {
		return image.getHeight();
	}
	
	public void save(String fileName) {
		File f = new File(fileName);
		try {
			ImageIO.write(image,"jpg", f);
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Error saving image");
		}
	}
}
