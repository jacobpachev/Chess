package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

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
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        var moves = new ArrayList<ChessMove>();
        //TODO: Add castling and en passant functionality
        if(piece == null) return null;
        for(ChessMove move : piece.pieceMoves(board,startPosition)) {
            ChessPosition startPos = move.getStartPosition();
            ChessPosition endPos = move.getEndPosition();
            ChessPiece startPiece = board.getPiece(startPos);
            ChessPiece endSquarePiece = board.getPiece(endPos);
            tryMove(startPos, endPos, startPiece);
            if(!isInCheck(teamTurn)) {
                moves.add(move);
            }
            board.clearSquare(endPos);
            board.addPiece(endPos, endSquarePiece);
            board.addPiece(startPos, startPiece);
        }
        return moves;
    }

    @Override
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPos = move.getStartPosition();
        ChessPosition endPos = move.getEndPosition();
        ChessPiece startPiece = board.getPiece(startPos);
        TeamColor otherTeam = (teamTurn == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
        if(startPiece == null) throw new InvalidMoveException("No piece to move");
        if(startPiece.getTeamColor() != getTeamTurn()) throw new InvalidMoveException("Wrong color");
        if(validMoves(startPos).contains(move)) {
            ChessPiece endSquarePiece = board.getPiece(endPos);
            tryMove(startPos, endPos, startPiece);
            if(isInCheck(teamTurn)) {
                board.clearSquare(endPos);
                board.addPiece(endPos, endSquarePiece);
                board.addPiece(startPos, startPiece);
                throw new InvalidMoveException("Attempted to move while in check");
            }
            setTeamTurn(otherTeam);
        }
        else throw new InvalidMoveException("Illegal move");
    }

    public void tryMove(ChessPosition startPos, ChessPosition endPos, ChessPiece startPiece) {
        board.clearSquare(endPos);
        board.addPiece(endPos, startPiece);
        board.getPiece(endPos).setMoved(true);
        board.clearSquare(startPos);
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
        board.clearSquare(kingPos); // Pieces can't take a king, but can move to the square king was on if empty
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
        board.addPiece(kingPos, king);
        return inCheck;
    }

    @Override
    public boolean isInCheckmate(TeamColor teamColor) {
        return false;
    }

    @Override
    public boolean isInStalemate(TeamColor teamColor) {
        return false;
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
