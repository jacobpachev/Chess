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
        if(row < 8 && col < 8) {
            while (i <= 8 && j <= 8) {
                i++;
                j++;
                if (checkSquare(board, myPosition, moves, i, j) == null) break;
                moves.add(checkSquare(board, myPosition, moves, i, j));
            }
        }
        if(row > 1 && col < 8) {
            while (i >= 1 && j <= 8) {
                i--;
                j++;
                if (checkSquare(board, myPosition, moves, i, j) == null) break;
                moves.add(checkSquare(board, myPosition, moves, i, j));
            }
        }
        if(row > 1 && col > 1) {
            while (i >= 1 && j >= 1) {
                i--;
                j--;
                if (checkSquare(board, myPosition, moves, i, j) == null) break;
                moves.add(checkSquare(board, myPosition, moves, i, j));
            }
        }
        if(row < 8 && col > 1) {
            while (i <= 8 && j >= 1) {
                i++;
                j--;
                if (checkSquare(board, myPosition, moves, i, j) == null) break;
                moves.add(checkSquare(board, myPosition, moves, i, j));
            }
        }
        return moves;
    }

    private ChessMove checkSquare(ChessBoard board, ChessPosition myPosition, List<ChessMove> moves, int i, int j) {
        ChessPosition curSquare = new MyPosition(i, j);
        if (board.getPiece(curSquare) == null) return new MyChessMove(myPosition, curSquare);
        else {
            ChessPiece curPiece = board.getPiece(curSquare);
            if (curPiece.getTeamColor() != getTeamColor() && curPiece.getPieceType() != PieceType.KING)
                return new MyChessMove(myPosition, curSquare);
            return null;
        }
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

