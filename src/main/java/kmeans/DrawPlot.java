package kmeans;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

class DrawPlot extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final int SIZE = 500;
    private static final float STROKE_SIZE = 5.0f;
    private static final double POINT_SIZE = 5.0;
    private final Plot plot;
    private final double max;

    public DrawPlot(final Plot plot, final double max) {
        final JPanel panel = new JPanel();
        getContentPane().add(panel);
        setSize(SIZE, SIZE);
        this.plot = plot;
        this.max = SIZE / (2.0 * max);
    }

    @Override
    public void paint(final Graphics g) {
        super.paint(g);
        final Graphics2D g2 = (Graphics2D) g;
        drawCoordinateLines(g2);
        g2.setStroke(new BasicStroke(STROKE_SIZE));

        for (final Cluster cluster : plot.getClusters()) {
            g2.setPaint(Color.BLACK);
            final double centroidEllipseX = SIZE / 2.0 + max * cluster.getCentroid().getX();
            final double centroidEllipseY = SIZE / 2.0 - max * cluster.getCentroid().getY();
            g2.draw(new Ellipse2D.Double(centroidEllipseX, centroidEllipseY, POINT_SIZE, POINT_SIZE));

            g2.setPaint(cluster.getColor());

            for (final Point point : cluster.getPoints()) {
                final double ellipseX = SIZE / 2.0 + max * point.getX();
                final double ellipseY = SIZE / 2.0 - max * point.getY();

                g2.draw(new Ellipse2D.Double(ellipseX, ellipseY, POINT_SIZE, POINT_SIZE));
            }
        }
    }

    private void drawCoordinateLines(final Graphics2D g2) {
        // horizontal
        g2.draw(new Line2D.Double(245, 50, 255, 50));
        g2.draw(new Line2D.Double(245, 100, 255, 100));
        g2.draw(new Line2D.Double(245, 150, 255, 150));
        g2.draw(new Line2D.Double(245, 200, 255, 200));
        g2.draw(new Line2D.Double(0, 250, 500, 250));
        g2.draw(new Line2D.Double(245, 300, 255, 300));
        g2.draw(new Line2D.Double(245, 350, 255, 350));
        g2.draw(new Line2D.Double(245, 400, 255, 400));
        g2.draw(new Line2D.Double(245, 450, 255, 450));

        // vertical
        g2.draw(new Line2D.Double(50, 245, 50, 255));
        g2.draw(new Line2D.Double(100, 245, 100, 255));
        g2.draw(new Line2D.Double(150, 245, 150, 255));
        g2.draw(new Line2D.Double(200, 245, 200, 255));
        g2.draw(new Line2D.Double(250, 0, 250, 500));
        g2.draw(new Line2D.Double(300, 245, 300, 255));
        g2.draw(new Line2D.Double(350, 245, 350, 255));
        g2.draw(new Line2D.Double(400, 245, 400, 255));
        g2.draw(new Line2D.Double(450, 245, 450, 255));
    }

    public static void main(final String[] args) {
        final int numClusters = 5;
        final double min = -5.0;
        final double max = -min;
        final int pointsPerCluster = 15;
        final Plot plot = new Plot(numClusters, min, max);

        for (final Cluster c : plot.getClusters()) {
            c.getPoints().addAll(Point.generateRandomPoints(min, max, c.getCentroid(), pointsPerCluster));
        }

        int iteration = 0;
        final DrawPlot drawPlot = new DrawPlot(plot, max);
        drawPlot.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        drawPlot.setVisible(true);
        plot.printClusters();

        while (plot.reevaluatePoints()) {
            iteration++;
            plot.printClusters();
        }

        System.out.println(iteration);
    }
}
