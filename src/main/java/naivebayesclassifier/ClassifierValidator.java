package naivebayesclassifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ClassifierValidator {

    private static final int ATTRIBUTES = 16;

    public static void main(final String[] args) {
        try {
            foldValidation();
        } catch (final IOException e) {
            System.out.println("Error while preforming I/O operations");
        }
    }

    private static void foldValidation() throws IOException {
        final int crossoverFolds = 10;
        final Set<Individual> individuals = new LinkedHashSet<>();
        setupData(individuals);

        double accuracy = 0;

        for (int i = 0; i < crossoverFolds; i++) {
            final int partSize = individuals.size() / crossoverFolds;
            final int from = i * partSize;
            final int to = (i + 1) * partSize;
            final List<Individual> subList = new ArrayList<>(individuals).subList(from, to);
            final Set<Individual> testSet = new LinkedHashSet<>(subList);
            final Set<Individual> learnSet = new LinkedHashSet<>(individuals);
            learnSet.removeAll(testSet);

            final Classifier classifier = new Classifier();
            classifier.learn(learnSet);

            int match = 0;
            for (final Individual individual : testSet) {
                if (individual.getClassName() == classifier.classify(individual)) {
                    match++;
                }
            }

            final double currentAccuracy = (double) match / (double) partSize;
            System.out.println("matches: " + match + "/" + partSize + " => " + currentAccuracy);
            accuracy += currentAccuracy;
        }

        System.out.println("accuracy: " + accuracy / crossoverFolds);

    }

    private static void setupData(final Set<Individual> individuals) throws IOException {
        final File file = new File("src/main/resources/house-votes-84.data");
        try (final BufferedReader is = new BufferedReader(new FileReader(file))) {
            while (is.ready()) {
                final String line = is.readLine();
                final String[] attrs = line.split(",");

                final boolean className = attrs[0].equals("republican");
                final boolean[] attributes = new boolean[ATTRIBUTES];
                for (int i = 1; i <= ATTRIBUTES; i++) {
                    attributes[i - 1] = attrs[i].equals("y");
                }

                individuals.add(new Individual(className, attributes));
            }
        }
    }
}
