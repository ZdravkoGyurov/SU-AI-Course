package nqueens;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        final Scanner sc = new Scanner(System.in);
        final long start = System.currentTimeMillis();
        MinConflictsAlgorithm.run(sc.nextInt(), false);
        final long stop = System.currentTimeMillis();
        System.out.println("Found in " + ((double) (stop - start)) / 1000 + "s.");
    }
}
