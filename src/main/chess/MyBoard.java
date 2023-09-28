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
    public HashMap<ChessPosition, ChessPiece> getBoard() {return board;}
    @Override
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board.put(position, piece);
    }

    public void clearSquare(ChessPosition position) { board.put(position, null); }

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
            addPiece(new MyPosition(2, i), new Pawn(ChessGame.TeamColor.WHITE));
            addPiece(new MyPosition(7, i), new Pawn(ChessGame.TeamColor.BLACK));
        }
        addPiece(new MyPosition(1, 3), new Bishop(ChessGame.TeamColor.WHITE));
        addPiece(new MyPosition(8, 3), new Bishop(ChessGame.TeamColor.BLACK));
        addPiece(new MyPosition(1, 6), new Bishop(ChessGame.TeamColor.WHITE));
        addPiece(new MyPosition(8, 6), new Bishop(ChessGame.TeamColor.BLACK));

        addPiece(new MyPosition(1, 2), new Knight(ChessGame.TeamColor.WHITE));
        addPiece(new MyPosition(8, 2), new Knight(ChessGame.TeamColor.BLACK));
        addPiece(new MyPosition(1, 7), new Knight(ChessGame.TeamColor.WHITE));
        addPiece(new MyPosition(8, 7), new Knight(ChessGame.TeamColor.BLACK));

        addPiece(new MyPosition(1, 1), new Rook(ChessGame.TeamColor.WHITE));
        addPiece(new MyPosition(8, 1), new Rook(ChessGame.TeamColor.BLACK));
        addPiece(new MyPosition(1, 8), new Rook(ChessGame.TeamColor.WHITE));
        addPiece(new MyPosition(8, 8), new Rook(ChessGame.TeamColor.BLACK));

        addPiece(new MyPosition(1, 5), new King(ChessGame.TeamColor.WHITE));
        addPiece(new MyPosition(8, 5), new King(ChessGame.TeamColor.BLACK));
        addPiece(new MyPosition(1, 4), new Queen(ChessGame.TeamColor.WHITE));
        addPiece(new MyPosition(8, 4), new Queen(ChessGame.TeamColor.BLACK));
    }

    @Override
    public String toString() {
        StringBuilder boardString = new StringBuilder();
        for(int i = 8; i >= 1; i--) {
            for(int j = 1; j <= 8; j++) {
                boardString.append("|");
                if(getPiece(new MyPosition(i,j)) == null) boardString.append(" ");
                else boardString.append(getPiece(new MyPosition(i,j)).toString());
                if(j == 8) boardString.append("|\n");
            }
        }
        return boardString.toString();
    }
}
