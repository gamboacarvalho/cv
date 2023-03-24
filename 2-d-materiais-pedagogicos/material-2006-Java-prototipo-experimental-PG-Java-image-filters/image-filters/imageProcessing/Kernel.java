package imageProcessing;

import java.util.Arrays;


class Kernel {
	private int[][] data;
	private int width;
	private int height;
	private int weight;
			
	// Constructor 
	public Kernel(int[][] data) {
		this.width = data[0].length;
		this.height = data.length;
		this.data = data;
		// Determine kernel weight
		for (int row = 0; row < height; ++row) {
			for (int col = 0; col < width; ++col) {
				weight += data[row][col]; 
			}
		}
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	public int[][] getKernelData() {
		return  data;
	} 
	public int getWeight() {
		return weight;
	}
	public String toString() {
		String out = "[";
		for (int row = 0; row < height; ++row) {
			out += Arrays.toString(data[row]) + "\n";
		}
		out += "]\n";
		return out;
	}
}
