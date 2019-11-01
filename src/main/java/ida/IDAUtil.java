package ida;

import java.util.*;

/**
 * Utility class with helper methods for IDA*
 */
public final class IDAUtil {

    private static final int FOUND = -1;
    private static final int ESTIMATED_MAXIMUM_POSSIBLE_F_VALUE = 100;
    private static int[] rowCoordinates;
    private static int[] colCoordinates;
    private static Node goal;
    private static Node reachedGoal;

    /**
     * Utility class not meant for instantiation
     */
    private IDAUtil() {
        // Util class
    }

    /**
     * Method for calculating heuristic estimate of the cost to travel to the goal from given board state
     *
     * @param board          board matrix to be evaluated
     * @param rowCoordinates array containing row coordinates of the tiles in the goal board state
     *                       (zero/blank tile not included)
     * @param colCoordinates array containing col coordinates of the tiles in the goal board state
     *                       (zero/blank tile not included)
     * @return heuristic estimate of the cost to travel to the goal from given board state
     */
    public static int heuristic(final int[][] board, final int[] rowCoordinates, final int[] colCoordinates) {
        int heuristic = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                final int currentElement = board[i][j];
                if (currentElement != 0 && currentElement <= board.length * board.length - 1) {
                    heuristic += Math.abs(i - rowCoordinates[currentElement - 1]) +
                            Math.abs(j - colCoordinates[currentElement - 1]);
                }
            }
        }
        return heuristic;
    }

    /**
     * Method for determining if board is solvable
     *
     * @param board matrix board to be evaluated
     * @return true if board is solvable
     */
    public static boolean isSolvable(final int[][] board) {
        final int[] puzzle = new int[board.length * board.length];
        int inversionCount = 0;
        int index = 0;
        int zeroRow = board.length - 1;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == 0) {
                    zeroRow = board.length - i;
                }
                puzzle[index++] = board[i][j];
            }
        }

        for (int i = 0; i < puzzle.length - 1; i++) {
            if (puzzle[i] != 0) {
                for (int j = i + 1; j < puzzle.length; j++) {
                    if (puzzle[i] > puzzle[j] && puzzle[j] != 0) {
                        inversionCount++;
                    }
                }
            }
        }

        final boolean oddGridWidth = board.length % 2 == 1;
        final boolean evenInversions = inversionCount % 2 == 0;
        final boolean evenZeroRow = zeroRow % 2 == 0;

        if (oddGridWidth && evenInversions) {
            return true;
        } else if (!oddGridWidth && evenZeroRow && !evenInversions) {
            return true;
        } else return !oddGridWidth && !evenZeroRow && evenInversions;
    }

    /**
     * Method for generating an array containing the row coordinates of all numbers/tiles in the goal position
     * (zero not included in the array). Zero is considered to be last in the matrix.
     *
     * @param boardSize        size of the board
     * @param numberOfElements number of all elements excluding zero/blank tile
     * @return rowCoordinates - array containing the row coordinates of all numbers/tiles in the goal position
     * (zero not included in the array)
     */
    public static int[] generateRowCoordinates(final int boardSize, final int numberOfElements) {
        final int[] rowCoordinates = new int[numberOfElements];
        for (int i = 0; i < numberOfElements; i++) {
            rowCoordinates[i] = i / boardSize;
        }
        IDAUtil.rowCoordinates = rowCoordinates;
        return rowCoordinates;
    }

    /**
     * Method for generating an array containing the column coordinates of all numbers/tiles in the goal position
     * (zero not included in the array). Zero is considered to be last in the matrix.
     *
     * @param boardSize        size of the board
     * @param numberOfElements number of all elements excluding zero/blank tile
     * @return colCoordinates - array containing the column coordinates of all numbers/tiles in the goal position
     * (zero not included in the array)
     */
    public static int[] generateColCoordinates(final int boardSize, final int numberOfElements) {
        final int[] colCoordinates = new int[numberOfElements];
        for (int i = 0; i < numberOfElements; i++) {
            colCoordinates[i] = i % boardSize;
        }
        IDAUtil.colCoordinates = colCoordinates;
        return colCoordinates;
    }

    /**
     * Method for fixing the row and column coordinates of all numbers/tiles if zero is specified not to be in the
     * default position (last in the matrix)
     *
     * @param rowCoordinates array containing the row coordinates of all numbers/tiles in the goal position
     * @param colCoordinates array containing the column coordinates of all numbers/tiles in the goal position
     * @param zeroIndex      position of the zero/blank tile in the goal position
     * @param boardSize      size of the board
     */
    public static void fixCoordinates(final int[] rowCoordinates, final int[] colCoordinates, final int zeroIndex, final int boardSize) {
        for (int i = 0; i < rowCoordinates.length; i++) {
            if (i >= zeroIndex) {
                colCoordinates[i]++;
                if (colCoordinates[i] == boardSize) {
                    colCoordinates[i] = 0;
                    rowCoordinates[i]++;
                }
            }
        }
    }

    /**
     * Method for generating a matrix representing a goal board.
     *
     * @param boardSize      size of the board
     * @param rowCoordinates array containing the row coordinates of all numbers/tiles in the goal position
     * @param colCoordinates array containing the column coordinates of all numbers/tiles in the goal position
     * @return matrix representing the goal board
     */
    public static int[][] generateGoalBoard(final int boardSize, final int[] rowCoordinates, final int[] colCoordinates) {
        final int[][] goalBoard = new int[boardSize][boardSize];

        for (int i = 0; i < rowCoordinates.length; i++) {
            goalBoard[rowCoordinates[i]][colCoordinates[i]] = i + 1;
        }

        return goalBoard;
    }

    /**
     * Method starting the IDA* algorithm used to solve the N puzzle
     *
     * @param initBoard     board matrix representing the starting position
     * @param zeroRow       row coordinate of the zero/blank tile in the starting position board
     * @param zeroCol       column coordinate of the zero/blank tile in the starting position board
     * @param rootHeuristic heuristic estimate of the cost to travel to the goal from the starting position board
     * @param goalBoard     board matrix representing the goal position
     * @param zeroIndex     index of the zero/blank tile in the goal position board
     */
    public static void runIDAStar(final int[][] initBoard, final int zeroRow, final int zeroCol, final int rootHeuristic, final int[][] goalBoard, final int zeroIndex) {
        final Node root = new Node(initBoard, zeroRow, zeroCol, rootHeuristic, null, "S");
        final int goalZeroRow = zeroIndex / goalBoard.length, goalZeroCol = zeroIndex / goalBoard.length;
        goal = new Node(goalBoard, goalZeroRow, goalZeroCol, 0, null, "G");

        int threshold = rootHeuristic;

        while (true) {
            int temp = search(root, 0, threshold);

            if (temp == FOUND) {
                // not printing start, not printing boards, printing directions
                printPath(true, true);
                return;
            }
            if (temp >= ESTIMATED_MAXIMUM_POSSIBLE_F_VALUE) {
                System.out.println("NOT FOUND");
                return;
            }
            threshold = temp;
        }
    }

    /**
     * Recursive method executing IDA* algorithm, iterating over the nodes, checking all possible further moves and
     * choosing the best option depending on the heuristic cost of the children.
     *
     * @param node      node the algorithm is starting from
     * @param g         the cost to travel from this node to the parent
     * @param threshold threshold for the current run of the algorithm
     * @return either -1 if goal was found or a new threshold value
     */
    private static int search(final Node node, final int g, final int threshold) {
        final int f = g + heuristic(node.getBoardSnapshot(), rowCoordinates, colCoordinates);

        if (f > threshold) {
            return f;
        }
        if (node.equals(goal)) {
            reachedGoal = node;
            return FOUND;
        }

        int min = Integer.MAX_VALUE;

        for (final Node tempnode : nextNodes(node)) {
            int temp = search(tempnode, g + 1, threshold);
            if (temp == FOUND) {
                return FOUND;
            }
            if (temp < min) {
                min = temp;
            }
        }
        return min;
    }

    /**
     * Method for printing the path from the start to the goal position
     *
     * @param printBoardPath     flag determining if board path should be printed
     * @param printDirectionPath flag determining if Direction path should be printed
     */
    private static void printPath(final boolean printBoardPath, final boolean printDirectionPath) {
        Node iter = reachedGoal;
        final Deque<int[][]> stackBoardPathToGoal = new ArrayDeque<>();
        final Deque<String> stackDirectionPathToGoal = new ArrayDeque<>();

        while (iter.getParent() != null) {
            stackBoardPathToGoal.push(iter.getBoardSnapshot());
            stackDirectionPathToGoal.push(iter.getDirection());
            iter = iter.getParent();
        }

        final Iterator<int[][]> boardIterator = stackBoardPathToGoal.iterator();
        final Iterator<String> directionIterator = stackDirectionPathToGoal.iterator();

        System.out.println(stackBoardPathToGoal.size());

        while (boardIterator.hasNext() && directionIterator.hasNext()) {
            if (printDirectionPath) {
                System.out.println(directionIterator.next());
            }
            if (printBoardPath) {
                printBoard(boardIterator.next());
            }
        }
    }

    /**
     * Method for printing easily readable board
     *
     * @param board matrix board to be printed
     */
    private static void printBoard(final int[][] board) {
        System.out.println("----------");
        for (int[] boardRow : board) {
            for (int i : boardRow) {
                System.out.print(i + " ");
            }
            System.out.println();
        }
        System.out.println("----------");
        System.out.println();
    }

    /**
     * Method for generating a list of all possible next steps from a given position
     *
     * @param node node representing the current state
     * @return list containing 2 to 4 elements depending on where the zero is positioned in the starting state
     */
    private static List<Node> nextNodes(final Node node) {
        final List<Node> nextNodes = new LinkedList<>();

        int oldRow = node.getZeroRow(), oldCol = node.getZeroCol();

        if (node.getZeroRow() != 0) { // zero can move up
            nextNodes.add(movedZeroNode(node, oldRow, oldCol, oldRow - 1, oldCol, "down")); // element moves down
        }
        if (node.getZeroRow() != node.getBoardSnapshot().length - 1) { // zero can move down
            nextNodes.add(movedZeroNode(node, oldRow, oldCol, oldRow + 1, oldCol, "up")); // element moves up
        }
        if (node.getZeroCol() != 0) { // zero can move left
            nextNodes.add(movedZeroNode(node, oldRow, oldCol, oldRow, oldCol - 1, "right")); // element moves right
        }
        if (node.getZeroCol() != node.getBoardSnapshot().length - 1) { // zero can move right
            nextNodes.add(movedZeroNode(node, oldRow, oldCol, oldRow, oldCol + 1, "left")); // element moves left
        }

        return nextNodes;
    }

    /**
     * Method for generating a node after zero position is moved
     *
     * @param node      node representing the current state
     * @param oldRow    zero row coordinate in the current state
     * @param oldCol    zero column coordinate in the current state
     * @param newRow    zero row coordinate in the new node/state
     * @param newCol    zero column coordinate in the new node/state
     * @param direction string representing which direction the zero is moving to
     * @return new child node of the current node with moved zero position
     */
    private static Node movedZeroNode(final Node node, final int oldRow, final int oldCol, final int newRow,
                                      final int newCol, final String direction) {
        final int[][] boardAfterMove = generateBoardCopy(node.getBoardSnapshot());
        swapZero(boardAfterMove, oldRow, oldCol, newRow, newCol);
        final int h = heuristic(boardAfterMove, rowCoordinates, colCoordinates);
        return new Node(boardAfterMove, newRow, newCol, h, node, direction);
    }

    /**
     * Method for generating a new instance of a board
     *
     * @param board matrix board to be cloned
     * @return board matrix, new instance
     */
    private static int[][] generateBoardCopy(final int[][] board) {
        return Arrays.stream(board).map(int[]::clone).toArray(int[][]::new);
    }

    /**
     * Method for swapping the zero position in a board
     *
     * @param board  matrix of the current state
     * @param oldRow zero row coordinate in the current state
     * @param oldCol zero column coordinate in the current state
     * @param newRow zero row coordinate in the new node/state
     * @param newCol zero column coordinate in the new node/state
     */
    private static void swapZero(final int[][] board, final int oldRow, final int oldCol, final int newRow, final int newCol) {
        final int temp = board[oldRow][oldCol];
        board[oldRow][oldCol] = board[newRow][newCol];
        board[newRow][newCol] = temp;
    }
}
