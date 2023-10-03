package main.chess;

import java.util.Objects;

public class MyChessMove implements ChessMove {
    private final ChessPiece.PieceType promotionPiece;
    private final ChessPosition start;
    private final ChessPosition end;

    public MyChessMove(ChessPiece.PieceType piece, ChessPosition start, ChessPosition end){
        this.promotionPiece = piece;
        this.start = start;
        this.end = end;
    }
    public MyChessMove(ChessPosition start, ChessPosition end){
        this.promotionPiece = null;
        this.start = start;
        this.end = end;
    }
    @Override
    public ChessPosition getStartPosition() {
        return start;
    }

    @Override
    public ChessPosition getEndPosition() {
        return end;
    }

    @Override
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionPiece;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyChessMove that = (MyChessMove) o;
        return promotionPiece == that.promotionPiece && start.equals(that.start) && end.equals(that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(promotionPiece, start, end);
    }

    public String toString() {
        return("Start: "+start+", End: "+end);
    }
}
