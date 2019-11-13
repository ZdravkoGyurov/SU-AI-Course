package minimax.tictactoe;

import minimax.enums.Mark;

import java.util.Arrays;
import java.util.Objects;

public class Board {

    private final char[][] board;
    private int availableSpaces;

    public Board() {
        board = new char[3][3];
        availableSpaces = 9;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                board[i][j] = Mark.BLANK.getValue();
            }
        }
    }

    public Board(final char[][] board) {
        this.board = board;
        availableSpaces = 0;
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board.length; j++) {
                if(board[i][j] == Mark.BLANK.getValue()) {
                    availableSpaces++;
                }
            }
        }
    }

    public char[][] getBoard() {
        return board;
    }

    public int getAvailableSpaces() {
        return availableSpaces;
    }

    public boolean isGameOver() {
        for(int i = 0; i < board.length; i++) {
            if(board[i][0] != Mark.BLANK.getValue() && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return true;
            }

            if(board[0][i] != Mark.BLANK.getValue() && board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                return true;
            }

        }

        if(board[0][0] != Mark.BLANK.getValue() && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return true;
        }

        if(board[0][2] != Mark.BLANK.getValue() && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return true;
        }

        return availableSpaces == 0;
    }

    public void putMark(final int x, final int y, final Mark mark) {
        final int fixedX = x - 1;
        final int fixedY = y - 1;

        if (board[fixedX][fixedY] != Mark.BLANK.getValue()) {
            throw new IllegalArgumentException("This place is already taken!");
        }

        board[fixedX][fixedY] = mark.getValue();
        availableSpaces--;
    }

    public void printBoard() {
        for (final char[] row : board) {
            for (final char cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board1 = (Board) o;

        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board.length; j++) {
                if(board[i][j] != board1.board[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(availableSpaces);
        result = 31 * result + Arrays.hashCode(board);
        return result;
    }
}
