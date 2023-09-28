package chess;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

public class Pawn implements ChessPiece{
    ChessGame.TeamColor color;
    boolean moved;
    boolean firstMove; //for en-passant
    PieceType pieceType;
    public Pawn(ChessGame.TeamColor color) {
        this.color = color;
        moved = false;
        firstMove = false;
        pieceType = PieceType.PAWN;
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
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int direction = getTeamColor() == ChessGame.TeamColor.WHITE ? 1 : -1;
        boolean moved = (getTeamColor() == ChessGame.TeamColor.WHITE) ? row != 2 : row != 7;
        boolean edge = (getTeamColor() == ChessGame.TeamColor.WHITE) ? row == 7 : row == 2;
        ChessPosition squareRight = new MyPosition(row, col+1);
        ChessPosition squareLeft = new MyPosition(row, col-1);
        ChessPiece side1;
        ChessPiece side2;
        if(board.getPiece(squareRight) != null){
            side1 = board.getPiece(squareRight);
            if(side1.getTeamColor() != getTeamColor()) {
                moves.add(new MyChessMove(myPosition, new MyPosition(row+direction,col+1)));
            }
        }
        if(board.getPiece(squareLeft) != null){
            side2 = board.getPiece(squareLeft);
            if(side2.getTeamColor() != getTeamColor()) {
                moves.add(new MyChessMove(myPosition, new MyPosition(row+direction,col-1)));
            }
        }
        ChessPosition square1 = new MyPosition(row+direction, col+1);
        ChessPosition square2 = new MyPosition(row+direction, col-1);
        ChessPiece diagonal1 = board.getPiece(square1);
        ChessPiece diagonal2 = board.getPiece(square2);
        if(diagonal1 != null) {
            if (diagonal1.getTeamColor() != getTeamColor()){
                if(!edge) moves.add(new MyChessMove(myPosition, square1));
                else {
                    moves.addAll(addAllPromotion(myPosition, square1));
                }
            }
        }
        if(diagonal2 != null) {
            if (diagonal2.getTeamColor() != getTeamColor()) {
                if(!edge) moves.add(new MyChessMove(myPosition, square2));
                else {
                    moves.addAll(addAllPromotion(myPosition, square2));
                }
            }
        }
        ChessPosition squareAhead = new MyPosition(row+direction, col);
        if(board.getPiece(squareAhead) != null)
            return moves;
        if(!edge) moves.add(new MyChessMove(myPosition, squareAhead));
        else {
            moves.addAll(addAllPromotion(myPosition, squareAhead));
        }
        if(!moved) {
            squareAhead = new MyPosition(row+2*direction,col);
            if(board.getPiece(squareAhead) == null) moves.add(new MyChessMove(myPosition, squareAhead));
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
        return getTeamColor() == ChessGame.TeamColor.BLACK ? "p" :"P";
    }

    public List<ChessMove> addAllPromotion(ChessPosition pos, ChessPosition endPos) {
        var moves = new ArrayList<ChessMove>();
        moves.add(new MyChessMove(PieceType.KNIGHT, pos, endPos));
        moves.add(new MyChessMove(PieceType.BISHOP, pos, endPos));
        moves.add(new MyChessMove(PieceType.ROOK, pos, endPos));
        moves.add(new MyChessMove(PieceType.QUEEN, pos, endPos));
        return moves;
    }
}
