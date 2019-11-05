package tsp;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public final class GeneticTSP {

    private static final Random RANDOM = new Random();
    private static int MIN_COORDINATE = -10;
    private static int MAX_COORDINATE = 10;
    private static int POPULATION_SIZE = 50;
    private static int ELITE_POPULATION_SIZE = 5;
    private static int BINARY_TOURNAMENT_SELECTION = 2;

    private GeneticTSP() {
        // Utility class
    }

    public static void run(final int cities) {
        final City[] cityList = generateCityList(cities);

        final City[][] parentPopulation = generateInitialPopulation(cityList, POPULATION_SIZE);

        while(true) {
            final City[][] childPopulation = new City[POPULATION_SIZE][cities];
            int childrenInChildPopulation = 0;

            // add elite ?????
            final RouteRank[] parentPopulationRanks = generatePopulationRanks(parentPopulation);
            for(int i = 0; i < ELITE_POPULATION_SIZE; i++) {
                // childPopulation[childrenInChildPopulation++] = parentPopulationRanks;
            }

            while(childrenInChildPopulation < POPULATION_SIZE) {
                final City[] parent1 = tournamentSelection(parentPopulation);
                final City[] parent2 = tournamentSelection(parentPopulation);
//                crossover(parent1, parent2, child1, child2);
//                mutate(child1);
//                mutate(child2);
//                evaluate child1, child2 for fitness ??? why
//                childPopulation[childrenInChildPopulation++] = child1;
//                childPopulation[childrenInChildPopulation++] = child2;
            }
//            parentPopulation = combine parentPopulation and childPopulation somehow to get POPULATION_SIZE new individuals
        }

    }

    private static City[] tournamentSelection(final City[][] population) {
        City[] bestRoute = null;

        for(int i = 0; i < BINARY_TOURNAMENT_SELECTION; i++) {
            final City[] currentRoute = population[RANDOM.nextInt(POPULATION_SIZE)];
            if(bestRoute == null || calcFitness(currentRoute) > calcFitness(bestRoute)) {
                bestRoute = currentRoute;
            }
        }

        return bestRoute;
    }

    private static void printPopulation(final City[][] population) {
        for(final City[] route : population) {
            for(final City city : route) {
                System.out.print("(" + city.getX() + ", " + city.getY() + ") ");
            }
            System.out.print(calcFitness(route));
            System.out.println();
        }
    }

    private static RouteRank[] generatePopulationRanks(final City[][] population) {
        final RouteRank[] popRanks = new RouteRank[population.length];

        int iter = 0;
        for(final City[] route : population) {
            popRanks[iter] = new RouteRank(iter, calcFitness(route));
            iter++;
        }

        Arrays.sort(popRanks, new Comparator<RouteRank>() {
            @Override
            public int compare(final RouteRank rank1, final RouteRank rank2) {
                if(rank1.getFitness() > rank2.getFitness()) {
                    return -1;
                } else if(rank1.getFitness() == rank2.getFitness()) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });

        return popRanks;
    }

//    private static void sortPopulationByFitness(final City[][] population) {
//        Arrays.sort(population, new Comparator<City[]>() {
//            @Override
//            public int compare(final City[] route1, final City[] route2) {
//                final double route1Fitness = calcFitness(route1);
//                final double route2Fitness = calcFitness(route2);
//
//                if(route1Fitness > route2Fitness) {
//                    return -1;
//                } else if(route1Fitness == route2Fitness) {
//                    return 0;
//                } else {
//                    return 1;
//                }
//            }
//        });
//    }

    private static City[][] generateInitialPopulation(final City[] cityList, final int populationSize) {
        final City[][] population = new City[populationSize][cityList.length];

        for(int i = 0; i < populationSize; i++) {
            population[i] = generateRoute(cityList);
        }

        return population;
    }

    private static City[] generateRoute(final City[] cityList) {
        final City[] newRoute = new City[cityList.length];

        for(int i = 0; i < cityList.length; i++) {
            newRoute[i] = new City(cityList[i].getX(), cityList[i].getY());
        }

        for(int i = newRoute.length - 1; i > 0; i--) {
            final int index = RANDOM.nextInt(i + 1);
            final City temp = newRoute[index];
            newRoute[index] = newRoute[i];
            newRoute[i] = temp;
        }

        return newRoute;
    }

    private static City[] generateCityList(final int numCities) {
        final City[] cityList = new City[numCities];

        for(int i = 0; i < cityList.length; i++) {
            cityList[i] = new City(generateRandomCoordinate(), generateRandomCoordinate());
        }

        return cityList;
    }

    private static int generateRandomCoordinate() {
        return RANDOM.nextInt(MAX_COORDINATE - MIN_COORDINATE) + MIN_COORDINATE;
    }

    private static double calcFitness(final City[] route) {
        double distance = 0;

        for(int i = 0; i < route.length - 1; i++) {
            distance += route[i].calcDistance(route[i + 1]);
        }

        return 1 / distance;
    }

    private static double calcDistance(final City[] route) {
        double distance = 0;

        for(int i = 0; i < route.length - 1; i++) {
            distance += route[i].calcDistance(route[i + 1]);
        }

        return distance;
    }
}
