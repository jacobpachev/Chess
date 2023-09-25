package chess;
import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        MyBoard board = new MyBoard();
//        board.resetBoard();
        ChessPosition pos = new MyPosition(8,8);
        board.addPiece(pos, new Rook(ChessGame.TeamColor.WHITE));
        board.addPiece(new MyPosition(7,8), new King(ChessGame.TeamColor.BLACK));
        System.out.println(board.getPiece(pos).pieceMoves(board, pos));
    }
}
