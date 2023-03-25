package bubbles;

import isel.leic.pg.Console;

public class Node implements Comparable<Node> {

	static final int[] colors = { Console.RED, Console.GREEN, Console.BLUE,
			Console.YELLOW, Console.MAGENTA, Console.ORANGE, Console.CYAN,
			Console.PINK, Console.GRAY, Console.BLACK };
	final Point origin;
	final int col;
	private int line;
	final int defaultColor;
	private int currentBackColor, currentForeColor;

	public Node(Point origin, int line, int col, int level) {
		assert level <= colors.length;
		this.origin = origin;
		this.line = line;
		this.col = col;
		int colorIdx = (int) (Math.random() * (level));
		defaultColor = colors[colorIdx];
		currentForeColor = defaultColor;
		currentBackColor = Console.WHITE;
	}

	public Node(Point origin, int line, int col, int level, int custColor) {
		assert level <= colors.length;
		this.origin = origin;
		this.line = line;
		this.col = col;
		defaultColor = custColor;
		currentForeColor = defaultColor;
		currentBackColor = Console.WHITE;
	}

	public void lineInc() {
		line++;
	}

	public void lineDec() {
		line--;
	}

	public int line() {
		return line;
	}

	public void print() {
		Console.cursor(origin.line + line, origin.col + col);
		Console.color(currentForeColor, currentBackColor);
		Console.print('O');
	}

	/**
	 * Select this node and returns its default color.
	 */
	public int select() {
		currentBackColor = Console.GRAY;
		currentForeColor = Console.BLUE;
		print();
		return defaultColor;
	}

	public void unselect() {
		currentBackColor = Console.WHITE;
		currentForeColor = defaultColor;
		print();
	}

	/**
	 * Returns true if the default color is equals to the target color,
	 * otherwise returns false.
	 */
	public boolean inGroup(int target) {
		if (target != defaultColor)
			return false;
		currentBackColor = Console.BLACK;
		currentForeColor = Console.BLUE;
		print();
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + col;
		result = prime * result + line;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (col != other.col)
			return false;
		if (line != other.line)
			return false;
		return true;
	}

	@Override
	public int compareTo(Node n) {
		if (this.line != n.line)
			return this.line - n.line;
		else
			return this.col - n.col;
	}

	@Override
	public String toString() {
		return "[" + col + ", " + line + ", c=" + defaultColor + "]";
	}

	public boolean isEmptNode() {
		return defaultColor == Console.WHITE;
	}

}
