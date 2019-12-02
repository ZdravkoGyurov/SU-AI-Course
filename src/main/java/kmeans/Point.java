package kmeans;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class Point {

    private final double x;
    private final double y;
    private Point centroid;
    private static final Random R = new Random();

    public Point(final double x, final double y, final Point centroid) {
        this.x = x;
        this.y = y;
        this.centroid = centroid;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Point getCentroid() {
        return centroid;
    }

    public void setCentroid(final Point centroid) {
        this.centroid = centroid;
    }

    public static double distance(final Point p1, final Point p2) {
        return Math.sqrt(Math.pow(p2.getX() - p1.getX(), 2) + Math.pow(p2.getY() - p1.getY(), 2));
    }

    public static void migrate(final Point point, final Point oldCentroid, final Point newCentroid, final Plot plot) {
        final Cluster oldCluster = plot.getClusterWithCentroid(oldCentroid);
        final Cluster newCluster = plot.getClusterWithCentroid(newCentroid);

        migrate(point, oldCluster, newCluster);
    }

    // Method for testing purposes
    public static Point generateRandomPoint(final double min, final double max, final Point centroid) {
        final double x = min + (max - min) * R.nextDouble();
        final double y = min + (max - min) * R.nextDouble();

        return new Point(x, y, centroid);
    }

    // Method for testing purposes
    public static Set<Point> generateRandomPoints(final double min, final double max, final Point centroid,
            final int numPts) {
        final Set<Point> randomPoints = new LinkedHashSet<>();

        while (randomPoints.size() < numPts) {
            randomPoints.add(generateRandomPoint(min, max, centroid));
        }

        return randomPoints;
    }

    private static void migrate(final Point p, final Cluster oldCluster, final Cluster newCluster) {
        p.setCentroid(newCluster.getCentroid());

        oldCluster.getPoints().remove(p);
        newCluster.getPoints().add(p);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Point)) {
            return false;
        }
        final Point other = (Point) obj;
        return Double.doubleToLongBits(x) == Double.doubleToLongBits(other.x)
                && Double.doubleToLongBits(y) == Double.doubleToLongBits(other.y);
    }

}
