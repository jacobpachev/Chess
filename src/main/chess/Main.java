package chess;

public class Main {
    public static void main(String[] args) throws InvalidMoveException {
        ChessBoard board = new MyBoard();
        ChessGame game = new GameImpl();
        board.addPiece(new MyPosition(7,4), new King(ChessGame.TeamColor.BLACK));
        board.addPiece(new MyPosition(6,5), new Pawn(ChessGame.TeamColor.WHITE));
        board.addPiece(new MyPosition(2,7), new Pawn(ChessGame.TeamColor.BLACK));
        game.setTeamTurn(ChessGame.TeamColor.BLACK);
        game.setBoard(board);
        System.out.println(board);
        System.out.println(game.isInCheck(ChessGame.TeamColor.BLACK));
//        System.out.println(game.validMoves(new MyPosition(2,7)));



    }
}
