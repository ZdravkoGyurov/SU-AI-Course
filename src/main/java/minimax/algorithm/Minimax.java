package minimax.algorithm;

import minimax.enums.Mark;
import minimax.enums.Player;
import minimax.tictactoe.Board;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public final class Minimax {

    public static void run() {

    }

    public static Set<Board> children(final Board position, final Player player) {
        final Set<Board> children = new LinkedHashSet<>();

        for(int i = 0; i < position.getBoard().length; i++) {
            for(int j = 0; j < position.getBoard().length; j++) {
                if(position.getBoard()[i][j] == Mark.BLANK.getValue()) {
                    final char[][] boardCopy = Arrays.stream(position.getBoard()).map(char[]::clone).toArray(char[][]::new);

                    if(player == Player.MAX) {
                        boardCopy[i][j] = Mark.CROSS.getValue();
                    } else {
                        boardCopy[i][j] = Mark.CIRCLE.getValue();
                    }

                    children.add(new Board(boardCopy));
                }
            }
        }

        return children;
    }

    // TODO
    private static int evaluate(final Board position) {
        return position.getAvailableSpaces();
    }

    public static int minimax(final Board position, final int depth, int alpha, int beta, final Player player) {
        if(depth == 0 || position.isGameOver()) {
            return evaluate(position);
        }

        if(player == Player.MAX) {
            int maxEval = Integer.MIN_VALUE;
            for(final Board child : children(position, player)) {
                final int eval = minimax(child, depth - 1, alpha, beta, Player.MIN);
                maxEval = Integer.max(maxEval, eval); // ???
                alpha = Integer.max(alpha, eval);
                if(alpha >= beta) {
                    break;
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for(final Board child : children(position, player)) {
                int eval = minimax(child, depth - 1, alpha, beta, Player.MAX);
                minEval = Integer.min(minEval, eval); // ???
                beta = Integer.min(beta, eval);
                if(alpha >= beta) {
                    break;
                }
            }
            return minEval;
        }
    }
}
