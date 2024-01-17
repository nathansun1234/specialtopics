import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

    // inner class representing a node in the KD-tree
    private static class Node {
        public final Point2D point;
        public Node left;
        public Node right;
        public int level;

        // node constructor
        public Node(Point2D point, Node left, Node right, int level) {
            this.point = point;
            this.left = left;
            this.right = right;
            this.level = level;
        }
    }

    private Node root; // root of the tree
    private int size;   // number of points in the tree

    // construct an empty set of points 
    public KdTree() {
        size = 0;
        // we don't create a root because then it wouldn't be empty
    }

    // check if the set is empty
    public boolean isEmpty() {
        return size() == 0;
    }

    // get the number of points in the set
    public int size() {
        return size;
    }

    // add a point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        else if (isEmpty()) {
            // create a new root if we need one because the tree is empty
            root = new Node(p, null, null, 0);
            size++;
        }
        else if (!contains(p)) {
            // call helper method for insertion
            inserthelper(root, p);
            size++;
        }
    }

    // helper method for point insertion
    private void inserthelper(Node node, Point2D p) {
        Comparator<Point2D> comparator;

        // choose comparator based on the current level (X_ORDER or Y_ORDER)
        if (node.level % 2 == 0) {
            comparator = Point2D.X_ORDER;
        }
        else {
            comparator = Point2D.Y_ORDER;
        }

        // compare and insert the point recursively using our comparator
        if (comparator.compare(node.point, p) > 0) { // if the point is smaller, then we go left
            if (node.left == null) { // if theres no more points to the left then we put our point there
                node.left = new Node(p, null, null, node.level + 1);
            }
            else { // if there are then we keep going
                inserthelper(node.left, p);
            }
        }
        // if the point is bigger, then we go right
        else { // if theres no more points to the right then we put our point there
            if (node.right == null) {
                node.right = new Node(p, null, null, node.level + 1);
            }
            else { // if there are then we keep going
                inserthelper(node.right, p);
            }
        }
    }

    // check if the set contains a specific point
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        else if (isEmpty()) {
            return false;
        }
        else {
            // call helper method for point containment check
            return containshelper(root, p);
        }
    }

    // helper method for point containment check
    private boolean containshelper(Node root, Point2D p) {
        Comparator<Point2D> comparator;

        // choose comparator based on the current level (X_ORDER or Y_ORDER)
        if (root.level % 2 == 0) {
            comparator = Point2D.X_ORDER;
        }
        else {
            comparator = Point2D.Y_ORDER;
        }

        // compare recursively and check containment
        if (root.point.equals(p)) { // if its equal then we found it!
            return true;
        }
        else if (comparator.compare(root.point, p) > 0) { // if its too big then we look to the left
            if (root.left != null) {
                return containshelper(root.left, p);
            }
            else { // we know weve hit the min if theres no more points to the left, at this point we can say that it doesnt contain it
                return false;
            }
        }
        else {
            if (root.right != null) { // if its too small then we look to the right
                return containshelper(root.right, p);
            }
            else { // we know weve hit the max if theres no more points to the right, at this point we can say that it doesnt contain it
                return false;
            }
        }
    }

    // draw all the points to standard draw
    public void draw() {
        drawhelper(root);
    }

    // helper method for drawing points recursively
    private void drawhelper(Node root) {
        if (root != null) { // this is to check if we're still in the range of the points, if its ever null we've hit the smallest or largest
            StdDraw.point(root.point.x(), root.point.y());
            drawhelper(root.left); // draw all the points to the left
            drawhelper(root.right); // draw all the points to the right
        }
        else { // when we hit the edge of the tree we start going back up the recursion loop
            return;
        }
    }

    // get all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        else {
            // call helper method for range search
            return rangehelper(root, rect);
        }
    }

    // helper method for range search
    private List<Point2D> rangehelper(Node root, RectHV rect) {
        // base case: if the current node is null, return up an empty list
        if (root == null) {
            return Collections.emptyList();
        }

        // check if the level is even or odd to decide whether to compare by x or y
        if (root.level % 2 == 0) {
            // if the current node's x-coordinate is within the rectangle's x-range
            if (root.point.x() >= rect.xmin() && root.point.x() <= rect.xmax()) {
                List<Point2D> current = new ArrayList<>();

                // check if the current node is within the rectangle and add it to the list
                if (rect.contains(root.point)) {
                    current.add(root.point);
                }

                // recursively search both left and right subtrees, adding all the points we find to our list
                current.addAll(rangehelper(root.left, rect));
                current.addAll(rangehelper(root.right, rect));

                return current;
            }
            else {
                // if the current node isnt in the rectangle's x range, we search either left or right depending which way it is
                if (root.point.x() > rect.xmax()) {
                    return rangehelper(root.left, rect);
                }
                else {
                    return rangehelper(root.right, rect);
                }
            }
        }
        else { // level is odd, so compare by Y-coordinate
            // same logic as before
            if (root.point.y() >= rect.ymin() && root.point.y() <= rect.ymax()) {
                List<Point2D> current = new ArrayList<>();

                if (rect.contains(root.point)) {
                    current.add(root.point);
                }

                current.addAll(rangehelper(root.left, rect));
                current.addAll(rangehelper(root.right, rect));

                return current;
            }
            else {

                if (root.point.y() > rect.ymax()) {
                    return rangehelper(root.left, rect);
                }
                else {
                    return rangehelper(root.right, rect);
                }
            }
        }
    }

    // get the nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (root == null) {
            return null;
        }
        return nearest(null, p, this.root, new RectHV(0, 0, 1, 1), Double.POSITIVE_INFINITY);
        // for the initial rectangle 0 0 1 1 is suficcient bc all the points it tests are decimals in the unit square
    }

    // helper method for nearest point
    private Point2D nearest(Point2D nearest, Point2D p, Node root, RectHV rect, double bestdistance) {
        if (root == null) {
            return nearest;
        }

        double currentdistance = root.point.distanceSquaredTo(p);

        // set new best if current is closer
        if (bestdistance > currentdistance) {
            bestdistance = currentdistance;
            nearest = root.point;
        }

        if (root.level % 2 == 0) { // if its even we go horizontally
            // define the search area rectangles
            // x vals: from the min x we pass in to the current x, and from the current x to the max x we pass in
            // y vals: same as we passed in
            // this way, as the loop recurses, the area gets smaller and smaller
            RectHV left = new RectHV(rect.xmin(), rect.ymin(), root.point.x(), rect.ymax());
            RectHV right = new RectHV(root.point.x(), rect.ymin(), rect.xmax(), rect.ymax());

            if (p.x() < root.point.x()) { // if the target point is to the left of the current point
                nearest = nearest(nearest, p, root.left, left, bestdistance); // therefore we search the rectangle to the left using the left point to the left to start

                // look at the other tree ONLY if its possible that it contains a closer point
                if (p.distanceSquaredTo(nearest) >= right.distanceSquaredTo(p)) {
                    nearest = nearest(nearest, p, root.right, right, p.distanceSquaredTo(nearest));
                }
            }
            else { // otherwise the target point is to the right of the current point
                nearest = nearest(nearest, p, root.right, right, bestdistance); // we search the rectangle to the right using the next point to the right to start
                
                // look at the other tree ONLY if its possible that it contains a closer point
                if (p.distanceSquaredTo(nearest) >= left.distanceSquaredTo(p)) {
                    nearest = nearest(nearest, p, root.left, left, p.distanceSquaredTo(nearest));
                }
            } 
        }
        else { // if its odd we go vertical. rest of loop uses same logic as before
            RectHV below = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), root.point.y());
            RectHV above = new RectHV(rect.xmin(), root.point.y(), rect.xmax(), rect.ymax());

            if (p.y() < root.point.y()) {
                nearest = nearest(nearest, p, root.left, below, bestdistance);

                if (p.distanceSquaredTo(nearest) >= above.distanceSquaredTo(p)) {
                    nearest= nearest(nearest, p, root.right, above, p.distanceSquaredTo(nearest));
                } 
            }
            else {
                nearest = nearest(nearest, p, root.right, above, bestdistance);

                if (p.distanceSquaredTo(nearest) >= below.distanceSquaredTo(p)) {
                    nearest = nearest(nearest, p, root.left, below, p.distanceSquaredTo(nearest));
                }
            }
        }

        return nearest;
    }
}