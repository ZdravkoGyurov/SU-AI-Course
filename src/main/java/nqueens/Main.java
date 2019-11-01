package nqueens;

import java.util.Scanner;

/**
 * Entry point class. Starts the Min Conflicts algorithm and tracks how much time it took to complete.
 */
public final class Main {

    public static void main(String[] args) {
        final Scanner sc = new Scanner(System.in);

        final long start = System.currentTimeMillis();
        MinConflictsAlgorithm.run(sc.nextInt());
        final long stop = System.currentTimeMillis();

        System.out.println("Found in " + ((double) (stop - start)) / 1000 + "s.");
    }
}
