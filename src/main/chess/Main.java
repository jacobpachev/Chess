package chess;

public class Main {
    public static void main(String[] args) throws InvalidMoveException {
        ChessBoard board = new MyBoard();
        ChessGame game = new GameImpl();
        board.addPiece(new MyPosition(4,3), new Knight(ChessGame.TeamColor.BLACK));
        board.addPiece(new MyPosition(6,2), new Bishop(ChessGame.TeamColor.WHITE));
        board.addPiece(new MyPosition(2,6), new King(ChessGame.TeamColor.BLACK));
        game.setTeamTurn(ChessGame.TeamColor.BLACK);
        game.setBoard(board);
        System.out.println(game.validMoves(new MyPosition(4,3)));



    }
}
