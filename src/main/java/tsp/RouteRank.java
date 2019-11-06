package tsp;

public final class RouteRank {

    private final int index;
    private final double fitness;

    public RouteRank(final int index, final double fitness) {
        this.index = index;
        this.fitness = fitness;
    }

    public int getIndex() {
        return index;
    }

    public double getFitness() {
        return fitness;
    }
}