package ida;

/**
 * Class representing a single node/state in the IDA* algorithm
 */
public final class Node {
    private int[][] boardSnapshot;
    private int heuristic;
    private Node parent;
    private int zeroRow;
    private int zeroCol;
    private String direction;

    /**
     * Constructor with parameters
     *
     * @param boardSnapshot board in this node/state
     * @param zeroRow       row coordinate of the zero/blank tile
     * @param zeroCol       col coordinate of the zero/blank tile
     * @param heuristic     heuristic estimate of the cost to travel to the goal from this board state
     * @param direction     which direction was taken to generate this node
     * @param parent        reference to the parent node/previous state
     */
    public Node(final int[][] boardSnapshot, final int zeroRow, final int zeroCol, final int heuristic,
                final Node parent, final String direction) {
        this.boardSnapshot = boardSnapshot;
        this.zeroRow = zeroRow;
        this.zeroCol = zeroCol;
        this.heuristic = heuristic;
        this.parent = parent;
        this.direction = direction;
    }

    /**
     * Getter for boardSnapshot
     *
     * @return boardSnapshot - matrix/board in this node/state
     */
    public int[][] getBoardSnapshot() {
        return boardSnapshot;
    }

    /**
     * Getter for zeroRow
     *
     * @return zeroRow - row coordinate of the zero/blank tile
     */
    public int getZeroRow() {
        return zeroRow;
    }

    /**
     * Getter for zeroCol
     *
     * @return zeroCol - col coordinate of the zero/blank tile
     */
    public int getZeroCol() {
        return zeroCol;
    }

    /**
     * Getter for the parent node/previous state
     *
     * @return node parent of this node
     */
    public Node getParent() {
        return parent;
    }

    /**
     * Getter for the direction which was taken to generate this node
     *
     * @return string representing the direction which was taken to generate this node
     */
    public String getDirection() {
        return direction;
    }

    /**
     * Overridden equals method for comparing two nodes' board states, used for determining if IDA* reached the goal
     *
     * @param o node
     * @return true if nodes have the same board
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Node node = (Node) o;

        for (int i = 0; i < this.boardSnapshot.length; i++) {
            for (int j = 0; j < this.boardSnapshot.length; j++) {
                if (this.boardSnapshot[i][j] != node.getBoardSnapshot()[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
