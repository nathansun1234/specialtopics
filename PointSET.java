import java.util.Set;
import java.util.TreeSet;
import java.util.List;
import java.util.LinkedList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class PointSET {
    
    private final Set<Point2D> points;

    // construct an empty set of points 
    public PointSET() {
        points = new TreeSet<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of poitns in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        else if (!points.contains(p)) {
            points.add(p);
        }
    }

    // does the set contain point
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }

        return points.contains(p);
    }

    // draw all the points to standard draw
    public void draw() {
        for (Point2D point : points) {
            point.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }

        List<Point2D> result = new LinkedList<>();
        for (Point2D point : points) {
            if (rect.contains(point)) {
                result.add(point);
            }
        }

        return result;

    }

    //a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        
        if (points.isEmpty()) {
            return null;
        }

        Point2D nearestpoint = new Point2D(-1 * Double.MAX_VALUE, -1 * Double.MAX_VALUE);

        for (Point2D point : points) {
            if (point.distanceTo(p) < nearestpoint.distanceTo(p)) {
                nearestpoint = point;
            }
        }

        return nearestpoint;
    }
}