package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Queen implements ChessPiece{
    ChessGame.TeamColor color;
    boolean moved;
    boolean firstMove;
    PieceType pieceType;
    public Queen(ChessGame.TeamColor color) {
        this.color = color;
        moved = false;
        firstMove = false;
        pieceType = PieceType.QUEEN;
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

        List<ChessMove> moves = new ArrayList<>();
        ChessPiece rook = new Rook(getTeamColor());
        ChessPiece bishop = new Bishop(getTeamColor());
        moves.addAll(rook.pieceMoves(board, myPosition));
        moves.addAll(bishop.pieceMoves(board, myPosition));
        return moves;
    }

    public boolean hasMoved() {
        return moved;
    }
    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    public String toString() {
        return getTeamColor() == ChessGame.TeamColor.BLACK ? "q" :"Q";
    }

    @Override
    public boolean isFirstMove() {
        return firstMove;
    }

    public void setFirstMove(boolean b) {
        firstMove = b;
    }
}
