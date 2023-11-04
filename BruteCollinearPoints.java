import java.util.ArrayList;

public class BruteCollinearPoints {
    // finds all line segments containing 4 points
    private Point[] points;
    private ArrayList<LineSegment> segments;

    public BruteCollinearPoints(Point[] pts) {
        points = pts;

        if (points == null) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
        }

        if (points.length > 1) {
            for (int i = 0; i < points.length; i++) {
                for (int j = i + 1; j < points.length; j++) {
                    if (points[i].compareTo(points[j]) == 0) {
                        throw new IllegalArgumentException();
                    }
                }
            }
        }

        segments = new ArrayList<LineSegment>();

        for (int p = 0; p < points.length; p++) {
            for (int q = p + 1; q < points.length; q++) {
                for (int r = q + 1; r < points.length; r ++) {
                    for (int s = r + 1; s < points.length; s++) {
                        double pq = points[p].slopeTo(points[q]);
                        double pr = points[p].slopeTo(points[r]);
                        double ps = points[p].slopeTo(points[s]);

                        if (pq == pr && pq == ps) {
                            Point[] currentpoints = new Point[]{points[p], points[q], points[r], points[s]};
                            Point lowest = new Point(32678, 32678);
                            Point highest = new Point(-1, -1);
                            for (Point point : currentpoints) {
                                if (point.compareTo(lowest) < 0) {
                                    lowest = point;
                                }

                                if (point.compareTo(highest) > 0) {
                                    highest = point;
                                }
                            }
                            segments.add(new LineSegment(lowest, highest));
                        }
                    }
                }
            }
        }
    }
    // the number of line segments
    public int numberOfSegments() {
        return segments.size();
    }
    // the line segments
    public LineSegment[] segments() {
        LineSegment[] result = new LineSegment[numberOfSegments()];
        for (int i = 0; i < numberOfSegments(); i++) {
            result[i] = segments.get(i);
        }
        return result;
    }
}