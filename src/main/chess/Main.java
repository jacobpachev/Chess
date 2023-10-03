package chess;

public class Main {
    public static void main(String[] args) throws InvalidMoveException {
        ChessBoard board = new MyBoard();
        GameImpl game = new GameImpl();
        board.addPiece(new MyPosition(8,5), new King(ChessGame.TeamColor.BLACK));
        board.addPiece(new MyPosition(8,1), new Rook(ChessGame.TeamColor.BLACK));
        board.addPiece(new MyPosition(8,8), new Rook(ChessGame.TeamColor.BLACK));
        board.addPiece(new MyPosition(6,6), new Rook(ChessGame.TeamColor.WHITE));
        board.addPiece(new MyPosition(8,4), new Bishop(ChessGame.TeamColor.WHITE));
        board.addPiece(new MyPosition(3,2), new King(ChessGame.TeamColor.WHITE));
        game.setBoard(board);
        System.out.println(game.validMoves(new MyPosition(8,5)));
        System.out.println(game.getBoard());
    }
}
