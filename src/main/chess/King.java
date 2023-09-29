package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class King implements ChessPiece{
    ChessGame.TeamColor color;
    boolean moved;
    boolean firstMove;
    PieceType pieceType;
    public King(ChessGame.TeamColor color) {
        this.color = color;
        moved = false;
        firstMove = false;
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
        List<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        boolean otherKingNear = false;
        List<Integer> rowIncrements = new ArrayList<>(Arrays.asList(-1,-1,-1,0,0,1,1,1));
        List<Integer> colIncrements = new ArrayList<>(Arrays.asList(-1,0,1,-1,1,-1,0,1));
        for(int i = 0; i < rowIncrements.size(); i++) {
            int curRow = row+rowIncrements.get(i);
            int curCol = col+colIncrements.get(i);
            if(curRow <= 8 && curRow >= 1 && curCol <= 8 && curCol >= 1) {
                ChessPosition curSquare = new MyPosition(curRow, curCol);
                for (int j = 0; j < rowIncrements.size(); j++) {
                    int checkKingRow = curRow + rowIncrements.get(j);
                    int checkKingCol = curCol + colIncrements.get(j);
                    if(checkKingRow <= 8 && checkKingRow >= 1 && checkKingCol <= 8 && checkKingCol >= 1) {
                        ChessPosition checkSquare = new MyPosition(checkKingRow, checkKingCol);
                        ChessPiece possiblePiece = board.getPiece(checkSquare);
                        if (possiblePiece != null) {
                            if(possiblePiece.getPieceType() == PieceType.KING && possiblePiece.getTeamColor() != getTeamColor()) otherKingNear = true;
                        }
                    }
                }
                if (!otherKingNear) {
                    ChessPiece possiblePiece = board.getPiece(curSquare);
                    if(possiblePiece != null) {
                        if (board.getPiece(curSquare).getTeamColor() != getTeamColor())
                            moves.add(new MyChessMove(myPosition, curSquare));
                    }
                    else moves.add(new MyChessMove(myPosition, curSquare));
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
        return getTeamColor() == ChessGame.TeamColor.BLACK ? "k" :"K";
    }
    @Override
    public boolean isFirstMove() {
        return firstMove;
    }

    public void setFirstMove(boolean b) {
        firstMove = b;
    }
}
