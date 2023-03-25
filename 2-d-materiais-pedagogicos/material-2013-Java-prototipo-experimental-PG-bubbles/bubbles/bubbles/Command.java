package bubbles;

import isel.leic.pg.Console;

import javax.swing.JOptionPane;

public class Command {
	private final static int LEFT_KEY = 37;
	private final static Point[] DIRECTIONS = { new Point(0, -1),
			new Point(-1, 0), new Point(0, 1), new Point(1, 0), };
	private final static String LABEL = "COMMAND:";
	private final int line;
	private final int nrCols;
	private final Controller ctrl;

	public Command(int line, int nrCols, Controller ctrl) {
		this.line = line;
		this.nrCols = nrCols;
		this.ctrl = ctrl;
	}

	public void print() {
		printLabel();
		for (int i = LABEL.length(); i < nrCols; i++) {
			Console.print(" ");
		}
	}

	public void print(String v) {
		printLabel();
		Console.print(v);
	}

	private void printLabel() {
		Console.color(Console.RED, Console.GRAY);
		Console.cursor(line, 0);
		Console.print(LABEL);
	}

	public void readCommand() {
		while (true) {
			print();
			Console.cursor(line, LABEL.length());
			String input = Console.nextLine(3);
			if (input == null)
				continue;
			int line = parseLine(input);
			if (line < 0) {
				if (!input.equals("")
						&& parseCommandAndCheckExitStatus(input.charAt(0)))
					return;
			} else {
				char col = input.charAt(input.length() - 1);
				if (ctrl.selectGroup(line, col)) {
					print();
					char c = Console.waitChar(5000);
					parseCommandAndCheckExitStatus(c);
					ctrl.clearSelection();
				}
			}
		}
	}

	private boolean parseCommandAndCheckExitStatus(char c) {
		while (true) {
			switch (Character.toLowerCase(c)) {
			case 'c':
				c = cursorMode();
				continue;
			case 'd':
				deleteGroup();
				return false;
			case 'e':
				return promptExit();
			case 'n':
				newGame();
				return false;
			case 'u':
				ctrl.clearSelection();
				ctrl.undo();
				return false;
			default:
				ctrl.clearSelection();
				JOptionPane.showMessageDialog(null, "Illegal command: " + c);
				return false;
			}
		}

	}

	private void deleteGroup() {
		int total = ctrl.deleteGroup();
		if (total > 0)
			JOptionPane.showMessageDialog(null,
					"Congratulations ó Palhação!!! Fizeste " + total
							+ " pontos!");
	}

	private void newGame() {
		if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null,
				"De certeza ó Palhaço?")) {
			ctrl.newGame();
		}
	}

	private boolean promptExit() {
		if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null,
				"Ó Palhaço, queres mesmo sair?")) {
			Console.close();
			return true;
		} else {
			JOptionPane.showMessageDialog(null, "Então chupa!");
			return false;
		}
	}

	private char cursorMode() {
		while (Console.isKeyPressed())
			;
		print("CURSOR");
		while (true) {
			int k = Console.waitKeyPressed(5000);
			while (Console.isKeyPressed())
				;
			int idx = k - LEFT_KEY;
			if (idx < 0 || idx >= DIRECTIONS.length)
				return (char) k;
			ctrl.move(DIRECTIONS[idx]);
		}
	}

	private static int parseLine(String s) {
		int i = 0;
		for (; i < s.length(); i++) {
			if (!Character.isDigit(s.charAt(i)))
				break;
		}
		if (i == 0) {
			// The input string does not contain a number and thus we will
			// proceed to parse command.
			return -1;
		}
		if (Character.isDigit(s.charAt(s.length() - 1))) {
			JOptionPane.showMessageDialog(null,
					"Illegal command: last character must be a letter!");
			return -1;
		}
		if (i != (s.length() - 1)) {
			JOptionPane.showMessageDialog(null,
					"Illegal command: a maximum of 2 digits and 1 letter!");
			return -1;
		}
		return Integer.parseInt(s.substring(0, s.length() - 1));
	}
}
