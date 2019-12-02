package kmeans;

import java.awt.Color;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Plot {

    final int k;
    final double min;
    final double max;
    final Map<Integer, Color> colors;
    final Set<Cluster> clusters;

    public Plot(final int k, final double min, final double max) {
        this.k = k;
        this.min = min;
        this.max = max;
        colors = generateColors();
        clusters = generateClusters(k);
    }

    // Method for testing purposes
    public void printClusters() {
        for (final Cluster c : clusters) {
            System.out.println(c);
        }
    }

    public Cluster getClusterWithCentroid(final Point centroid) {
        for (final Cluster c : clusters) {
            if (c.getCentroid().equals(centroid)) {
                return c;
            }
        }

        return null;
    }

    public Set<Point> getCentroids() {
        final Set<Point> centroids = new LinkedHashSet<>();

        for (final Cluster cluster : clusters) {
            centroids.add(cluster.getCentroid());
        }

        return centroids;
    }

    public Set<Cluster> getClusters() {
        return clusters;
    }

    public boolean reevaluatePoints() {
        final Map<Point, Point> migratingPoints = findMigratingPoints();

        if (migratingPoints.size() == 0) {
            return false;
        }

        migratePoints(migratingPoints);

        reevaluateCentroids();

        return true;
    }

    private void migratePoints(final Map<Point, Point> migratingPoints) {
        for (final Entry<Point, Point> migratingPoint : migratingPoints.entrySet()) {
            final Point point = migratingPoint.getKey();
            final Point oldCentroid = point.getCentroid();
            final Point newCentroid = migratingPoint.getValue();
            Point.migrate(point, oldCentroid, newCentroid, this);
        }
    }

    private Map<Point, Point> findMigratingPoints() {
        final Map<Point, Point> migratingPoints = new LinkedHashMap<>();

        for (final Cluster cluster : clusters) {
            for (final Point point : cluster.getPoints()) {
                final Point closestCentroid = getClosestCentroidToPoint(point);
                if (!closestCentroid.equals(cluster.getCentroid())) {
                    migratingPoints.put(point, closestCentroid);
                }
            }
        }

        return migratingPoints;
    }

    private void reevaluateCentroids() {
        for (final Cluster cluster : clusters) {
            cluster.reevaluateCentroid();
        }
    }

    private Map<Integer, Color> generateColors() {
        final Map<Integer, Color> colorMap = new LinkedHashMap<>();

        // Red
        colorMap.put(0, new Color(230, 25, 75));
        // Green
        colorMap.put(1, new Color(60, 180, 75));
        // Blue
        colorMap.put(2, new Color(0, 130, 200));
        // Yellow
        colorMap.put(3, new Color(255, 225, 25));
        // Orange
        colorMap.put(4, new Color(245, 130, 48));
        // Purple
        colorMap.put(5, new Color(145, 30, 180));
        // Cyan
        colorMap.put(6, new Color(70, 240, 240));
        // Magenta
        colorMap.put(7, new Color(240, 50, 230));
        // Lime
        colorMap.put(8, new Color(210, 245, 60));
        // Maroon
        colorMap.put(9, new Color(128, 0, 0));

        return colorMap;
    }

    private Set<Point> generateCentroids(final int k) {
        final Set<Point> centroids = new LinkedHashSet<>();

        while (centroids.size() < k) {
            centroids.add(Point.generateRandomPoint(min, max, null));
        }

        return centroids;
    }

    private Set<Cluster> generateClusters(final int k) {
        final Set<Cluster> clusterSet = new HashSet<>();
        final Set<Point> centroids = generateCentroids(k);

        while (centroids.size() < k) {
            centroids.add(Point.generateRandomPoint(min, max, null));
        }

        int colorIter = 0;
        for (final Point centroid : centroids) {
            clusterSet.add(new Cluster(centroid, colors.get(colorIter++)));
        }

        return clusterSet;
    }

    private Point getClosestCentroidToPoint(final Point point) {
        Point closestCentroid = null;
        double closestDistance = Double.MAX_VALUE;

        for (final Point centroid : getCentroids()) {
            final double currentDistance = Point.distance(point, centroid);
            if (currentDistance < closestDistance) {
                closestDistance = currentDistance;
                closestCentroid = centroid;
            }
        }

        return closestCentroid;
    }
}
