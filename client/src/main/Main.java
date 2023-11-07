import chess.ChessBoard;
import chess.ChessGame;
import chess.GameImpl;
import chess.MyPosition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static ui.EscapeSequences.*;

public class Main {
    public static void main(String[] args) {
        var game = new GameImpl();
        game.getBoard().resetBoard();
        renderBoard(game.getBoard(), "white");
        renderBoard(game.getBoard(), "black");
    }

    static void renderBoard(ChessBoard board, String color) {
        var range = new ArrayList<>(Arrays.asList(8, 7, 6, 5, 4, 3, 2, 1));
        if (color.equals("white")) {
            range = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8));
        }
        var reversedRange = range;
        Collections.reverse(reversedRange);

        System.out.print(SET_TEXT_BOLD);
        System.out.print(SET_BG_COLOR_LIGHT_GREY);
        System.out.print(SET_TEXT_COLOR_BLACK);
        System.out.println("    a  b  c  d  e  f  g  h");
        for(var j : range) {
            System.out.print(SET_BG_COLOR_LIGHT_GREY);
            System.out.print(" "+j+" ");
            var offset = (j % 2 == 0) ? 1 : 0;
            for (var i : reversedRange) {
                var bg_color = ((i+offset) % 2 != 0) ? SET_BG_COLOR_BLACK : SET_BG_COLOR_WHITE;
                System.out.print(bg_color);
                var piece = board.getPiece(new MyPosition(j, i));
                var pieceStr = " ";
                if(piece != null) {
                    var pieceColor = (piece.getTeamColor() == ChessGame.TeamColor.WHITE) ? SET_TEXT_COLOR_RED : SET_TEXT_COLOR_GREEN;
                    pieceStr = piece.toString();
                    System.out.print(pieceColor);
                }
                System.out.print(" "+pieceStr+" ");
            }
            System.out.print(SET_BG_COLOR_LIGHT_GREY);
            System.out.print(SET_TEXT_COLOR_BLACK);
            System.out.print(" "+j+" \n");

        }
        System.out.print(SET_BG_COLOR_LIGHT_GREY);
        System.out.print(SET_TEXT_COLOR_BLACK);
        System.out.print("    a  b  c  d  e  f  g  h\n");
    }

}
