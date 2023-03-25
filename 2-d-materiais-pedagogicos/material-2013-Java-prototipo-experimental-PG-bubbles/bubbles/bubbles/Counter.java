package bubbles;

import isel.leic.pg.Console;

public class Counter {
	final String label;
	private int value;
	final Point location;

	public Counter(Point location, String label, int init) {
		this.label = label;
		this.value = init;
		this.location = location;
	}

	public int getValue() {
		return value;
	}

	public void print() {
		Console.color(Console.RED, Console.GRAY);
		Console.cursor(location.line, location.col);
		Console.print(label);
		for (int i = 0; i < label.length(); i++) {
			Console.cursor(location.line + 1, location.col + i);
			Console.print(' ');
		}
		refresh();
	}

	public void refresh() {
		Console.color(Console.WHITE, Console.GRAY);
		Console.cursor(location.line + 1, location.col);
		Console.print(value);
	}

	public void add(int points) {
		value += points;
	}

	public void reset() {
		value = 0;
		print();
	}
}
