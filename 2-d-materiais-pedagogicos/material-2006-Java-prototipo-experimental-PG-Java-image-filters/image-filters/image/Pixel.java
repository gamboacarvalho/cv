
package image;


public class Pixel {
	public int red, green, blue;
		
	private int normalizeComponent(int v){
		if (v > 255) return 255;
		if (v < 0) return 0;
		return v;
	}
	public void setPixel(int p) {
		red = (p >> 16) & 0xff;
		green = (p >>8) & 0xff;
		blue = p & 0xff;
	}
	public int getPixel() {
		return (red << 16) | (green << 8) | blue;
	}
	public void normalize() {
	   red = normalizeComponent(red);
	   green = normalizeComponent(green);
	   blue = normalizeComponent(green);
	}
	public static  boolean isIdentical(int px1, int px2) {
		int r1  = (px1 >> 16) & 0xff;
		int g1 = (px1 >> 8) & 0xff;
		int b1 = px1 & 0xff;
		 
		int r2  = (px2 >> 16) & 0xff;
		int g2 = (px2 >> 8) & 0xff;
		int b2 = px2 & 0xff;
				 
		return Math.sqrt((r2-r1)*(r2-r1) +(g2-g1)*(g2-g1)+ (b2-b1)*(b2-b1)) < 40; 
	}
}

