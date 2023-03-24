package imageProcessing;

import image.PGImage;
import image.Pixel;

class ConvolveOp {
	private Kernel kernel;
	private int[][] srcData;
	private int[][] destData;
	private int width;
	private int height;
	
	private void convolveKernel(int y, int x, int kernelWidth, int kernelHeight, int[][] kernelData) {
		int kernelMinY = -kernelHeight/2;
		int kernelMaxY = kernelHeight/2;
		int kernelMinX = -kernelWidth/2;
		int kernelMaxX = kernelWidth/2;
		
		// Test if it is off limits
		if (x+kernelMinX < 0 ||  x+kernelMaxX > width-1 || y+kernelMinY < 0 || y+kernelMaxY > height-1) { 
			destData[y][x] = srcData[y][x];
			return;
		}
		
		Pixel pixel = new Pixel();
		int red = 0, green = 0, blue = 0;
				
		// Convolve kernel
		for (int dy = kernelMinY, ky = 0; dy <= kernelMaxY; ++dy, ++ky) {
			for (int dx = kernelMinX, kx = 0; dx <= kernelMaxX; ++dx, ++kx) {
				pixel.setPixel(srcData[y+dy][x+dx]);
				// Dot product
				red += pixel.red * kernelData[ky][kx];
				green += pixel.green * kernelData[ky][kx];
				blue += pixel.blue * kernelData[ky][kx];
				
			}
		}
		int kWeight = kernel.getWeight();
		if (kWeight == 0) {
			red += 128;
			green += 128;
			blue += 128;
		}
		else {
			red /= kWeight;
			green /= kWeight;
			blue /= kWeight;
		}
		pixel.red = red;
		pixel.green = green;
		pixel.blue = blue;
		
		pixel.normalize();
		// Write pixel in dest image
		destData[y][x] = pixel.getPixel();
	}
		
	// Constructor
	public ConvolveOp(Kernel kernel) {
		this.kernel = kernel;
	}
	
	public PGImage filter(PGImage src) {
		width = src.getWidth();
		height = src.getHeight();
		// Source data
		srcData = src.getPixels();
		// Dest data
		destData = new int[height][width];
		// Kernel dimensions
		int kernelWidth = kernel.getWidth();
		int kernelHeight = kernel.getHeight();
		int[][] kernelData = kernel.getKernelData();
				
		//System.out.println("Kernel: " + kernel.toString());
		
		// Convolution
		for (int y = 0; y < height; ++y) {
			for (int x = 0; x < width; ++x) {
				convolveKernel(y, x, kernelWidth, kernelHeight, kernelData);
			}
		}
		// Return dest image
		return new PGImage(destData);
	}
	
	public Kernel getKernel() {
		return kernel;
	}
}
