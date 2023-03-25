package bubbles;

import isel.leic.pg.Console;

public class BubblesGui {

	static final int FOOTER_SIZE = 2;
	static final int RIGHT_MARGIN_SIZE = 8;

	private final Point origin;
	private final int nrLines;
	private final int nrCols;
	private final Command cmd;
	private final Controller ctrl;

	public BubblesGui(int level, Point origin, int nrLines, int nrCols) {
		this.origin = origin;
		this.nrLines = nrLines;
		this.nrCols = nrCols;
		int totalLines = origin.line + nrLines + FOOTER_SIZE;
		int totalCols = origin.col + nrCols + RIGHT_MARGIN_SIZE;

		World w = new World(origin, nrLines, nrCols, level);
		Counter gr = new Counter(origin.inc(-1, nrCols + 2), "GROUP", 0);
		Counter total = new Counter(origin.inc(2, nrCols + 2), "TOTAL", 0);
		Counter l = new Counter(origin.inc(6, nrCols + 2), "LEVEL", level);

		ctrl = new Controller(w, gr, total, l);
		cmd = new Command(totalLines - 1, totalCols, ctrl);
	}

	public void start() {
		printBoard();
		cmd.readCommand();
	}

	void printBoard() {
		int totalLines = origin.line + nrLines + FOOTER_SIZE;
		int totalCols = origin.col + nrCols + RIGHT_MARGIN_SIZE;
		Console.open("Bubbles", totalLines, totalCols);
		printFrame();
		ctrl.reprint();
	}

	void printFrame() {
		Console.color(Console.LIGHT_GRAY, Console.BLACK);
		printLetters();
		printNumbers();
		printCorners();
	}

	void printCorners() {
		Console.cursor(origin.line - 1, origin.col - 1);
		Console.print("+");
		Console.cursor(origin.line + nrLines, origin.col - 1);
		Console.print("+");
		Console.cursor(origin.line + nrLines, origin.col + nrCols);
		Console.print("+");
		Console.cursor(origin.line - 1, origin.col + nrCols);
		Console.print("+");
	}

	void printNumbers() {
		for (int y = 0; y < nrLines; y++) {
			int delta = y >= 9 ? 1 : 0;
			Console.cursor(y + origin.line, origin.col - 2 - delta);
			Console.print(1 + y);
			Console.cursor(y + origin.line, origin.col - 1);
			Console.print('|');
			Console.cursor(y + origin.line, origin.col + nrCols);
			Console.print('|');
		}
	}

	private void printLetters() {
		for (int x = 0; x < nrCols; x++) {
			Console.cursor(origin.line - 2, x + origin.col);
			Console.print((char) ('A' + x));
			Console.cursor(origin.line - 1, x + origin.col);
			Console.print('-');
			Console.cursor(origin.line + nrLines, x + origin.col);
			Console.print('-');
		}
	}
}
