package nqueens;

import java.util.*;

public class MinConflictsAlgorithm {

    private static final Random random = new Random();

    private MinConflictsAlgorithm() {
        // Utility class
    }

    public static void run(final int size, final boolean printBoard) {
        int[] queens = new int[size];
        boolean foundAnswer = false;
        final int maxIterations = size * 3;

        while(!foundAnswer) {
            queens = randomInit(queens, size);
            int[] queensInRow = getQueensInRow(queens);
            int[] queensInDiagonal1 = getQueensInDiagonal1(queens);
            int[] queensInDiagonal2 = getQueensInDiagonal2(queens);
            int row, column;

            for(int i = 0; i < maxIterations; i++) {
                column = getColumnWithMaxConflicts(queens, queensInRow, queensInDiagonal1, queensInDiagonal2);
                row = getRowWithMinConflicts(column, queens, queensInRow, queensInDiagonal1, queensInDiagonal2);

                final int oldRow = queens[column];
                final int oldCol = column;
                final int newRow = row;
                final int newCol = column;
                queens[column] = row;

                updateQueensInRow(queensInRow, oldRow, newRow);
                updateQueensInDiagonal1(queensInDiagonal1, queens.length, oldRow, oldCol, newRow, newCol);
                updateQueensInDiagonal2(queensInDiagonal2, oldRow, oldCol, newRow, newCol);

                if(!hasConflicts(queens, queensInRow, queensInDiagonal1, queensInDiagonal2)) {
                    foundAnswer = true;
                    break;
                }
            }
        }

        if(printBoard) {
            printBoard(queens);
        }
    }

    private static void updateQueensInDiagonal2(final int[] queensInDiagonal2, final int oldRow, final int oldCol, final int newRow, final int newCol) {
        queensInDiagonal2[oldRow + oldCol]--;
        queensInDiagonal2[newRow + newCol]++;
    }

    private static void updateQueensInDiagonal1(final int[] queensInDiagonal1, final int size, final int oldRow, final int oldCol, final int newRow, final int newCol) {
        queensInDiagonal1[oldCol - oldRow + size - 1]--;
        queensInDiagonal1[newCol - newRow + size - 1]++;
    }

    private static void updateQueensInRow(final int[] queensInRow, final int oldRow, final int newRow) {
        queensInRow[oldRow]--;
        queensInRow[newRow]++;
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

        for(int queen : queens) {
            queensInRow[queen]++;
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

    private static int getConflicts(final int row, final int col, final int[] queensInRow,
                                    final int[] queensInDiagonal1, final int[] queensInDiagonal2) {
        return queensInRow[row] + queensInDiagonal1[col - row + queensInRow.length - 1] + queensInDiagonal2[row + col] - 3;
    }

    private static int getColumnWithMaxConflicts(final int[] queens, final int[] queensInRow,
                                                 final int[] queensInDiagonal1, final int[] queensInDiagonal2) {
        int maxConflicts = 0;
        int columnWithMaxConflicts = 0;

        for(int i = 0; i < queens.length; i++) {
            final int currentConflicts = getConflicts(queens[i], i, queensInRow, queensInDiagonal1, queensInDiagonal2);
            if(currentConflicts > maxConflicts) {
                maxConflicts = currentConflicts;
                columnWithMaxConflicts = i;
            } else if(currentConflicts == maxConflicts && random.nextInt(2) == 0) {
                columnWithMaxConflicts = i;
            }
        }

        return columnWithMaxConflicts;
    }

    private static int getRowWithMinConflicts(final int column, final int[] queens, final int[] queensInRow,
                                              final int[] queensInDiagonal1, final int[] queensInDiagonal2) {
        int minConflicts = 8;
        int rowsWithMinConflicts = 0;

        for(int i = 0; i < queens.length; i++) {
            if(queens[i] == i) {
                continue;
            }

            final int currentConflicts = getConflicts(i, column, queensInRow, queensInDiagonal1, queensInDiagonal2) + 3;

            if(currentConflicts < minConflicts) {
                minConflicts = currentConflicts;
                rowsWithMinConflicts = i;
            } else if(currentConflicts == minConflicts && random.nextInt(2) == 0) {
                rowsWithMinConflicts = i;
            }
        }

        return rowsWithMinConflicts;
    }

    private static boolean hasConflicts(final int[] queens, final int[] queensInRow,
                                        final int[] queensInDiagonal1, final int[] queensInDiagonal2) {
        for(int i = 0; i < queens.length; i++) {
            if(getConflicts(queens[i], i, queensInRow, queensInDiagonal1, queensInDiagonal2) > 0) {
                return true;
            }
        }

        return false;
    }

    private static int[] randomInit(final int[] queens, final int size) {
        for(int i = 0; i < size; i++) {
            queens[i] = i;
        }

        for (int i = 0; i < size; i++) {
            final int j = random.nextInt(size);
            final int swap = queens[i];
            queens[i] = queens[j];
            queens[j] = swap;
        }

        return queens;
    }

}
