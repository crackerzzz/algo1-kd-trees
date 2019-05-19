import java.awt.Color;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
	public static final double xmin = 0.0, ymin = 0.0; // initial endpoint of rectangle
	public static final double xmax = 1.0, ymax = 1.0; // current location of mouse

	private Node root;
	private int count;

	// construct an empty set of points
	public KdTree() {
		this.count = 0;
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
		// handle root case
		if (root == null) {
			root = new Node(p, new RectHV(xmin, ymin, xmax, ymax), true);
		} else {
			root = traverse(null, root, p);
		}
		count++;
	}

	private Node traverse(Node parent, Node node, Point2D point) {
		// handle new nodes
		if (node == null) {
			final RectHV rect = buildContainingRect(parent, point, parent.vertical);
			node = new Node(point, rect, !parent.vertical);
		} else {
			int cmp = findComparator(node.vertical).compare(point, node.point);
			if (cmp == 0) {
				return node;
			}
			// if given value is less than node, go left else go right
			else if (cmp < 0) {
				node.left = traverse(node, node.left, point);
			} else {
				node.right = traverse(node, node.right, point);
			}
		}
		return node;
	}

	private RectHV buildContainingRect(Node parent, Point2D p, boolean vertical) {
		if (vertical) {
			// find whether point is left or right of parent
			// point is to left if x is smaller than parent
			if (p.x() < parent.point.x()) {
				return new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.point.x(), parent.rect.ymax());
			} else {
				return new RectHV(parent.point.x(), parent.rect.ymin(), parent.rect.xmax(), parent.rect.ymax());
			}
		} else {
			// find whether point is top or bottom of parent
			// point is to bottom if y is smaller than parent
			if (p.y() < parent.point.y()) {
				return new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.rect.xmax(), parent.point.y());
			} else {
				return new RectHV(parent.rect.xmin(), parent.point.y(), parent.rect.xmax(), parent.rect.ymax());
			}
		}
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
		draw(root);
	}

	private void draw(Node node) {
		if (node == null) {
			return;
		}
		drawLine(node);
		drawPoint(node.point, node.vertical);
		StdDraw.show();

		draw(node.left);
		draw(node.right);
	}

	private void drawLine(Node current) {
		final Point2D from;
		final Point2D to;
		if (current.vertical) {
			from = new Point2D(current.point.x(), current.rect.ymin());
			to = new Point2D(current.point.x(), current.rect.ymax());
		} else {
			from = new Point2D(current.rect.xmin(), current.point.y());
			to = new Point2D(current.rect.xmax(), current.point.y());
		}
		drawLine(from, to, current.vertical ? Color.RED : Color.BLUE);
	}

	private void drawLine(Point2D from, Point2D to, Color color) {
		StdDraw.setPenRadius(0.0001);
		StdDraw.setPenColor(color);
		StdDraw.line(from.x(), from.y(), to.x(), to.y());
	}

	private void drawPoint(Point2D point, boolean vertical) {
		StdDraw.setPenRadius(0.01);
		StdDraw.setPenColor(vertical ? StdDraw.BLACK : StdDraw.BLUE);
		point.draw();
	}

	// all points that are inside the rectangle (or on the boundary)
	public Iterable<Point2D> range(RectHV rect) {
		if (rect == null) {
			throw new IllegalArgumentException();
		}
		final Set<Point2D> points = new HashSet<>();
		range(root, rect, points);
		return points;
	}

	private void range(Node node, RectHV rect, Set<Point2D> points) {
		if (node == null) {
			return;
		} else {
			if (node.rect.intersects(rect)) {
				if (rect.contains(node.point)) {
					points.add(node.point);
				}
				range(node.left, rect, points);
				range(node.right, rect, points);
			}
		}
	}

	// a nearest neighbor in the set to point p; null if the set is empty
	public Point2D nearest(Point2D p) {
		if (p == null) {
			throw new IllegalArgumentException();
		}
		return findNearest(root, p);
	}

	private Point2D findNearest(Node node, Point2D p) {
		Nearest nearest = new Nearest();
		findNearest(node, p, nearest);
//		System.out.println("Nearest: " + nearest.p);
		return nearest.p;
	}

	private void findNearest(Node node, Point2D p, Nearest nearest) {
		if (node == null) {
			return;
		}

		if (nearest.p == null) {
			nearest.p = node.point;
			nearest.distance = p.distanceSquaredTo(node.point);
		} else {
			final double dist = p.distanceSquaredTo(node.point);
			if (p.distanceSquaredTo(node.point) < nearest.distance) {
				nearest.p = node.point;
				nearest.distance = dist;
			}
		}

		final Node nodeToTraverseFirst;
		final Node nodeToTraverseNext;
		if (node.vertical) {
			if (p.x() <= node.point.x()) {
				nodeToTraverseFirst = node.left;
				nodeToTraverseNext = node.right;
			} else {
				nodeToTraverseFirst = node.right;
				nodeToTraverseNext = node.left;
			}
		} else {
			if (p.y() <= node.point.y()) {
				nodeToTraverseFirst = node.left;
				nodeToTraverseNext = node.right;
			} else {
				nodeToTraverseFirst = node.right;
				nodeToTraverseNext = node.left;
			}
		}
		findNearest(nodeToTraverseFirst, p, nearest);
		if (nodeToTraverseNext != null
				&& nodeToTraverseNext.rect.distanceSquaredTo(p) < nearest.p.distanceSquaredTo(p)) {
			findNearest(nodeToTraverseNext, p, nearest);
		}
	}

	private static class Node {
		private final Point2D point;
		private final boolean vertical;
		private final RectHV rect;
		private Node left, right;

		public Node(Point2D point, RectHV rect, boolean vertical) {
			this.point = point;
			this.rect = rect;
			this.vertical = vertical;
		}

		@Override
		public String toString() {
			return "Node [point=" + point + ", vertical=" + vertical + ", rect=" + rect + ", left=" + left + ", right="
					+ right + "]";
		}
	}

	private static class Nearest {
		private Point2D p;
		private double distance;
	}

	@Override
	public String toString() {
		return "KdTree [root=" + root + ", count=" + count + "]";
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

//		KdTree kdTree = new KdTree();
//		kdTree.insert(new Point2D(0.5, 0.4));
//		kdTree.insert(new Point2D(0.2, 0.6));
//		kdTree.insert(new Point2D(0.7, 0.4));
//
//		kdTree.insert(new Point2D(0.1, 0.8));
//		kdTree.insert(new Point2D(0.3, 0.3));
//
//		kdTree.insert(new Point2D(0.8, 0.8));
//		kdTree.insert(new Point2D(0.1, 0.6));
//
//		System.out.println("kdTree : " + kdTree);
	}

}
