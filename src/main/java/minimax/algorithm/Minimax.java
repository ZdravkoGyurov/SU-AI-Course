package minimax.algorithm;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

import minimax.enums.Mark;
import minimax.enums.Player;
import minimax.tictactoe.Board;
import minimax.tictactoe.EvaluatedBoard;

public final class Minimax {

    private static final int MAX_WIN = 1;
    private static final int MIN_WIN = -1;
    private static final int DRAW = 0;

    private Minimax() {
        // Utility class
    }

    public static void run() {
        final Scanner scanner = new Scanner(System.in);
        System.out.println("Choose a mark(X/O): ");
        final String markChoice = scanner.next();

        final Mark playerMark;
        final Mark algorithmMark;

        if (markChoice.equals("X")) {
            playerMark = Mark.CROSS;
            algorithmMark = Mark.CIRCLE;
        } else {
            playerMark = Mark.CIRCLE;
            algorithmMark = Mark.CROSS;
        }

        Board board = new Board();
        board.print();
        boolean playerOnTurn = playerMark == Mark.CROSS;

        while (!board.isGameOver()) {
            if (playerOnTurn) {
                System.out.println("Choose a position(x, y): ");
                final int x = scanner.nextInt();
                final int y = scanner.nextInt();

                board.putMark(x, y, playerMark);
                board.print();
            } else {
                final Player algorithmPlayer = algorithmMark == Mark.CROSS ? Player.MAX : Player.MIN;
                board = minimax(board, 20, Integer.MIN_VALUE, Integer.MAX_VALUE, algorithmPlayer).getState();
                board.print();
            }
            playerOnTurn = !playerOnTurn;
        }

        System.out.println("GAME OVER");

        if (evaluate(board) == 0) {
            System.out.println("Draw!");
        } else if (!playerOnTurn) {
            System.out.println("Player won!");
        } else {
            System.out.println("Computer won!");
        }

        scanner.close();
    }

    private static Set<Board> children(final Board position, final Player player) {
        final Set<Board> children = new LinkedHashSet<>();

        for (int i = 0; i < position.getBoard().length; i++) {
            for (int j = 0; j < position.getBoard().length; j++) {
                if (position.getBoard()[i][j] == Mark.BLANK.getValue()) {
                    final char[][] boardCopy = Arrays.stream(position.getBoard()).map(char[]::clone)
                            .toArray(char[][]::new);

                    if (player == Player.MAX) {
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

    private static int evaluate(final Board position) {
        for (int i = 0; i < position.getBoard().length; i++) {
            if (position.getBoard()[i][0] == position.getBoard()[i][1]
                    && position.getBoard()[i][1] == position.getBoard()[i][2]) {
                if (position.getBoard()[i][0] == Mark.CROSS.getValue()) {
                    return MAX_WIN;
                } else if (position.getBoard()[i][0] == Mark.CIRCLE.getValue()) {
                    return MIN_WIN;
                }
            }

            if (position.getBoard()[0][i] == position.getBoard()[1][i]
                    && position.getBoard()[1][i] == position.getBoard()[2][i]) {
                if (position.getBoard()[0][i] == Mark.CROSS.getValue()) {
                    return MAX_WIN;
                } else if (position.getBoard()[0][i] == Mark.CIRCLE.getValue()) {
                    return MIN_WIN;
                }
            }
        }

        if (position.getBoard()[0][0] == position.getBoard()[1][1]
                && position.getBoard()[1][1] == position.getBoard()[2][2]) {
            if (position.getBoard()[0][0] == Mark.CROSS.getValue()) {
                return MAX_WIN;
            } else if (position.getBoard()[0][0] == Mark.CIRCLE.getValue()) {
                return MIN_WIN;
            }
        }
        if (position.getBoard()[0][2] == position.getBoard()[1][1]
                && position.getBoard()[1][1] == position.getBoard()[2][0]) {
            if (position.getBoard()[0][2] == Mark.CROSS.getValue()) {
                return MAX_WIN;
            } else if (position.getBoard()[0][2] == Mark.CIRCLE.getValue()) {
                return MIN_WIN;
            }
        }

        return DRAW;
    }

    private static EvaluatedBoard minimax(final Board position, final int depth, int alpha, int beta,
            final Player player) {
        if (depth == 0 || position.isGameOver()) {
            return new EvaluatedBoard(position, evaluate(position));
        }

        if (player == Player.MAX) {
            final EvaluatedBoard maxEval = new EvaluatedBoard(null, Integer.MIN_VALUE);
            for (final Board child : children(position, player)) {
                final EvaluatedBoard evalBoard = minimax(child, depth - 1, alpha, beta, Player.MIN);

                if (evalBoard.getEvaluation() > maxEval.getEvaluation()) {
                    maxEval.setEvaluation(evalBoard.getEvaluation());
                    maxEval.setState(child);
                }

                alpha = Integer.max(alpha, evalBoard.getEvaluation());
                if (alpha >= beta) {
                    break;
                }
            }
            return maxEval;
        } else {
            final EvaluatedBoard minEval = new EvaluatedBoard(null, Integer.MAX_VALUE);
            for (final Board child : children(position, player)) {
                final EvaluatedBoard evalBoard = minimax(child, depth - 1, alpha, beta, Player.MAX);
                if (evalBoard.getEvaluation() < minEval.getEvaluation()) {
                    minEval.setEvaluation(evalBoard.getEvaluation());
                    minEval.setState(child);
                }

                beta = Integer.min(beta, evalBoard.getEvaluation());
                if (alpha >= beta) {
                    break;
                }
            }
            return minEval;
        }
    }
}