package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Knight implements ChessPiece{
    ChessGame.TeamColor color;
    boolean moved;
    boolean firstMove;
    PieceType pieceType;
    public Knight(ChessGame.TeamColor color) {
        this.color = color;
        moved = false;
        firstMove = false;
        pieceType = PieceType.KNIGHT;
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
        List<Integer> rowIncrements = List.of(2,2,1,-1,-2,-2,1,-1);
        List<Integer> colIncrements = List.of(1,-1,2,2,1,-1,-2,-2);
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        for(int i = 0; i < rowIncrements.size(); i++) {
            int curRow = rowIncrements.get(i)+row;
            int curCol = colIncrements.get(i)+col;
            if(curRow <= 8 && curRow >= 1 && curCol <= 8 && curCol >= 1) {
                ChessPosition curSquare = new MyPosition(curRow, curCol);
                ChessPiece piece = board.getPiece(curSquare);
                if(piece != null) {
                    if(piece.getTeamColor() != getTeamColor()) moves.add(new MyChessMove(myPosition, curSquare));
                }
                else moves.add(new MyChessMove(myPosition, curSquare));
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
        return getTeamColor() == ChessGame.TeamColor.BLACK ? "n" :"N";
    }

    @Override
    public boolean isFirstMove() {
        return firstMove;
    }
    public void setFirstMove(boolean b) {
        firstMove = b;
    }
}
