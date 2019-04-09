import java.awt.Color;
import java.util.Comparator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
	public static final double xmin = 0.0, ymin = 0.0; // initial endpoint of rectangle
	public static final double xmax = 1.0, ymax = 1.0; // current location of mouse

	private Node root;
	private int count = 0;
	private boolean vertical = true;

	// construct an empty set of points
	public KdTree() {
	}

	// is the set empty?
	public boolean isEmpty() {
		return count == 0;
	}

	// number of points in the set
	public int size() {
		return count;
	}

	// add the point to the set (if it is not already in the set)
	public void insert(Point2D p) {
		if (p == null) {
			throw new IllegalArgumentException();
		}
		root = traverse(root, p, vertical);
		count++;
		vertical = !vertical;
	}

	private Node traverse(Node node, Point2D point, boolean vertical) {
		if (node == null) {
			node = new Node(point, vertical);
		} else {
			int cmp = findComparator(node.vertical).compare(point, node.point);
			// if given value is less than root, go left else go right
			if (cmp < 0) {
				node.left = traverse(node.left, point, !node.vertical);
			} else {
				node.right = traverse(node.right, point, !node.vertical);
			}
		}
		return node;
	}

	private Comparator<Point2D> findComparator(boolean vertical) {
		if (vertical) {
			return Point2D.X_ORDER;
		} else {
			return Point2D.Y_ORDER;
		}
	}

	// does the set contain point p?
	public boolean contains(Point2D p) {
		if (p == null) {
			throw new IllegalArgumentException();
		}
		Node current = root;
		while (current != null) {
			if (p.compareTo(current.point) == 0) {
				return true;
			}
			int cmp = findComparator(current.vertical).compare(p, current.point);
			if (cmp < 0) {
				current = current.left;
			} else {
				current = current.right;
			}
		}
		return false;
	}

	// draw all points to standard draw
	public void draw() {
		draw(root, null, true);
	}

	private void draw(Node node, Node parent, boolean less) {
		if (node == null) {
			return;
		}
		drawLine(node, parent, less);
		drawPoint(node.point);
		StdDraw.show();

		draw(node.left, node, true);
		draw(node.right, node, false);
	}

	private void drawLine(Node current, Node parent, boolean less) {
		final Point2D from;
		final Point2D to;
		if (parent == null) {
			// first is always vertical
			from = new Point2D(current.point.x(), ymin);
			to = new Point2D(current.point.x(), ymax);
		} else {
			if (less) {
				// draw to left
				if (current.vertical) {
					from = new Point2D(current.point.x(), ymin);
					to = new Point2D(current.point.x(), parent.point.y());
				} else {
					from = new Point2D(xmin, current.point.y());
					to = new Point2D(parent.point.x(), current.point.y());
				}
			} else {
				// draw to right
				if (current.vertical) {
					from = new Point2D(current.point.x(), parent.point.y());
					to = new Point2D(current.point.x(), ymax);
				} else {
					from = new Point2D(parent.point.x(), current.point.y());
					to = new Point2D(xmax, current.point.y());
				}
			}
		}
		drawLine(from, to, current.vertical ? Color.RED : Color.BLUE);
	}

	private void drawLine(Point2D from, Point2D to, Color color) {
		StdDraw.setPenRadius(0.0001);
		StdDraw.setPenColor(color);
		StdDraw.line(from.x(), from.y(), to.x(), to.y());
	}

	private void drawPoint(Point2D point) {
		StdDraw.setPenRadius(0.01);
		StdDraw.setPenColor(StdDraw.BLACK);
		point.draw();
	}

	// all points that are inside the rectangle (or on the boundary)
	public Iterable<Point2D> range(RectHV rect) {
		if (rect == null) {
			throw new IllegalArgumentException();
		}
		return null;
	}

	// a nearest neighbor in the set to point p; null if the set is empty
	public Point2D nearest(Point2D p) {
		if (p == null) {
			throw new IllegalArgumentException();
		}
		return null;
	}

	private class Node {
		private final Point2D point;
		private final boolean vertical;
		private Node left, right;

		public Node(Point2D point, boolean vertical) {
			this.point = point;
			this.vertical = vertical;
		}

		@Override
		public String toString() {
			return "Node [point=" + point + ", vertical=" + vertical + ", left=" + left + ", right=" + right + "]";
		}
	}

	// unit testing of the methods (optional)
	public static void main(String[] args) {
		String filename = "./samples/input10.txt";
		In in = new In(filename);
		KdTree kdTree = new KdTree();
		while (!in.isEmpty()) {
			double x = in.readDouble();
			double y = in.readDouble();
			Point2D p = new Point2D(x, y);
			kdTree.insert(p);
		}
		System.out.println("nearest to (0,0) : " + kdTree);
	}

}
