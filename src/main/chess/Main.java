package chess;

public class Main {
    public static void main(String[] args) throws InvalidMoveException {
        ChessBoard board = new MyBoard();
        GameImpl game = new GameImpl();
        board.addPiece(new MyPosition(7,3), new Pawn(ChessGame.TeamColor.BLACK));
        board.addPiece(new MyPosition(5,2), new Pawn(ChessGame.TeamColor.WHITE));
//        board.addPiece(new MyPosition(5,6), new Pawn(ChessGame.TeamColor.WHITE));
        game.setTeamTurn(ChessGame.TeamColor.BLACK);
        game.setBoard(board);
        game.makeMove(new MyChessMove(new MyPosition(7,3), new MyPosition(5,3)));
        System.out.println(game.validMoves(new MyPosition(5,2)));
        game.makeMove(new MyChessMove(new MyPosition(5,2), new MyPosition(6,3)));
        game.setTeamTurn(ChessGame.TeamColor.WHITE);
        System.out.println(board);



    }
}
