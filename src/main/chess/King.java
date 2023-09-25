package chess;

import java.util.Collection;

public class King implements ChessPiece{
    ChessGame.TeamColor color;
    boolean hasMoved;
    PieceType pieceType;
    public King(ChessGame.TeamColor color) {
        this.color = color;
        hasMoved = false;
        pieceType = PieceType.KING;
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
        return null;
    }
}
