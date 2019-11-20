package minimax.tictactoe;

import java.util.Arrays;
import java.util.Objects;

import minimax.enums.Mark;

public class Board {

    private final char[][] state;
    private int availableSpaces;

    public Board() {
        state = new char[3][3];
        availableSpaces = 9;
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state.length; j++) {
                state[i][j] = Mark.BLANK.getValue();
            }
        }
    }

    public Board(final char[][] board) {
        state = board;
        availableSpaces = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == Mark.BLANK.getValue()) {
                    availableSpaces++;
                }
            }
        }
    }

    public char[][] getBoard() {
        return state;
    }

    public int getAvailableSpaces() {
        return availableSpaces;
    }

    public boolean isGameOver() {
        for (int i = 0; i < state.length; i++) {
            if (state[i][0] != Mark.BLANK.getValue() && state[i][0] == state[i][1] && state[i][1] == state[i][2]) {
                return true;
            }

            if (state[0][i] != Mark.BLANK.getValue() && state[0][i] == state[1][i] && state[1][i] == state[2][i]) {
                return true;
            }

        }

        if (state[0][0] != Mark.BLANK.getValue() && state[0][0] == state[1][1] && state[1][1] == state[2][2]) {
            return true;
        }

        if (state[0][2] != Mark.BLANK.getValue() && state[0][2] == state[1][1] && state[1][1] == state[2][0]) {
            return true;
        }

        return availableSpaces == 0;
    }

    public void putMark(final int x, final int y, final Mark mark) {
        final int fixedX = x - 1;
        final int fixedY = y - 1;

        if (state[fixedX][fixedY] != Mark.BLANK.getValue()) {
            throw new IllegalArgumentException("This place is already taken!");
        }

        state[fixedX][fixedY] = mark.getValue();
        availableSpaces--;
    }

    public void print() {
        for (final char[] row : state) {
            for (final char cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Board board1 = (Board) o;

        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state.length; j++) {
                if (state[i][j] != board1.state[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(availableSpaces);
        result = 31 * result + Arrays.hashCode(state);
        return result;
    }
}