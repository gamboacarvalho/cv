package bubbles;

import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;

public class Controller {

	final World world;
	final Counter group;
	final Counter total;
	final Counter level;
	private SortedSet<Node> groupSelection;
	private Point selectedPoint;
	private int bustedNodes;
	private Stack<SortedSet<Node>> memento;

	public Controller(World world, Counter group, Counter total, Counter level) {
		this.world = world;
		this.group = group;
		this.total = total;
		this.level = level;
		this.memento = new Stack<>();
	}

	public void reprint() {
		world.print();
		group.print();
		total.print();
		level.print();
	}

	public void newGame() {
		clearSelection();
		level.reset();
		level.add(1);
		incLevel();
	}

	private void incLevel() {
		selectedPoint = null;
		bustedNodes = 0;
		level.add(1);
		world.build(level.getValue());
		group.reset();
		total.reset();

		world.print();
		group.print();
		total.print();
		level.print();
	}

	public int points(int nrOfNodes) {
		if (nrOfNodes == 0)
			return 0;
		int l = level.getValue();
		return nrOfNodes <= 1 ? l : points(nrOfNodes - 1) + nrOfNodes * l;
	}

	public boolean selectGroup(int line, char col) {
		Point p = parsePoint(line, col);
		if (p == null)
			return false;
		selectedPoint = p;
		return selectGroup(p);
	}

	private boolean selectGroup(Point p) {
		groupSelection = world.selectGroup(p.line, p.col);
		group.add(points(groupSelection.size()));
		group.print();
		return !groupSelection.isEmpty();
	}

	/**
	 * Returns the total points collected when all bubbles are busted.
	 * Otherwise, it returns a negative number.
	 */
	public int deleteGroup() {
		if (groupSelection == null || groupSelection.isEmpty())
			return -1;
		selectedPoint = null;
		memento.push(deepCloneReverseOrder(groupSelection));
		int nrNodes = groupSelection.size();
		bustedNodes += nrNodes;
		group.reset();
		total.add(points(nrNodes));
		total.print();
		group.print();
		for (Iterator<Node> iter = groupSelection.iterator(); iter.hasNext();) {
			world.blowNode(iter.next());
			iter.remove();
		}
		int res = -1;
		if (bustedNodes == (world.nrCols * world.nrLines)) {
			res = total.getValue();
			incLevel();
		}
		world.print();
		return res;
	}

	private Point parsePoint(int line, char col) {
		if (line > world.nrLines) {
			return null;
		}
		int x = Character.toUpperCase(col) - 'A';
		if (x >= world.nrCols) {
			return null;
		}
		return new Point(line - 1, x);
	}

	public void clearSelection() {
		if (selectedPoint != null) {
			world.getNode(selectedPoint).unselect();
			selectedPoint = null;
		}
		if (groupSelection == null)
			return;
		for (Iterator<Node> iter = groupSelection.iterator(); iter.hasNext();) {
			iter.next().unselect();
			iter.remove();
		}
		group.reset();
	}

	public void move(Point p) {
		Point curr = selectedPoint;
		clearSelection();
		curr = curr != null ? curr : new Point(0, 0);
		selectedPoint = curr.move(p);
		if (world.invalid(selectedPoint)) {
			selectedPoint = curr;
		}
		selectGroup(selectedPoint);
		group.reset();
		group.add(points(groupSelection.size()));
		group.print();
	}

	public boolean undo() {
		if (memento.isEmpty())
			return false;
		selectedPoint = null;
		groupSelection = memento.pop();
		int nrNodes = groupSelection.size();
		bustedNodes -= nrNodes;
		group.reset();
		total.add(-points(nrNodes));
		total.print();
		group.print();
		for (Iterator<Node> iter = groupSelection.iterator(); iter.hasNext();) {
			Node n = iter.next();
			world.pushNode(n);
			iter.remove();
		}
		world.print();
		return true;
	}

	private static SortedSet<Node> deepCloneReverseOrder(SortedSet<Node> src) {
		SortedSet<Node> res = new TreeSet<Node>(new Comparator<Node>() {
			@Override
			public int compare(Node arg0, Node arg1) {
				return -arg0.compareTo(arg1);
			}
		});
		res.addAll(src);
		return res;
	}
}
