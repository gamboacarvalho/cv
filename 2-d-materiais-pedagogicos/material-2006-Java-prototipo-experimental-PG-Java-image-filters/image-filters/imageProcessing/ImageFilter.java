
package imageProcessing;

import image.PGImage;
import image.Pixel;


public class ImageFilter {
	public static final int F_BRIGHTNESS    = 1;
	public static final int F_GREYSCALE     = 2;
	public static final int F_INVERT        = 3;
	public static final int F_PENCIL        = 4;
	public static final int F_EMBOSS        = 5;
	public static final int F_NOISEREDUCTOR = 6;
	public static final int F_SHARPER       = 7;
		
	private int filterType;
	private String name;
	
	private int[][] pixels, newPixels;
	private Pixel pixel = new Pixel();
	private int squareSize = 10;

	
	// Kernels
	private final static int[][] kernelEmboss = { 
		  {0, 0, -1},
		  {1, 1,  0},
		  {0, 0, -1}	                    
	};
	private final static int[][] kernelNoiseReductor = { 
		  {1, 2, 1},
		  {2, 4, 2},
		  {1, 2, 1}		                    
	};
	private final static int[][] kernelSharper = { 
		  {-1, -1, -1},
		  {-1, 9,  -1},
		  {-1, -1, -1}
	};
	private final static int[][] kernelEdge = { 
		  {-1, -1, -1},
		  {-1,  8, -1},
		  {-1, -1, -1}
	};
	
	// Kernels
	private Kernel emboss = new Kernel(kernelEmboss);
	private Kernel noiseReductor = new Kernel(kernelNoiseReductor);
	private Kernel sharper = new Kernel(kernelSharper);
	private Kernel edge = new Kernel(kernelEdge);
		
	
	// Constructor
	public ImageFilter(int filterType, String name) {
		this.filterType = filterType;
		this.name = name;
	}
	public String getName() {
		return name;
	}
	
	public PGImage execute(PGImage img) {
/*		// Pixelize
		if (filterType == F_PIXELIZE) 
			return pixelize(img);
		*/
		
		ConvolveOp convolveOp;
		switch (filterType) {
	/*		case F_EDGE:
					convolveOp = new ConvolveOp(edge);
					return convolveOp.filter(img);*/
			case F_EMBOSS: 
					convolveOp = new ConvolveOp(emboss);
		 			return convolveOp.filter(img);
			case F_NOISEREDUCTOR: 
					convolveOp = new ConvolveOp(noiseReductor);
					return convolveOp.filter(img);
			case F_SHARPER: 
					convolveOp = new ConvolveOp(this.sharper);
					return convolveOp.filter(img);
		}
		// Pixel filters
		return pixelOp(img);
	}
	
	public PGImage pixelOp(PGImage img) {
		this.pixels = img.getPixels();
		int width = pixels[0].length, height = pixels.length;
		this.newPixels =  new int[height][width];
		
		for (int y = 0; y < height; ++y) {
			for (int x = 0; x < width; ++x) {
				newPixels[y][x] = filterRGB(x, y);
			}
		}
		return  new PGImage(newPixels);
	}
	
	int filterRGB(int x, int y) {
		switch (filterType) {
			case F_BRIGHTNESS: return brighter(pixels[y][x]);
			case F_GREYSCALE: return greyScale(pixels[y][x]);
			case F_INVERT: return invert(pixels[y][x]);
			case F_PENCIL: return pencil(x,y);
				
			default: return 0;
		}
	}		


	// Pixelize
	private void pixelize(int rx, int ry) {
		int mr=0, mg=0, mb=0, nPixels=0;
		for (int py=ry*squareSize; py < (ry+1)*squareSize; ++py) {
			for (int px=rx*squareSize; px < (rx+1)*squareSize; ++px) {
				if (py < pixels.length && px < pixels[0].length) {
					pixel.setPixel(pixels[py][px]);
					mr+= pixel.red; mg += pixel.green; mb += pixel.blue;
					nPixels++;
				}	
			}
		} 
		mr /= nPixels; mg /= nPixels; mb /= nPixels;
		pixel.red = mr; pixel.green = mg; pixel.blue=mb;
		int nPixel= pixel.getPixel();
		for (int py=ry*squareSize; py < (ry+1)*squareSize; ++py) {
			for (int px=rx*squareSize; px < (rx+1)*squareSize; ++px) {
				if (py < pixels.length && px < pixels[0].length) {
					newPixels[py][px]= nPixel;
				}	
			}
		} 
	}
	
	public PGImage pixelize(PGImage img) {
		this.pixels= img.getPixels();
		int width=pixels[0].length, height=pixels.length;
		this.newPixels =  new int[height][width];
		
		int nWidthSquares = width /squareSize;
		int nHeightSquares = height / squareSize;
		
		for (int y=0; y < nHeightSquares; ++y) {
			for (int x=0; x < nWidthSquares; ++x) {
				pixelize(x, y);
			}
		}
		
		return  new PGImage(newPixels);
	}

	
	//
	// Simple filters
	// 
		
	// Brightness
	int brighter(int px) {
		pixel.setPixel(px);
		pixel.red += 10;
		pixel.green += 10;
		pixel.blue += 10;
		pixel.normalize();
		return pixel.getPixel();
	}
	
	// Grey scale
	int greyScale(int px) {
		pixel.setPixel(px);
		
		int med = (pixel.red + pixel.green + pixel.blue)/3;
		pixel.red = med;
		pixel.green = med;
		pixel.blue = med;
		return pixel.getPixel();
	}
	
	// Negative
	int invert(int px) {
		pixel.setPixel(px);
		pixel.red = 255 - pixel.red;
		pixel.green = 255 - pixel.green;
		pixel.blue = 255 - pixel.blue;
		return pixel.getPixel();
	}
	
	// Pencil
	int pencil(int x, int y) {
		int px = pixels[y][x];
	    pixel.setPixel(px);
		if (y-1 >= 0 && x-1 >= 0) {
			if (!Pixel.isIdentical(px, pixels[y-1][x]) ||
				!Pixel.isIdentical(px, pixels[y][x-1]) )
				pixel.red = pixel.green = pixel.blue = 255;
			else
				pixel.red = pixel.green = pixel.blue = 0;
		}
		else
			pixel.red = pixel.green = pixel.blue = 0;
		
		return pixel.getPixel();
	}
	
} // ImageFilter
