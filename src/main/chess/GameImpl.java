package chess;

import java.util.Collection;

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
        //TODO: Add castling functionality
        if(piece == null) return null;
        return piece.pieceMoves(board,startPosition);
    }

    @Override
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece startPiece = board.getPiece(move.getStartPosition());
        if(startPiece == null) throw new InvalidMoveException("No piece to move");
        if(startPiece.getTeamColor() != getTeamTurn()) throw new InvalidMoveException("Wrong color");
        if(validMoves(move.getStartPosition()).contains(move)) {
            board.clearSquare(move.getEndPosition());
            board.addPiece(move.getEndPosition(), startPiece);
            board.getPiece(move.getEndPosition()).setMoved(true);
            board.clearSquare(move.getStartPosition());
        }
        else throw new InvalidMoveException("Illegal move");
    }

    @Override
    public boolean isInCheck(TeamColor teamColor) {
        return false;
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
