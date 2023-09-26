package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Bishop implements ChessPiece{
    ChessGame.TeamColor color;
    PieceType pieceType;
    boolean moved;
    public Bishop(ChessGame.TeamColor color) {
        this.color = color;
        pieceType = PieceType.BISHOP;
        moved = false;
    }
    @Override
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    @Override
    public PieceType getPieceType() {
        return pieceType;
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        List<ChessMove> moves = new ArrayList<>();
        int i = row;
        int j = col;
        while (i < 8 && j < 8) {
            i++;
            j++;
            if (checkSquare(board, myPosition, moves, i, j) == null) break;
            moves.add(checkSquare(board, myPosition, moves, i, j));
        }
        i = row;
        j = col;
        while (i > 1 && j < 8) {
            i--;
            j++;
            if (checkSquare(board, myPosition, moves, i, j) == null) break;
            moves.add(checkSquare(board, myPosition, moves, i, j));
        }
        i = row;
        j = col;
        while (i > 1 && j > 1) {
            i--;
            j--;
            if (checkSquare(board, myPosition, moves, i, j) == null) break;
            moves.add(checkSquare(board, myPosition, moves, i, j));
        }
        i = row;
        j = col;
        while (i < 8 && j > 1) {
            i++;
            j--;
            if (checkSquare(board, myPosition, moves, i, j) == null) break;
            moves.add(checkSquare(board, myPosition, moves, i, j));
        }
        return moves;
    }

    private ChessMove checkSquare(ChessBoard board, ChessPosition myPosition, List<ChessMove> moves, int i, int j) {
        ChessPosition curSquare = new MyPosition(i, j);
        ChessMove res = null;
        if (board.getPiece(curSquare) == null) res = new MyChessMove(myPosition, curSquare);
        else {
            ChessPiece curPiece = board.getPiece(curSquare);
            if (curPiece.getTeamColor() != getTeamColor() && curPiece.getPieceType() != PieceType.KING)
                res = new MyChessMove(myPosition, curSquare);
        }
        return res;
    }

    public String toString() {
        return getTeamColor() == ChessGame.TeamColor.BLACK ? "b" :"B";
    }

    public boolean hasMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }
}

