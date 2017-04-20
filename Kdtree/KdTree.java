import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class KdTree {
    private Node root;
    private int size;

    private double distance;
    private Point2D nearest;

    private class Node {
        private Point2D key;
        private Node left;
        private Node right;
        private RectHV rect;
        boolean isVertical;

        private Node(Point2D key, Node parent) {
            this.key = key;

            if (parent == null) {
                this.isVertical = true;
                this.rect = new RectHV(0, 0, 1, 1);
            } else {
                this.isVertical = !parent.isVertical;
                if (parent.compareTo(key) > 0) {
                    if (parent.isVertical) {
                        this.rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.rect.xmax(), parent.key.y());
                    } else {
                        this.rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.key.x(), parent.rect.ymax());
                    }
                } else {
                    if (parent.isVertical) {
                        this.rect = new RectHV(parent.rect.xmin(), parent.key.y(), parent.rect.xmax(), parent.rect.ymax());
                    } else {
                        this.rect = new RectHV(parent.key.x(), parent.rect.ymin(), parent.rect.xmax(), parent.rect.ymax());
                    }
                }
            }
        }

        private int compareTo(Point2D p) {
            if (key.equals(p)) return 0;
            if (isVertical) {
                if (key.y() < p.y()) return -1;

                return 1;
            } else {

                if (key.x() < p.x()) return -1;
                return 1;
            }
        }

        private void draw() {
            key.draw();
            if (isVertical) {
                StdDraw.setPenColor(Color.BLUE);
                StdDraw.line(rect.xmin(), key.y(), rect.xmax(), key.y());
            } else {
                StdDraw.setPenColor(Color.RED);
                StdDraw.line(key.x(), rect.ymin(), key.x(), rect.ymax());
            }
            StdDraw.setPenColor();
            if (left != null) left.draw();
            if (right != null) right.draw();
        }
    }

    public KdTree() {
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D p) {
        if (p == null) throw new NullPointerException();
        root = insert(root, p, null);
    }

    private Node insert(Node node, Point2D p, Node parent) {
        if (node == null) {
            size++;
            return new Node(p, parent);
        } else {
            if (node.compareTo(p) > 0) {
                node.left = insert(node.left, p, node);
            } else if (node.compareTo(p) < 0) {
                node.right = insert(node.right, p, node);
            }
            return node;
        }
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new NullPointerException();
        return contains(root, p);
    }

    private boolean contains(Node node, Point2D p) {
        if (p == null) throw new NullPointerException();
        if (node == null) return false;
        if (node.compareTo(p) == 1)
            return contains(node.left, p);
        else
            return (node.compareTo(p) != -1) || contains(node.right, p);
    }

    public void draw() {
        if (root != null) root.draw();
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new NullPointerException();
        List<Point2D> list = new ArrayList<>();
        range(list, root, rect);
        return list;

    }

    private void range(List<Point2D> list, Node node, RectHV rect) {
        if (node == null) return;
        if (!node.rect.intersects(rect)) return;
        if (rect.contains(node.key)) list.add(node.key);
        range(list, node.left, rect);
        range(list, node.right, rect);
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new NullPointerException();
        nearest = null;
        distance = Double.MAX_VALUE;

        nearest(root, p);
        return nearest;
    }

    private void nearest(Node node, Point2D p) {
        if (node == null) return;
        if (distance < node.rect.distanceSquaredTo(p)) return;
        double currentDistance = node.key.distanceSquaredTo(p);
        if (currentDistance < distance) {
            nearest = node.key;
            distance = currentDistance;
        }

        //search closer side first
        if (node.compareTo(p) > 0) {
            nearest(node.left, p);
            nearest(node.right, p);
        } else {
            nearest(node.right, p);
            nearest(node.left, p);
        }

    }
}
