import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FastCollinearPoints {
    private List<LineSegment> ls = new ArrayList<>();

    public FastCollinearPoints(Point[] originalPoints) {
        if (originalPoints == null) throw new NullPointerException();

        //clone the input array so it's immutable
        Point[] points = originalPoints.clone();

        //check if their is null or duplicated points
        Arrays.sort(points);
        validate(points);

        if (points.length < 4) return;

        Point[] copy = points.clone();
        for (int i = 0; i < points.length - 3; i++) {
            Point p = points[i];
            Arrays.sort(copy, p.slopeOrder());
            List<Point> current = new ArrayList<>();
            current.add(p);
            current.add(copy[1]);
            double slope = p.slopeTo(copy[1]);

            for (int j = 2; j < copy.length; j++) {
                double currentSlope = p.slopeTo(copy[j]);

                if (currentSlope == slope) {
                    current.add(copy[j]);
                } else {
                    if (current.size() > 3) {
                        addSegment(current, p);
                    }
                    current.clear();
                    current.add(p);
                    current.add(copy[j]);
                    slope = currentSlope;
                }
            }

            if (current.size() > 3) addSegment(current, p);
        }
    }

    public int numberOfSegments() {
        return ls.size();
    }

    public LineSegment[] segments() {
        return ls.toArray(new LineSegment[ls.size()]);
    }

    private void addSegment(List<Point> current, Point p) {
        Collections.sort(current);
        Point min = current.get(0);

        /* All line segments not started with p are duplicated ones or subsegments
         * this avoids using extra memory to store endpoints/slopes or time to loop through them
         */
        if (!min.equals(p)) return;

        Point max = current.get(current.size() - 1);
        ls.add(new LineSegment(min, max));
    }

    private void validate(Point[] points) {
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i] == null || points[i + 1] == null) throw new NullPointerException();
            if (points[i].compareTo(points[i + 1]) == 0) throw new IllegalArgumentException();
        }
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];

        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);

        for (Point p : points) {
            p.draw();
        }

        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);

        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        
        StdDraw.show();
    }
}
