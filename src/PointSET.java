import java.util.TreeSet;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class PointSET {
	private final TreeSet<Point2D> bst;

	// construct an empty set of points
	public PointSET() {
		this.bst = new TreeSet<>();
	}

	// is the set empty?
	public boolean isEmpty() {
		return bst.isEmpty();
	}

	// number of points in the set
	public int size() {
		return bst.size();
	}

	// add the point to the set (if it is not already in the set)
	public void insert(Point2D p) {
		if (p == null) {
			throw new IllegalArgumentException();
		}
		bst.add(p);
	}

	// does the set contain point p?
	public boolean contains(Point2D p) {
		if (p == null) {
			throw new IllegalArgumentException();
		}
		return bst.contains(p);
	}

	// draw all points to standard draw
	public void draw() {
		bst.stream()
			.forEach(p -> p.draw());
	}

	// all points that are inside the rectangle (or on the boundary)
	public Iterable<Point2D> range(RectHV rect) {
		if (rect == null) {
			throw new IllegalArgumentException();
		}
		return bst.subSet(new Point2D(rect.xmin(), rect.ymin()), true, new Point2D(rect.xmax(), rect.ymax()), true);
	}

	// a nearest neighbor in the set to point p; null if the set is empty
	public Point2D nearest(Point2D p) {
		if (p == null) {
			throw new IllegalArgumentException();
		}
		final Point2D nearest = findNearest(p);
		System.out.println("nearest neighbour for " + p + " is " + nearest);
		return nearest;
	}

	private Point2D findNearest(Point2D p) {
		final Point2D floor = bst.floor(p);
		return floor != null ? floor : bst.ceiling(p);
	}

	// unit testing of the methods (optional)
	public static void main(String[] args) {

	}
}