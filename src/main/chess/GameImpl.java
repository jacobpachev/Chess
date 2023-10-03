package main.chess;

import java.util.ArrayList;
import java.util.List;

public class GameImpl implements ChessGame{
    TeamColor teamTurn;
    ChessBoard board;
    public GameImpl(){
        teamTurn = TeamColor.WHITE;
        board = new MyBoard();
    }
    @Override
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    @Override
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    @Override
    public List<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        var moves = new ArrayList<ChessMove>();
        if(piece == null) return null;
        for(ChessMove move : piece.pieceMoves(board,startPosition)) {
            ChessPosition startPos = move.getStartPosition();
            ChessPosition endPos = move.getEndPosition();
            ChessPiece startPiece = board.getPiece(startPos);
            boolean startPieceMoved = startPiece.hasMoved();
            ChessPiece endSquarePiece = board.getPiece(endPos);
            if(startPiece.getPieceType() == ChessPiece.PieceType.KING) {
                if(endPos.getColumn() == startPos.getColumn()+2) {
                    System.out.println(move);
                    if(canCastleMove(startPos, endPos, startPiece, new MyPosition(endPos.getRow(), endPos.getColumn()+1))){
                        moves.add(move);
                        board.addPiece(startPos, startPiece);
                        board.addPiece(new MyPosition(endPos.getRow(), endPos.getColumn()+1), board.getPiece(new MyPosition(endPos.getRow(), startPos.getColumn()+1)));
                        board.clearSquare(endPos);
                        board.clearSquare(new MyPosition(endPos.getRow(), endPos.getColumn()-1));
                    }
                }
                else if(endPos.getColumn() == startPos.getColumn()-2) {
                    if(canCastleMove(startPos, endPos, startPiece, new MyPosition(endPos.getRow(), endPos.getColumn()-2))) {
                        moves.add(move);
                        board.addPiece(startPos, startPiece);
                        board.addPiece(new MyPosition(endPos.getRow(), endPos.getColumn()-2), board.getPiece(new MyPosition(endPos.getRow(), startPos.getColumn()-1)));
                        board.clearSquare(endPos);
                        board.clearSquare(new MyPosition(endPos.getRow(), endPos.getColumn()+1));
                    }
                }
            }
            else tryMove(startPos, endPos, startPiece);
            if(!isInCheck(piece.getTeamColor())) {
                if (endSquarePiece != null) {
                    if (endSquarePiece.getPieceType() != ChessPiece.PieceType.KING) moves.add(move);
                }
                else moves.add(move);
            }
            board.clearSquare(endPos);
            board.addPiece(endPos, endSquarePiece);
            board.addPiece(startPos, startPiece);
            if(!startPieceMoved) startPiece.setMoved(false);
        }
        return moves;
    }

    @Override
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPos = move.getStartPosition();
        ChessPosition endPos = move.getEndPosition();
        boolean edge = (teamTurn == ChessGame.TeamColor.WHITE) ? endPos.getRow() == 8 : endPos.getRow() == 1;
        ChessPiece startPiece = board.getPiece(startPos);
        TeamColor otherTeam = (teamTurn == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
        if(startPiece == null) throw new InvalidMoveException("No piece to move");
        if(startPiece.getTeamColor() != getTeamTurn()) throw new InvalidMoveException("Wrong color");
        if(validMoves(startPos).contains(move)) {
            ChessPiece endSquarePiece = board.getPiece(endPos);
            if(startPiece.getPieceType() == ChessPiece.PieceType.PAWN && edge) {
                    board.clearSquare(endPos);
                    switch(move.getPromotionPiece()) {
                        case KNIGHT -> board.addPiece(endPos, new Knight(teamTurn));
                        case BISHOP -> board.addPiece(endPos, new Bishop(teamTurn));
                        case ROOK -> board.addPiece(endPos, new Rook(teamTurn));
                        case QUEEN -> board.addPiece(endPos, new Queen(teamTurn));
                    }
                    board.getPiece(endPos).setMoved(true);
                    board.clearSquare(startPos);
            }
            else if(startPiece.getPieceType() == ChessPiece.PieceType.KING) {
                if(isInCheck(teamTurn)) throw new InvalidMoveException("Tried to castle in check");
                if(endPos.getColumn() == startPos.getColumn()+2) {

                    if(!canCastleMove(startPos, endPos, startPiece, new MyPosition(endPos.getRow(), endPos.getColumn()+1)))
                        throw new InvalidMoveException("Tried to castle through check");
                }
                else if(endPos.getColumn() == startPos.getColumn()-2) {
                    if(!canCastleMove(startPos, endPos, startPiece, new MyPosition(endPos.getRow(), endPos.getColumn()-2)))
                        throw new InvalidMoveException("Tried to castle through check");
                }
            }
            else tryMove(startPos, endPos, startPiece);
            int direction = startPiece.getTeamColor() == ChessGame.TeamColor.WHITE ? 1 : -1;
            if(startPiece.getPieceType() == ChessPiece.PieceType.PAWN && startPos.getRow()+direction == endPos.getRow() && endSquarePiece == null) {
                board.clearSquare(new MyPosition(endPos.getRow()-direction, endPos.getColumn()));
            }
            if(isInCheck(teamTurn)) {
                board.clearSquare(endPos);
                board.addPiece(endPos, endSquarePiece);
                board.addPiece(startPos, startPiece);
                throw new InvalidMoveException("Attempted to move while in check");
            }
            checkEnPassant(teamTurn);
            setTeamTurn(otherTeam);
        }
        else {
            throw new InvalidMoveException("Illegal move");
        }
    }

    public void tryMove(ChessPosition startPos, ChessPosition endPos, ChessPiece startPiece) {
        board.clearSquare(endPos);
        board.addPiece(endPos, startPiece);
        board.getPiece(endPos).setMoved(true);
        board.clearSquare(startPos);
    }

    boolean canCastleMove(ChessPosition startPos, ChessPosition endPos, ChessPiece startPiece, ChessPosition rookSquare) {
        int rookEndCol = (rookSquare.getColumn() == 8) ? 6 : 4;
        if(rookSquare.getColumn() == 8) {
            for(int i = 7; i >5; i--) {
                if(hasChecksCastle(startPos, rookSquare, startPiece, i)) return false;
            }
        }
        else {
            for(int i = 3; i <5; i++) {
                if(hasChecksCastle(startPos, rookSquare, startPiece, i)){
                    return false;
                }
            }
        }
        ChessPiece rook = board.getPiece(rookSquare);
        board.addPiece(endPos, startPiece);
        board.clearSquare(startPos);
        board.addPiece(new MyPosition(endPos.getRow(), rookEndCol), rook);
        board.clearSquare(rookSquare);
        return true;
    }
    boolean hasChecksCastle(ChessPosition startPos, ChessPosition rookSquare, ChessPiece startPiece, int i) {
        tryMove(startPos, new MyPosition(rookSquare.getRow(), i), startPiece);
        if (isInCheck(teamTurn)) {
            board.clearSquare(new MyPosition(rookSquare.getRow(), i));
            board.addPiece(startPos, startPiece);
            return true;
        }
        board.clearSquare(new MyPosition(rookSquare.getRow(), i));
        board.addPiece(startPos, startPiece);
        return false;
    }

    void checkEnPassant(TeamColor turn) {
        var allBoard = List.copyOf(board.getBoard().keySet());
        for (ChessPosition pos : allBoard) {
            ChessPiece curPiece = board.getPiece(pos);
            if (curPiece != null) {
                if(curPiece.getTeamColor().equals(turn) && curPiece.getPieceType().equals(ChessPiece.PieceType.PAWN)) {
                    for(ChessMove move : validMoves(pos)) {
                        ChessPosition startPos = move.getStartPosition();
                        ChessPosition endPos = move.getEndPosition();
                        int direction = (turn == ChessGame.TeamColor.WHITE) ? 1 : -1;
                        int rightRow = (curPiece.getTeamColor() == ChessGame.TeamColor.WHITE) ? 5 : 4;
                        if (startPos.getColumn()  != endPos.getRow() && board.getPiece(endPos) == null && startPos.getRow() == rightRow) {
                            board.getPiece(new MyPosition(endPos.getRow()-direction, endPos.getColumn())).setFirstMove(false);
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean isInCheck(TeamColor teamColor) {
        TeamColor otherTeam = (teamColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
        boolean inCheck = false;
        ChessPosition kingPos = null;
        ChessPiece king = null;
        for(ChessPosition pos : board.getBoard().keySet()) {
            ChessPiece curPiece = board.getPiece(pos);
            if(curPiece != null) {
                if (curPiece.getPieceType().equals(ChessPiece.PieceType.KING) && curPiece.getTeamColor().equals(teamColor)) {
                    kingPos = pos;
                    king = curPiece;
                }
            }
        }
        if(king == null) return inCheck;
        for(ChessPosition pos : board.getBoard().keySet()) {
            ChessPiece curPiece = board.getPiece(pos);
            if(curPiece != null) {
                if (curPiece.getTeamColor().equals(otherTeam)) {
                    for (ChessMove move : curPiece.pieceMoves(board, pos)) {
                        if (move.getEndPosition().equals(kingPos)) {
                            inCheck = true;
                        }
                    }
                }
            }
        }
        return inCheck;
    }

    @Override
    public boolean isInCheckmate(TeamColor teamColor) {
        return isInCheck(teamColor) && noMoves(teamColor, false, new King(teamColor));
    }

    @Override
    public boolean isInStalemate(TeamColor teamColor) {
        return !isInCheck(teamColor) && noMoves(teamColor, true, null);
    }

    public boolean noMoves(TeamColor teamColor, boolean checkAllPieces, ChessPiece piece) {
        var allBoard = List.copyOf(board.getBoard().keySet());
        for (ChessPosition pos : allBoard) {
            ChessPiece curPiece = board.getPiece(pos);
            if (curPiece != null) {
                if (curPiece.getTeamColor().equals(teamColor)) {
                    if (checkAllPieces) {
                        if (!validMoves(pos).isEmpty()) return false;
                    } else {
                        if (!validMoves(pos).isEmpty() && curPiece.equals(piece)) return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    @Override
    public ChessBoard getBoard() {
        return board;
    }
}
