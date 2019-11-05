package tsp;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public final class GeneticTSP {

    private static final Random RANDOM = new Random();
    private static int MIN_COORDINATE = -10;
    private static int MAX_COORDINATE = 10;
    private static int POPULATION_SIZE = 5;

    private GeneticTSP() {
        // Utility class
    }

    public static void run(final int cities) {
        final City[] cityList = generateCityList(cities);

        final City[][] initialPopulation = generateInitialPopulation(cityList, POPULATION_SIZE);
        printPopulation(initialPopulation);
        sortPopulationByFitness(initialPopulation);
        System.out.println("------------");
        printPopulation(initialPopulation);
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
