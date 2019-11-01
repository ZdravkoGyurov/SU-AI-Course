package nqueens;

import java.util.Random;

/**
 * Utility class for running Min Conflicts algorithm including some helper methods.
 */
public final class MinConflictsAlgorithm {

    private static final Random RANDOM = new Random();

    /**
     * Private default constructor to prevent instantiating the class
     */
    private MinConflictsAlgorithm() {
        // Utility class
    }

    /**
     * Method for running the Min Conflicts algorithm
     *
     * @param size size of the board
     */
    public static void run(final int size) {
        final int[] queens = new int[size];
        boolean foundAnswer = false;
        final int maxIterations = size;

        while(!foundAnswer) {
            randomInit(queens, size);
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

//        printBoard(queens);
    }

    /**
     * Method for updating the queens in each secondary diagonal after a queen is moved
     *
     * @param queensInDiagonal2 array containing number of queens in secondary diagonals
     * @param oldRow row of the queen before the move
     * @param oldCol column of the queen before the move
     * @param newRow row of the queen after the move
     * @param newCol column of the queen after the move
     */
    private static void updateQueensInDiagonal2(final int[] queensInDiagonal2, final int oldRow, final int oldCol, final int newRow, final int newCol) {
        queensInDiagonal2[oldRow + oldCol]--;
        queensInDiagonal2[newRow + newCol]++;
    }

    /**
     * Method for updating the queens in each main diagonal after a queen is moved
     *
     * @param queensInDiagonal1 array containing number of queens in main diagonals
     * @param size size of the board
     * @param oldRow row of the queen before the move
     * @param oldCol column of the queen before the move
     * @param newRow row of the queen after the move
     * @param newCol column of the queen after the move
     */
    private static void updateQueensInDiagonal1(final int[] queensInDiagonal1, final int size, final int oldRow, final int oldCol, final int newRow, final int newCol) {
        queensInDiagonal1[oldCol - oldRow + size - 1]--;
        queensInDiagonal1[newCol - newRow + size - 1]++;
    }

    /**
     * Method for updating the number of queens in each row after a queen is moved
     *
     * @param queensInRow array containing number of queens in each row
     * @param oldRow row of the queen before the move
     * @param newRow row of the queen after the move
     */
    private static void updateQueensInRow(final int[] queensInRow, final int oldRow, final int newRow) {
        queensInRow[oldRow]--;
        queensInRow[newRow]++;
    }

    /**
     * Method for generating an array containing number of queens in secondary diagonals
     *
     * @param queens array with all queens' rows
     * @return array containing number of queens in secondary diagonals
     */
    private static int[] getQueensInDiagonal2(final int[] queens) {
        final int[] queensInDiagonal2 = new int[queens.length * 2 - 1];

        for(int i = 0; i < queens.length; i++) {
            queensInDiagonal2[i + queens[i]]++;
        }

        return queensInDiagonal2;
    }

    /**
     * Method for generating an array containing number of queens in main diagonals
     *
     * @param queens array with all queens' rows
     * @return array containing number of queens in main diagonals
     */
    private static int[] getQueensInDiagonal1(final int[] queens) {
        final int[] queensInDiagonal1 = new int[queens.length * 2 - 1];

        for(int i = 0; i < queens.length; i++) {
            queensInDiagonal1[i - queens[i] + queens.length - 1]++;
        }

        return queensInDiagonal1;
    }

    /**
     * Method for generating an array containing number of queens in each row
     *
     * @param queens array with all queens' rows
     * @return array containing number of queens in each row
     */
    private static int[] getQueensInRow(final int[] queens) {
        final int[] queensInRow = new int[queens.length];

        for(int queen : queens) {
            queensInRow[queen]++;
        }

        return queensInRow;
    }

    /**
     * Method for printing a 2d chess board to the standard output where '*' marks a queen and '_' marks a blank space.
     *
     * @param queens array with all queens' rows
     */
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

    /**
     * Method for determining how many conflicts a queen has. This method can also be used for blank spaces but 3 conflicts must be added to the final result.
     *
     * @param row row of the queen
     * @param col column of the queen
     * @param queensInRow array containing number of queens in each row
     * @param queensInDiagonal1 array containing number of queens in main diagonals
     * @param queensInDiagonal2 array containing number of queens in secondary diagonals
     * @return number of conflicts the queen has
     */
    private static int getConflicts(final int row, final int col, final int[] queensInRow, final int[] queensInDiagonal1, final int[] queensInDiagonal2) {
        return queensInRow[row] + queensInDiagonal1[col - row + queensInRow.length - 1] + queensInDiagonal2[row + col] - 3;
    }

    /**
     * Method for finding the column of the queen with most conflicts
     *
     * @param queens array with all queens' rows
     * @param queensInRow array containing number of queens in each row
     * @param queensInDiagonal1 array containing number of queens in main diagonals
     * @param queensInDiagonal2 array containing number of queens in secondary diagonals
     * @return column with most conflicts
     */
    private static int getColumnWithMaxConflicts(final int[] queens, final int[] queensInRow, final int[] queensInDiagonal1, final int[] queensInDiagonal2) {
        int maxConflicts = 0;
        int columnWithMaxConflicts = 0;

        for(int i = 0; i < queens.length; i++) {
            final int currentConflicts = getConflicts(queens[i], i, queensInRow, queensInDiagonal1, queensInDiagonal2);
            if(currentConflicts > maxConflicts) {
                maxConflicts = currentConflicts;
                columnWithMaxConflicts = i;
            } else if(currentConflicts == maxConflicts && RANDOM.nextInt() % 16 == 3) {
                columnWithMaxConflicts = i;
            }
        }

        return columnWithMaxConflicts;
    }

    /**
     * Method for determining which row in a given column would make least conflicts if the queen in this column is moved there.
     *
     * @param column selected column in which the method determines the best row
     * @param queens array with all queens' rows
     * @param queensInRow array containing number of queens in each row
     * @param queensInDiagonal1 array containing number of queens in main diagonals
     * @param queensInDiagonal2 array containing number of queens in secondary diagonals
     * @return row with least conflicts
     */
    private static int getRowWithMinConflicts(final int column, final int[] queens, final int[] queensInRow, final int[] queensInDiagonal1, final int[] queensInDiagonal2) {
        int minConflicts = getConflicts(queens[column], column, queensInRow, queensInDiagonal1, queensInDiagonal2);
        int rowWithMinConflicts = queens[column];

        for(int i = 0; i < queens.length; i++) {
            if(queens[column] == i) {
                continue;
            }

            final int currentConflicts = getConflicts(i, column, queensInRow, queensInDiagonal1, queensInDiagonal2) + 3;

            if(currentConflicts < minConflicts) {
                minConflicts = currentConflicts;
                rowWithMinConflicts = i;
            } else if(currentConflicts == minConflicts && RANDOM.nextInt() % 16 == 3) {
                rowWithMinConflicts = i;
            }
        }

        return rowWithMinConflicts;
    }

    /**
     * Method for checking if there are any conflicts on the board. Used to determine if goal is reached.
     *
     * @param queens array with all queens' rows
     * @param queensInRow array containing number of queens in each row
     * @param queensInDiagonal1 array containing number of queens in main diagonals
     * @param queensInDiagonal2 array containing number of queens in secondary diagonals
     * @return true if any conflict is found
     */
    private static boolean hasConflicts(final int[] queens, final int[] queensInRow, final int[] queensInDiagonal1, final int[] queensInDiagonal2) {
        for(int i = 0; i < queens.length; i++) {
            if(getConflicts(queens[i], i, queensInRow, queensInDiagonal1, queensInDiagonal2) > 0) {
                return true;
            }
        }

        return false;
    }

    /**
     * Method for randomly initializing the queens array with scrambled rows [0, ..., size] to start with minimum conflicts
     *
     * @param queens the array before being scrambled
     * @param size size of the board
     */
    private static void randomInit(final int[] queens, final int size) {
        for(int i = 0; i < size; i++) {
            queens[i] = i;
        }

        for (int i = 0; i < size; i++) {
            final int j = RANDOM.nextInt(size);
            final int swap = queens[i];
            queens[i] = queens[j];
            queens[j] = swap;
        }
    }
}
