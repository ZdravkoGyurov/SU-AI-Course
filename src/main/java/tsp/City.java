package tsp;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return x == city.x &&
                y == city.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}