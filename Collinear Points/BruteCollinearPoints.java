import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {
    private LineSegment[] segments;

    public BruteCollinearPoints(Point[] originalPoints) {
        if (originalPoints == null) throw new java.lang.NullPointerException();
        Point[] points = originalPoints.clone();
        Arrays.sort(points);
        validate(points);
        List<LineSegment> ls = new ArrayList<>();
        for (int i = 0; i < points.length - 3; i++) {
            for (int j = i + 1; j < points.length - 2; j++) {
                double sij = points[i].slopeTo(points[j]);
                for (int k = j + 1; k < points.length - 1; k++) {
                    double sik = points[i].slopeTo(points[k]);
                    if (sij != sik) continue;
                    for (int l = k + 1; l < points.length; l++) {
                        double sil = points[i].slopeTo(points[l]);
                        if (sij != sil) continue;
                        ls.add(new LineSegment(points[i], points[l]));
                    }
                }
            }
        }
        segments = ls.toArray(new LineSegment[ls.size()]);
    }

    private void validate(Point[] points) {
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i] == null || points[i + 1] == null) throw new NullPointerException();
            if (points[i].compareTo(points[i + 1]) == 0) throw new IllegalArgumentException();
        }
    }

    public int numberOfSegments() {
        return segments.length;
    }

    public LineSegment[] segments() {
        return segments.clone();
    }

    public static void main(String[] args) {

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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
