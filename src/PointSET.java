import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class PointSET {
	public static final Point2D MIN = new Point2D(0.0, 0.0);
	public static final Point2D MAX = new Point2D(1.0, 1.0);
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
		return findRange(rect);
	}

	private Iterable<Point2D> findRange(RectHV rect) {
		final Set<Point2D> range = new HashSet<>();

		final Iterator<Point2D> iter = bst.iterator();
		while (iter.hasNext()) {
			final Point2D next = iter.next();
			if (next.y() >= rect.ymin() && next.y() <= rect.ymax() && next.x() >= rect.xmin()
					&& next.x() <= rect.xmax()) {
				range.add(next);
			}
		}
		return range;
	}

	// a nearest neighbor in the set to point p; null if the set is empty
	public Point2D nearest(Point2D p) {
		if (p == null) {
			throw new IllegalArgumentException();
		}
		return findNearest(p);
	}

	private Point2D findNearest(Point2D p) {
		double distance = Double.MAX_VALUE;
		Point2D nearest = null;
		final Iterator<Point2D> iter = bst.iterator();
		while (iter.hasNext()) {
			Point2D next = iter.next();
			double temp = p.distanceSquaredTo(next);
			if (temp < distance) {
				distance = temp;
				nearest = next;
			}
		}
		return nearest;
	}

	// unit testing of the methods (optional)
	public static void main(String[] args) {
		String filename = "./samples/input10.txt";
		In in = new In(filename);
		PointSET brute = new PointSET();
		while (!in.isEmpty()) {
			double x = in.readDouble();
			double y = in.readDouble();
			Point2D p = new Point2D(x, y);
			brute.insert(p);
		}

//		System.out.println("nearest to " + MIN + ": " + brute.nearest(MIN));
//		System.out.println("nearest to " + MIN + ": " + brute.nearest(MAX));
//
//		Point2D exact = new Point2D(0.226, 0.577);
//		System.out.println("nearest to " + exact + ": " + brute.nearest(exact));
//
//		Point2D t1 = new Point2D(0.226, 0.567);
//		System.out.println("nearest to " + t1 + ": " + brute.nearest(t1));
//
//		Point2D t2 = new Point2D(0.216, 0.577);
//		System.out.println("nearest to " + t2 + ": " + brute.nearest(t2));
//
//		Point2D t3 = new Point2D(0.905, 0.017);
//		System.out.println("nearest to " + t3 + ": " + brute.nearest(t3));

		Iterable<Point2D> range = brute.range(new RectHV(0, 0, 0.15, 0.18));
		System.out.println("range: " + range);
	}
}