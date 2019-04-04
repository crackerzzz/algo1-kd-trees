import java.util.TreeSet;

import edu.princeton.cs.algs4.Point2D;

public class TreeSetTest {
	public static void main(String[] args) {
		TreeSet<Point2D> tree = new TreeSet<>();

		for (int i = 0; i < 10; i++) {
			tree.add(new Point2D(Math.round(Math.random() * 100) / 10, Math.round(Math.random() * 100) / 10));
		}

//		for (int i = 0; i < 10; i++) {
//			tree.add(new Point2D(i, i));
//		}

//		tree.add(new Point2D(1.0, 1.0));
//		tree.add(new Point2D(2.0, 2.0));
//		tree.add(new Point2D(5.0, 5.0));

		System.out.println("tree: " + tree);

		System.out.println("submap: " + tree.subSet(new Point2D(0.0, 1.0), true, new Point2D(3.0, 3.0), true));

	}
}
