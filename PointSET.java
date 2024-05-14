import edu.princeton.cs.algs4.SET;
import java.util.ArrayList;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class PointSET {
    private int size;
    private SET<Point2D> points;

    public PointSET() {
        points = new SET<>();
        size = 0;

    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D p) {
        if (contains(p))
            return;
        points.add(p);
        size++;
    }

    public boolean contains(Point2D p) {
        return points.contains(p);
    }

    public void draw() {
        return;
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();
        ArrayList<Point2D> pointsInside = new ArrayList<>();
        java.util.Iterator<Point2D> it = points.iterator();
        while (it.hasNext()) {
            Point2D point = it.next();
            boolean inX = point.x() >= rect.xmin() && point.x() <= rect.xmax();
            boolean inY = point.y() >= rect.ymin() && point.y() <= rect.ymax();
            if (inX && inY)
                pointsInside.add(point);
        }
        return pointsInside;
    }

    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        if (isEmpty())
            return null;
        java.util.Iterator<Point2D> it = points.iterator();
        Point2D closest = it.next();
        while (it.hasNext()) {
            Point2D point = it.next();
            if (point.distanceSquaredTo(p) < closest.distanceSquaredTo(p))
                closest = point;
        }

        return closest;

    }

    public static void main(String[] args) {
        return;
    }
}