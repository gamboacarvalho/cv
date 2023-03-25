package bubbles;

public class Point {

	final int line, col;

	public Point(int line, int col) {
		this.line = line;
		this.col = col;
	}

	public Point inc(int lines, int cols) {
		return new Point(line + lines, col + cols);
	}

	@Override
	public String toString() {
		return "Point [line=" + line + ", col=" + col + "]";
	}

	public Point move(Point p) {
		return new Point(line + p.line, col + p.col);
	}
}
