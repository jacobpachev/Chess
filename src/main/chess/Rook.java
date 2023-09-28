package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Rook implements ChessPiece{
    ChessGame.TeamColor color;
    boolean moved;
    PieceType pieceType;
    public Rook(ChessGame.TeamColor color) {
        this.color = color;
        moved = false;
        pieceType = PieceType.ROOK;
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
        if(row < 8) {
            for (int i = row + 1; i <= 8; i++) {
                ChessPosition curSquare = new MyPosition(i, col);
                if (board.getPiece(curSquare) == null) moves.add(new MyChessMove(myPosition, curSquare));
                else {
                    ChessPiece curPiece = board.getPiece(curSquare);
                    if (curPiece.getTeamColor() != getTeamColor())
                        moves.add(new MyChessMove(myPosition, curSquare));
                    break;
                }
            }
        }
        if(row > 1) {
            for (int i = row - 1; i >= 1; i--) {
                ChessPosition curSquare = new MyPosition(i, col);
                if (board.getPiece(curSquare) == null) moves.add(new MyChessMove(myPosition, curSquare));
                else {
                    ChessPiece curPiece = board.getPiece(curSquare);
                    if (curPiece.getTeamColor() != getTeamColor())
                        moves.add(new MyChessMove(myPosition, curSquare));
                    break;
                }
            }
        }
        if(col < 8) {
            for (int i = col + 1; i <= 8; i++) {
                ChessPosition curSquare = new MyPosition(row, i);
                if (board.getPiece(curSquare) == null) moves.add(new MyChessMove(myPosition, curSquare));
                else {
                    ChessPiece curPiece = board.getPiece(curSquare);
                    if (curPiece.getTeamColor() != getTeamColor())
                        moves.add(new MyChessMove(myPosition, curSquare));
                    break;
                }
            }
        }
        if(col > 1) {
            for (int i = col - 1; i >= 1; i--) {
                ChessPosition curSquare = new MyPosition(row, i);
                if (board.getPiece(curSquare) == null) moves.add(new MyChessMove(myPosition, curSquare));
                else {
                    ChessPiece curPiece = board.getPiece(curSquare);
                    if (curPiece.getTeamColor() != getTeamColor())
                        moves.add(new MyChessMove(myPosition, curSquare));
                    break;
                }
            }
        }

        return moves;
    }

    public boolean hasMoved() {
        return moved;
    }
    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public String toString() {
        return getTeamColor() == ChessGame.TeamColor.BLACK ? "r" :"R";
    }
}

