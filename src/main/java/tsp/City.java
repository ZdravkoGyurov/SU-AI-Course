package tsp;

public final class City {

    private final int x;
    private final int y;

    public City(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public double calcDistance(final City city) {
        final double horizontalDist = Math.pow(city.getX() - this.x, 2);
        final double verticalDist = Math.pow(city.getY() - this.y, 2);

        return Math.sqrt(horizontalDist + verticalDist);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
