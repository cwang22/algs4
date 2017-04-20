import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.ArrayList;
import java.util.List;

public class PointSET {
    private SET<Point2D> set;

    public PointSET() {
        set = new SET<>();
    }

    public boolean isEmpty() {
        return set.isEmpty();
    }

    public int size() {
        return set.size();
    }

    public void insert(Point2D p) {
        if (p == null) throw new NullPointerException();
        set.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new NullPointerException();
        return set.contains(p);
    }

    public void draw() {
        for (Point2D p : set) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new NullPointerException();
        List<Point2D> results = new ArrayList<>();
        for (Point2D point : set) {
            if (rect.contains(point)) results.add(point);
        }
        return results;
    }

    public Point2D nearest(Point2D point) {
        if (point == null) throw new NullPointerException();
        Point2D result = null;
        double distance = 0.0;
        for (Point2D p : set) {
            if (result == null) {
                result = p;
                distance = p.distanceTo(point);
            } else if (p.distanceTo(point) < distance) {
                distance = p.distanceTo(point);
                result = p;
            }
        }
        return result;
    }
}
