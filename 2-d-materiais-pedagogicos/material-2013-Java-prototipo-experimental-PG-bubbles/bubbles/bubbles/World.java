package bubbles;

import isel.leic.pg.Console;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class World {
	private final Node[][] nodes;
	final int nrLines;
	final int nrCols;
	final Point origin;
	int level;

	public World(Point origin, int nrLines, int nrCols, int level) {
		this.origin = origin;
		this.nrLines = nrLines;
		this.nrCols = nrCols;
		this.nodes = new Node[nrLines][nrCols];
		build(level);
	}

	public void build(int level) {
		this.level = level;
		for (int i = 0; i < nodes.length; i++) {
			for (int j = 0; j < nodes[i].length; j++) {
				nodes[i][j] = new Node(origin, i, j, level);
				// For debug purpose:
				// nodes[i][j] = new Node(origin, i, j, level, i%2 == 0?
				// Console.RED: Console.BLUE);
				// nodes[i][j] = new Node(origin, i, j, level, Console.BLUE);
			}
		}
	}

	public void print() {
		for (Node[] elems : nodes) {
			for (Node node : elems) {
				node.print();
			}
		}
		/*
		 * System.out.println(
		 * "==========================================================="); for
		 * (Node[] elems : nodes) { for (Node node : elems) {
		 * System.out.print(node); } System.out.println(); }
		 */
	}

	public void blowNode(Node n) {
		for (int l = n.line(); l > 0; l--) {
			nodes[l][n.col] = nodes[l - 1][n.col];
			nodes[l][n.col].lineInc();
			nodes[l][n.col].unselect();
		}
		nodes[0][n.col] = new Node(origin, 0, n.col, level, Console.WHITE);
	}

	public void pushNode(Node n) {
		for (int l = 1; l <= n.line(); l++) {
			nodes[l - 1][n.col] = nodes[l][n.col];
			nodes[l - 1][n.col].lineDec();
			nodes[l - 1][n.col].unselect();
		}
		nodes[n.line()][n.col] = n;
		n.unselect();
	}

	public SortedSet<Node> selectGroup(int line, int col) {
		Node n = nodes[line][col];
		SortedSet<Node> marked = new TreeSet<Node>();
		n.select();
		if (n.isEmptNode())
			return marked;
		marked.add(n);
		checkAround(line, col, n.defaultColor, marked);
		return marked;
	}

	private void checkAround(int line, int col, int target, Set<Node> marked) {
		checkLine(line, col, target, marked);
		checkCol(line, col, target, marked);
	}

	private void checkLine(int line, int col, int target, Set<Node> marked) {
		for (int l = -1; l <= 1; l += 2) {
			if (invalidLine(line + l))
				continue;
			Node n = nodes[line + l][col];
			if (!marked.contains(n) && n.inGroup(target)) {
				marked.add(n);
				checkAround(line + l, col, target, marked);
			}
		}
	}

	private void checkCol(int line, int col, int target, Set<Node> marked) {
		for (int c = -1; c <= 1; c += 2) {
			if (invalidCol(col + c))
				continue;
			Node n = nodes[line][col + c];
			if (!marked.contains(n) && n.inGroup(target)) {
				marked.add(n);
				checkAround(line, col + c, target, marked);
			}
		}
	}

	public boolean invalid(Point p) {
		return invalidLine(p.line) || invalidCol(p.col);
	}

	private boolean invalidLine(int l) {
		return l < 0 || l >= nrLines;
	}

	private boolean invalidCol(int c) {
		return c < 0 || c >= nrCols;
	}

	public Node getNode(Point p) {
		return nodes[p.line][p.col];
	}
}
