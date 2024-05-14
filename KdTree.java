import java.util.ArrayList;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class KdTree {

    private class Node {
        private Point2D point;
        private Node left;
        private Node right;

        public Node(Point2D point) {
            this.point = point;
            left = null;
            right = null;
        }
    }

    private int size;
    private Node root;

    public KdTree() {
        size = 0;
        root = null;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    private Node insert(Node curr, Node entry, int depth) {
        if (curr == null) {
            size++;
            return entry;
        }
        double xcmp = Double.compare(curr.point.x(), entry.point.x());
        double ycmp = Double.compare(curr.point.y(), entry.point.y());
        boolean isDuplicate = xcmp == 0 && ycmp == 0;
        if (isDuplicate)
            return curr;
        boolean isEven = (depth % 2) == 0;
        boolean goRight = (isEven && xcmp < 0) || (!isEven && ycmp < 0);
        if (goRight)
            curr.right = insert(curr.right, entry, depth + 1);
        else
            curr.left = insert(curr.left, entry, depth + 1);
        return curr;
    }

    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        root = insert(root, new Node(p), 0);
    }

    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        Node curr = root;
        int depth = 0;
        while (curr != null) {
            double xcmp = Double.compare(curr.point.x(), p.x());
            double ycmp = Double.compare(curr.point.y(), p.y());
            if (xcmp == 0 && ycmp == 0)
                return true;
            boolean isEven = (depth % 2) == 0;
            boolean goRight = isEven && xcmp < 0 || (!isEven && ycmp < 0);
            if (goRight)
                curr = curr.right;
            else
                curr = curr.left;
            depth++;
        }
        return false;
    }

    public void draw() {
        return;
    }

    private ArrayList<Point2D> range(RectHV rect, Node h, ArrayList<Point2D> pointsInside, int depth) {
        if (h == null)
            return pointsInside;
        if (rect.contains(h.point)) {
            pointsInside.add(h.point);
        }
        double hx = h.point.x();
        double hy = h.point.y();
        double xcmp = Double.compare(hx, rect.xmin());
        double ycmp = Double.compare(hy, rect.ymin());
        boolean isEven = (depth % 2) == 0;
        boolean goRight = (isEven && xcmp < 0) || (!isEven && ycmp < 0);
        pointsInside = range(rect, goRight ? h.right : h.left, pointsInside, depth + 1);
        boolean doOther = isEven && hx <= rect.xmax() && hx >= rect.xmin();
        doOther = doOther || (!isEven && hy <= rect.ymax() && hy >= rect.ymin());
        if (doOther)
            pointsInside = range(rect, !goRight ? h.right : h.left, pointsInside, depth + 1);

        return pointsInside;
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();
        ArrayList<Point2D> pointsInside = new ArrayList<>();
        if (root == null)
            return pointsInside;
        pointsInside = range(rect, root, pointsInside, 0);

        return pointsInside;
    }

    private Point2D nearest(Node h, Point2D p, int depth, Point2D closest, RectHV space) {
        if (h == null)
            return closest;
        if (h.point.distanceSquaredTo(p) < closest.distanceSquaredTo(p)) {
            closest = h.point;
        }
        double xcmp = Double.compare(h.point.x(), p.x());
        double ycmp = Double.compare(h.point.y(), p.y());
        boolean isEven = (depth % 2) == 0;
        boolean goRight = (isEven && xcmp < 0) || (!isEven && ycmp < 0);
        RectHV split1;
        RectHV split2;
        if (isEven) {
            split1 = new RectHV(goRight ? h.point.x() : space.xmin(), space.ymin(),
                    !goRight ? h.point.x() : space.xmax(), space.ymax());
            split2 = new RectHV(!goRight ? h.point.x() : space.xmin(), space.ymin(),
                    goRight ? h.point.x() : space.xmax(), space.ymax());
        } else {
            split1 = new RectHV(space.xmin(), goRight ? h.point.y() : space.ymin(), space.xmax(),
                    !goRight ? h.point.y() : space.ymax());
            split2 = new RectHV(space.xmin(), !goRight ? h.point.y() : space.ymin(), space.xmax(),
                    goRight ? h.point.y() : space.ymax());
        }
        closest = nearest(goRight ? h.right : h.left, p, depth + 1, closest, split1);

        double closeDist = closest.distanceSquaredTo(p);
        double splitDist = split2.distanceSquaredTo(p);

        if (closeDist > splitDist)
            closest = nearest(goRight ? h.left : h.right, p, depth + 1, closest, split2);

        return closest;
    }

    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        if (isEmpty())
            return null;
        return nearest(root, p, 0, root.point, new RectHV(0, 0, 1, 1));
    }

    public static void main(String[] args) {
        return;
    }
}
