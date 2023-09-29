package chess;

public class Main {
    public static void main(String[] args) throws InvalidMoveException {
        ChessBoard board = new MyBoard();
        GameImpl game = new GameImpl();
        board.addPiece(new MyPosition(4,6), new King(ChessGame.TeamColor.WHITE));
        board.addPiece(new MyPosition(3,3), new Rook(ChessGame.TeamColor.BLACK));
        board.addPiece(new MyPosition(7,3), new Bishop(ChessGame.TeamColor.BLACK));
        board.addPiece(new MyPosition(5,4), new Pawn(ChessGame.TeamColor.BLACK));
        board.addPiece(new MyPosition(7,4), new Queen(ChessGame.TeamColor.BLACK));
        board.addPiece(new MyPosition(2,5), new Knight(ChessGame.TeamColor.BLACK));
        board.addPiece(new MyPosition(5,8), new King(ChessGame.TeamColor.BLACK));
        game.setTeamTurn(ChessGame.TeamColor.WHITE);
        game.setBoard(board);
        System.out.println(board);
        System.out.println(game.noMoves(ChessGame.TeamColor.WHITE, false, new King(ChessGame.TeamColor.WHITE)));



    }
}
