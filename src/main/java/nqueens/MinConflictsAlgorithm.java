package nqueens;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MinConflictsAlgorithm {

    // queensInRow[1]
    // queensInDiagonal1[3]
    // queensInDiagonal2[2]

    private MinConflictsAlgorithm() {
        // Utility class
    }

    public static void run(final int size) {
        int[] queens = new int[size];
        boolean foundAnswer = false;

        while(!foundAnswer) {
            queens = randomInit(size);
            final int maxIterations = size * 3;
            final int[] queensInRow = getQueensInRow(queens);
            final int[] queensInDiagonal1 = getQueensInDiagonal1(queens);
            final int[] queensInDiagonal2 = getQueensInDiagonal2(queens);
            int row, column;

            for(int i = 0; i < maxIterations; i++) {
                row = getRowWithMaxConflicts(queens);
                column = getColumnWithMaxConflicts(queens);

                queens[column] = row;

                if(!hasConflicts(queens)) {
                    foundAnswer = true;
                    break;
                }
            }
        }

        printBoard(queens);
    }

    private static int[] getQueensInDiagonal2(final int[] queens) {
        final int[] queensInDiagonal2 = new int[queens.length * 2 - 1];

        for(int i = 0; i < queens.length; i++) {
            queensInDiagonal2[i + queens[i]]++;
        }

        return queensInDiagonal2;
    }

    private static int[] getQueensInDiagonal1(final int[] queens) {
        final int[] queensInDiagonal1 = new int[queens.length * 2 - 1];

        for(int i = 0; i < queens.length; i++) {
            queensInDiagonal1[i - queens[i] + queens.length - 1]++;
        }

        return queensInDiagonal1;
    }

    private static int[] getQueensInRow(final int[] queens) {
        final int[] queensInRow = new int[queens.length];

        for(int i = 0; i < queens.length; i++) {
            queensInRow[queens[i]]++;
        }

        return queensInRow;
    }

    private static void printBoard(final int[] queens) {
        final char[][] board = new char[queens.length][queens.length];

        for(int i = 0; i < queens.length; i++) {
            for(int j = 0; j < queens.length; j++) {
                board[i][j] = '_';
            }
        }

        for(int i = 0; i < queens.length; i++) {
            board[queens[i]][i] = '*';
        }

        for(int i = 0; i < queens.length; i++) {
            for(int j = 0; j < queens.length; j++) {
                System.out.print(board[i][j]);
            }
            System.out.println();
        }
    }

    private static int getConflicts(final int[] queens, final int row, final int col) {
        int conflicts = 0;

        for(int i = 0; i < queens.length; i++) {
            if(i == col) {
                continue;
            }
            if(queens[i] == row || Math.abs(queens[i] - row) == Math.abs(i - col)) {
                conflicts++;
            }
        }

        return conflicts;
    }

    private static int getColumnWithMaxConflicts(final int[] queens) {
        int maxConflicts = 0;
        final List<Integer> columnsWithMaxConflicts = new ArrayList<>();

        for(int i = 0; i < queens.length; i++) {
            final int currentConflicts = getConflicts(queens, queens[i], i);
            if(currentConflicts > maxConflicts) {
                maxConflicts = currentConflicts;
                columnsWithMaxConflicts.add(i);
            }
        }

        if(columnsWithMaxConflicts.size() > 0) {
            Collections.shuffle(columnsWithMaxConflicts);
        }

        return columnsWithMaxConflicts.size() == 0 ? 0 : columnsWithMaxConflicts.get(0);
    }

    private static int getRowWithMaxConflicts(final int[] queens) {
        int maxConflicts = 0;
        final List<Integer> rowsWithMaxConflicts = new ArrayList<>();

        for(int i = 0; i < queens.length; i++) {
            final int currentConflicts = getConflicts(queens, queens[i], i);
            if(currentConflicts > maxConflicts) {
                maxConflicts = currentConflicts;
                rowsWithMaxConflicts.add(i);
            }
        }

        if(rowsWithMaxConflicts.size() > 0) {
            Collections.shuffle(rowsWithMaxConflicts);
        }

        return rowsWithMaxConflicts.size() == 0 ? 0 : rowsWithMaxConflicts.get(0);
    }

    private static boolean hasConflicts(final int[] queens) {
        for(int i = 0; i < queens.length; i++) {
            if(getConflicts(queens, queens[i], i) > 0) {
                return true;
            }
        }

        return false;
    }

    private static int[] randomInit(final int size) {
        final Integer[] queens = new Integer[size];

        for(int i = 0; i < size; i++) {
            queens[i] = i;
        }

        final List<Integer> queensList = Arrays.asList(queens);
        Collections.shuffle(queensList);

        final int[] result = new int[size];

        for(int i = 0; i < size; i++) {
            result[i] = queensList.get(i);
        }

        return result;
    }

}
