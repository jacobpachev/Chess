package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
            if(!hasMoved) { //castling
                int direction = (getTeamColor() == ChessGame.TeamColor.WHITE) ? 1 : -1;
                ChessPiece bishopKSide = board.getPiece(new MyPosition(row+direction,col));
                ChessPiece knightKSide = board.getPiece(new MyPosition(row+direction*2,col));
                ChessPiece rookKSide = board.getPiece(new MyPosition(row+direction*3,col));

                ChessPiece queen = board.getPiece(new MyPosition(row-direction,col));
                ChessPiece bishopQSide = board.getPiece(new MyPosition(row-direction*2,col));
                ChessPiece knightQSide = board.getPiece(new MyPosition(row-direction*3,col));
                ChessPiece rookQSide = board.getPiece(new MyPosition(row-direction*4,col));

                if(bishopKSide == null && knightKSide == null && rookKSide != null) {
                    if(rookKSide.getPieceType() == PieceType.ROOK) {
                        Rook rook = (Rook) rookKSide;
                        if (!rook.hasMoved)
                            moves.add(new MyChessMove(myPosition, new MyPosition(row + direction * 2, col)));
                    }
                }
                if(bishopQSide == null && knightQSide == null && queen == null && rookQSide != null) {
                    if(rookQSide.getPieceType() == PieceType.ROOK) {
                        Rook rook = (Rook) rookQSide;
                        if (!rook.hasMoved)
                            moves.add(new MyChessMove(myPosition, new MyPosition(row + direction * 3, col)));
                    }
                }
            }
        }
        return moves;
    }

    public String toString() {
        return getTeamColor() == ChessGame.TeamColor.BLACK ? "k" :"K";
    }
}
