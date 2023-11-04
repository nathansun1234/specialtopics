import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    // finds all line segments containing 4 or more points
    private Point[] points;
    private ArrayList<LineSegment> segments;

    public FastCollinearPoints(Point[] pts) {
        points = pts;
        segments = new ArrayList<LineSegment>();
        
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

        Point[] copy1 = points.clone();
        Point[] copy2 = points.clone();
        
        Arrays.sort(copy1);
        
        for (int i = 0; i < copy1.length; i++) {
            Point origin = copy1[i];

            Arrays.sort(copy2);
            Arrays.sort(copy2, origin.slopeOrder());

            int count = 1;
            Point lineBeginning = null;

            for (int j = 0; j < copy2.length - 1; j++) {
                if (copy2[j].slopeTo(origin) == copy2[j + 1].slopeTo(origin)) {
                    count++;

                    if (count == 2) {
                        lineBeginning = copy2[j];
                        count++;
                    }

                    else if (count >= 4 && j + 1 == copy2.length - 1) {
                        if (lineBeginning.compareTo(origin) > 0) {
                            segments.add(new LineSegment(origin, copy2[j + 1]));
                        }

                        count = 1;
                    }
                }

                else if (count >= 4) {
                    if (lineBeginning.compareTo(origin) > 0) {
                        segments.add(new LineSegment(origin, copy2[j]));
                    }
                    
                    count = 1;
                }

                else {
                    count = 1;
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
