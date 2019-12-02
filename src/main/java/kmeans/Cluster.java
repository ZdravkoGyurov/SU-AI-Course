package kmeans;

import java.awt.Color;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class Cluster {

    private Point centroid;
    private final Set<Point> points;
    private final Color color;

    public Cluster(final Point centroid, final Color color) {
        this.centroid = centroid;
        points = new LinkedHashSet<>();
        this.color = color;
    }

    public Point getCentroid() {
        return centroid;
    }

    public void setCentroid(final Point centroid) {
        this.centroid = centroid;
    }

    public Set<Point> getPoints() {
        return points;
    }

    public Color getColor() {
        return color;
    }

    public void reevaluateCentroid() {
        if (points.isEmpty()) {
            return;
        }

        double newX = 0;
        double newY = 0;

        for (final Point point : points) {
            newX += point.getX();
            newY += point.getY();
        }

        newX /= points.size();
        newY /= points.size();

        final Point newCentroid = new Point(newX, newY, null);
        centroid = newCentroid;

        for (final Point point : points) {
            point.setCentroid(centroid);
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Centroid: ").append(centroid).append("\n");

        sb.append("Points: \n");
        for (final Point p : points) {
            sb.append(p).append("\n");
        }

        return sb.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(centroid);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Cluster)) {
            return false;
        }
        final Cluster other = (Cluster) obj;
        return Objects.equals(centroid, other.centroid);
    }

}
