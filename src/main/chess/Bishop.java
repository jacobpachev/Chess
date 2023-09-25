package chess;

import java.util.Collection;

public class Bishop implements ChessPiece{
    ChessGame.TeamColor color;
    PieceType pieceType;
    public Bishop(ChessGame.TeamColor color) {
        this.color = color;
        pieceType = PieceType.BISHOP;
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

