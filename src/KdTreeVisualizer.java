
/******************************************************************************
 *  Compilation:  javac KdTreeVisualizer.java
 *  Execution:    java KdTreeVisualizer
 *  Dependencies: KdTree.java
 *
 *  Add the points that the user clicks in the standard draw window
 *  to a kd-tree and draw the resulting kd-tree.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class KdTreeVisualizer {

	public static void main(String[] args) {
		RectHV rect = new RectHV(0.0, 0.0, 1.0, 1.0);
		StdDraw.enableDoubleBuffering();
		KdTree kdtree = new KdTree();
		buildTree(kdtree);
		StdDraw.clear();
		kdtree.draw();
		StdDraw.show();
		while (true) {
			if (StdDraw.isMousePressed()) {
				double x = StdDraw.mouseX();
				double y = StdDraw.mouseY();
				StdOut.printf("%8.6f %8.6f\n", x, y);
				Point2D p = new Point2D(x, y);
				if (rect.contains(p)) {
					StdOut.printf("%8.6f %8.6f\n", x, y);
					kdtree.insert(p);
					StdDraw.clear();
					kdtree.draw();
					StdDraw.show();
				}
			}
			StdDraw.pause(20);
		}

	}

	public static void buildTree(KdTree kdtree) {
		String filename = "./samples/input05.txt";
		In in = new In(filename);
		while (!in.isEmpty()) {
			double x = in.readDouble();
			double y = in.readDouble();
			Point2D p = new Point2D(x, y);
			kdtree.insert(p);
		}
	}
}
