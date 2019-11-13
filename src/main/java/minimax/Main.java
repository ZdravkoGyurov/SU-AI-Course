package minimax;

import minimax.algorithm.Minimax;
import minimax.enums.Mark;
import minimax.enums.Player;
import minimax.tictactoe.Board;

import java.util.Set;

public class Main {
    public static void main(String[] args) {
        final Board board = new Board();
        board.putMark(1, 3, Mark.CIRCLE);
        board.putMark(2, 2, Mark.CIRCLE);
        board.putMark(3, 1, Mark.CIRCLE);
        board.printBoard();
        System.out.println(board.isGameOver());


        System.out.println(Minimax.minimax(board, 20, Integer.MIN_VALUE, Integer.MAX_VALUE, Player.MAX));
    }
}
