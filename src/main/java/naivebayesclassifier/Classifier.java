package naivebayesclassifier;

import java.util.LinkedHashSet;
import java.util.Set;

public class Classifier {

    private static final int ATTRIBUTES = 16;
    private static final boolean REPUBLICAN = true;
    private static final boolean DEMOCRAT = !REPUBLICAN;
    private final Set<Individual> individualsDataSet;
    private int totalRepublicans;
    private int totalDemocrats;
    private ProbabilityTable[] probabilityTables;

    public Classifier() {
        individualsDataSet = new LinkedHashSet<>();
        totalRepublicans = 0;
        totalDemocrats = 0;
        probabilityTables = new ProbabilityTable[ATTRIBUTES];
    }

    public void learn(final Individual individual) {
        if (individual == null) {
            throw new IllegalArgumentException();
        }

        individualsDataSet.add(individual);
        updateProbabilityTables();
    }

    public void learn(final Set<Individual> individuals) {
        if (individuals == null) {
            throw new IllegalArgumentException();
        }

        individualsDataSet.addAll(individuals);
        updateProbabilityTables();
    }

    public boolean classify(final Individual individual) {
        if (individual == null) {
            throw new IllegalArgumentException();
        }

        double probabilityOfXWhenRepublican = 1;
        double probabilityOfXWhenDemocrat = 1;

        final boolean[] individualAttributes = individual.getAttributes();
        for (int i = 0; i < ATTRIBUTES; i++) {
            final boolean currentAttributeValue = individualAttributes[i];
            final ProbabilityTable currentAttributeTable = probabilityTables[i];

            if (currentAttributeValue) {
                probabilityOfXWhenRepublican *= currentAttributeTable.probabilityTrueWhenRepublicans();
                probabilityOfXWhenDemocrat *= currentAttributeTable.probabilityTrueWhenDemocrats();
            } else {
                probabilityOfXWhenRepublican *= currentAttributeTable.probabilityFalseWhenRepublicans();
                probabilityOfXWhenDemocrat *= currentAttributeTable.probabilityFalseWhenDemocrats();
            }
        }

        final double probabilityOfX = probabilityOfX(individual);

        // normalizing
        probabilityOfXWhenRepublican /= probabilityOfX;
        probabilityOfXWhenDemocrat /= probabilityOfX;

        return probabilityOfXWhenRepublican > probabilityOfXWhenDemocrat ? REPUBLICAN : DEMOCRAT;
    }

    private void updateProbabilityTables() {
        totalRepublicans = 0;
        totalDemocrats = 0;
        probabilityTables = new ProbabilityTable[ATTRIBUTES];
        for (int i = 0; i < ATTRIBUTES; i++) {
            probabilityTables[i] = new ProbabilityTable();
        }

        for (final Individual individual : individualsDataSet) {
            updateTableValues(individual);
        }
    }

    private double probabilityOfX(final Individual individual) {
        double probability = 1.0;

        final boolean[] individualAttributes = individual.getAttributes();
        for (int i = 0; i < ATTRIBUTES; i++) {
            final ProbabilityTable currentAttributeTable = probabilityTables[i];
            if (individualAttributes[i]) {
                probability *= currentAttributeTable.getWhenTrue();
            } else {
                probability *= currentAttributeTable.getWhenFalse();
            }
        }

        return probability;
    }

    private void updateTableValues(final Individual individual) {
        final boolean[] individualAttributes = individual.getAttributes();

        if (individual.getClassName() == REPUBLICAN) {
            totalRepublicans++;

            for (int i = 0; i < ATTRIBUTES; i++) {
                final boolean currentAttributeValue = individualAttributes[i];
                final ProbabilityTable currentAttributeTable = probabilityTables[i];

                if (currentAttributeValue) {
                    currentAttributeTable.setTrueWhenRepublicans(currentAttributeTable.getTrueWhenRepublicans() + 1);
                } else {
                    currentAttributeTable.setFalseWhenRepublicans(currentAttributeTable.getFalseWhenRepublicans() + 1);
                }

                currentAttributeTable.setTotalRepublicans(totalRepublicans);
            }
        } else {
            totalDemocrats++;

            for (int i = 0; i < ATTRIBUTES; i++) {
                final boolean currentAttributeValue = individualAttributes[i];
                final ProbabilityTable currentAttributeTable = probabilityTables[i];

                if (currentAttributeValue) {
                    currentAttributeTable.setTrueWhenDemocrats(currentAttributeTable.getTrueWhenDemocrats() + 1);
                } else {
                    currentAttributeTable.setFalseWhenDemocrats(currentAttributeTable.getFalseWhenDemocrats() + 1);
                }

                currentAttributeTable.setTotalDemocrats(totalDemocrats);
            }
        }
    }

}
