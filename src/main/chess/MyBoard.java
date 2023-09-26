package chess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyBoard implements ChessBoard{
    HashMap<ChessPosition, ChessPiece> board;
    public MyBoard(){
        board = new HashMap<>();
    }
    @Override
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board.put(position, piece);
    }

    @Override
    public ChessPiece getPiece(ChessPosition position) {
        for(Map.Entry<ChessPosition, ChessPiece> entry : board.entrySet()) {
            if(entry.getKey().equals(position)) {
                return board.get(entry.getKey());
            }
        }
        return null;
    }

    @Override
    public void resetBoard() {
        board = new HashMap<>();
        for(int i = 1; i < 9; i++) {
            addPiece(new MyPosition(i, 2), new Pawn(ChessGame.TeamColor.WHITE));
            addPiece(new MyPosition(i, 7), new Pawn(ChessGame.TeamColor.BLACK));
        }
        addPiece(new MyPosition(3, 1), new Bishop(ChessGame.TeamColor.WHITE));
        addPiece(new MyPosition(3, 8), new Bishop(ChessGame.TeamColor.BLACK));
        addPiece(new MyPosition(6, 1), new Bishop(ChessGame.TeamColor.WHITE));
        addPiece(new MyPosition(6, 8), new Bishop(ChessGame.TeamColor.BLACK));

        addPiece(new MyPosition(2, 1), new Knight(ChessGame.TeamColor.WHITE));
        addPiece(new MyPosition(2, 8), new Knight(ChessGame.TeamColor.BLACK));
        addPiece(new MyPosition(7, 1), new Knight(ChessGame.TeamColor.WHITE));
        addPiece(new MyPosition(7, 8), new Knight(ChessGame.TeamColor.BLACK));

        addPiece(new MyPosition(1, 1), new Rook(ChessGame.TeamColor.WHITE));
        addPiece(new MyPosition(1, 8), new Rook(ChessGame.TeamColor.BLACK));
        addPiece(new MyPosition(8, 1), new Rook(ChessGame.TeamColor.WHITE));
        addPiece(new MyPosition(8, 8), new Rook(ChessGame.TeamColor.BLACK));

        addPiece(new MyPosition(5, 1), new King(ChessGame.TeamColor.WHITE));
        addPiece(new MyPosition(5, 8), new King(ChessGame.TeamColor.BLACK));
        addPiece(new MyPosition(4, 1), new Queen(ChessGame.TeamColor.WHITE));
        addPiece(new MyPosition(4, 8), new Queen(ChessGame.TeamColor.BLACK));
    }

    @Override
    public String toString() {
        StringBuilder boardString = new StringBuilder();
        for(int i = 8; i >= 1; i--) {
            for(int j = 1; j <= 8; j++) {
                boardString.append("|");
                if(getPiece(new MyPosition(j,i)) == null) boardString.append(" ");
                else boardString.append(getPiece(new MyPosition(j,i)).toString());
                if(j == 8) boardString.append("|\n");
            }
        }
        return boardString.toString();
    }
}
