package tsp;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public final class GeneticTSP {

    private static final Random RANDOM = new Random();
    private static int MIN_COORDINATE = -20;
    private static int MAX_COORDINATE = 20;
    private static int POPULATION_SIZE = 50;
    private static int ELITE_POPULATION_SIZE = 6;
    private static int BINARY_TOURNAMENT_SELECTION = 2;
    private static final int GENERATIONS = 100;

    private GeneticTSP() {
        // Utility class
    }

    public static void run(final int cities) {
        final City[] cityList = generateCityList(cities);

        City[][] parentPopulation = generateInitialPopulation(cityList, POPULATION_SIZE);

        for(int j = 0; j < GENERATIONS; j++) {
            final City[][] childPopulation = new City[POPULATION_SIZE][cities];
            int childrenInChildPopulation = 0;

            // add elite ??? should we?
            final RouteRank[] parentPopulationRanks = generatePopulationRanks(parentPopulation);
            for(int i = 0; i < ELITE_POPULATION_SIZE; i++) {
                final int indexOfCurrentElite = parentPopulationRanks[i].getIndex();
                final City[] currentEliteRoute = parentPopulation[indexOfCurrentElite];
                 childPopulation[childrenInChildPopulation++] = currentEliteRoute;
            }

            while(childrenInChildPopulation < POPULATION_SIZE) {
                final City[] parent1 = tournamentSelection(parentPopulation);
                final City[] parent2 = tournamentSelection(parentPopulation);

                final City[] child1 = copyParent(parent1);
                final City[] child2 = copyParent(parent2);

                crossover(parent1, parent2, child1, child2);

                swapMutate(child1, cities);
                swapMutate(child2, cities);

//                evaluate child1, child2 for fitness ??? why?

                childPopulation[childrenInChildPopulation++] = child1;
                childPopulation[childrenInChildPopulation++] = child2;
            }

//            parentPopulation = combine parentPopulation and childPopulation somehow to get POPULATION_SIZE new individuals
            parentPopulation = childPopulation;

            final RouteRank[] ranks = generatePopulationRanks(parentPopulation);
            for(final City c : parentPopulation[ranks[0].getIndex()]) {
                System.out.print("(" + c.getX() + ", " + c.getY() + ") ");
            }
            System.out.println(calcDistance(parentPopulation[ranks[0].getIndex()]));
            for(int i = 0; i < 5; i++) {
                System.out.println(ranks[i].getIndex() + ", " + ranks[i].getFitness());
            }
            System.out.println("--------------------------------------------------");
        }

    }

    private static City[] copyParent(final City[] parent) {
        final City[] child = new City[parent.length];

        int iter = 0;
        for(final City c : parent) {
            child[iter++] = new City(c.getX(), c.getY());
        }

        return child;
    }

    private static void crossover(final City[] parent1, final City[] parent2, final City[] child1, final City[] child2) {
        final int swapPoint = RANDOM.nextInt(child1.length);

        crossoverChild(swapPoint, parent1, parent2, child1);
        crossoverChild(swapPoint, parent1, parent2, child2);
    }

    private static void crossoverChild(final int swapPoint, final City[] parent1, final City[] parent2, final City[] child) {
        int iter2 = swapPoint + 1;

        for(int i = swapPoint + 1; i < child.length; i++) {
            if(isCityInRoute(parent2[iter2], parent1, 0, i)) {
                child[i] = parent2[iter2];
            }

            iter2++;
            if(iter2 == child.length) {
                iter2 = 0;
            }
        }
    }

    private static boolean isCityInRoute(final City city, final City[] route, final int from, final int to) {
        for(int i = from; i < to; i++) {
            if(route[i].equals(city)) return true;
        }
        return false;
    }

    private static void swapMutate(final City[] route, final int cities) {
        final int r = RANDOM.nextInt(10);

        if(r < 2) {
            final int randomIndex1 = RANDOM.nextInt(cities);
            int randomIndex2 = RANDOM.nextInt(cities);

            while(randomIndex1 == randomIndex2) randomIndex2 = RANDOM.nextInt(cities);

            final City tempCity = route[randomIndex1];
            route[randomIndex1] = route[randomIndex2];
            route[randomIndex2] = tempCity;
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

    private static void sortPopulationByFitness(final City[][] population) {
        Arrays.sort(population, new Comparator<City[]>() {
            @Override
            public int compare(final City[] route1, final City[] route2) {
                final double route1Fitness = calcFitness(route1);
                final double route2Fitness = calcFitness(route2);

                if(route1Fitness > route2Fitness) {
                    return -1;
                } else if(route1Fitness == route2Fitness) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
    }

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
