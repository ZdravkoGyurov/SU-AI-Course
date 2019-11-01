package ida;

import java.util.Scanner;

/**
 * Entry point of the algorithm
 */
public final class IterativeDeepeningAStar {

    private static int zeroRow = 0;
    private static int zeroCol = 0;

    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        final int numberOfElements = scanner.nextInt();
        int zeroIndex = scanner.nextInt();
        // Set zeroIndex to be the last element of the board if input was -1.
        if (zeroIndex == -1) zeroIndex = numberOfElements;

        final int boardSize = (int) Math.sqrt(numberOfElements + 1);
        final int[][] rootBoard = new int[boardSize][boardSize];
        initBoard(rootBoard, boardSize, scanner);

        final int[] rowCoordinates = IDAUtil.generateRowCoordinates(boardSize, numberOfElements);
        final int[] colCoordinates = IDAUtil.generateColCoordinates(boardSize, numberOfElements);
        IDAUtil.fixCoordinates(rowCoordinates, colCoordinates, zeroIndex, boardSize);

        final int rootHeuristic = IDAUtil.heuristic(rootBoard, rowCoordinates, colCoordinates);

        final int[][] goalBoard = IDAUtil.generateGoalBoard(boardSize, rowCoordinates, colCoordinates);

        // Check if board is solvable. If it's solvable, run IDA*.
        if (IDAUtil.isSolvable(rootBoard)) {
            IDAUtil.runIDAStar(rootBoard, zeroRow, zeroCol, rootHeuristic, goalBoard, zeroIndex);
        } else {
            System.out.println("BOARD IS NOT SOLVABLE");
        }

    }

    /**
     * Method for initializing the root board matrix from user input. Also keeps track of zero/blank tile coordinates
     * and saves them in variables.
     *
     * @param board     reference of non-initialized board yet
     * @param boardSize size of the board
     * @param scanner   scanner taking user input
     */
    private static void initBoard(final int[][] board, final int boardSize, final Scanner scanner) {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = scanner.nextInt();
                if (board[i][j] == 0) {
                    zeroRow = i;
                    zeroCol = j;
                }
            }
        }
    }
}
