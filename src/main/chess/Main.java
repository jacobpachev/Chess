package chess;

public class Main {
    public static void main(String[] args) throws InvalidMoveException {
        MyBoard board = new MyBoard();
        board.resetBoard();
        ChessGame game = new GameImpl();
        game.setBoard(board);
        game.makeMove(new MyChessMove(new MyPosition(5,2), new MyPosition(5,4)));
        game.makeMove(new MyChessMove(new MyPosition(5,4), new MyPosition(5,5)));

        game.setTeamTurn(ChessGame.TeamColor.BLACK);
        game.makeMove(new MyChessMove(new MyPosition(6,7), new MyPosition(6,5)));
        game.setTeamTurn(ChessGame.TeamColor.WHITE);
        System.out.println(game.validMoves(new MyPosition(5,5)));
//        game.makeMove(new MyChessMove(new MyPosition(5,5), new MyPosition(6,6)));
        System.out.println(game.getBoard());

    }
}
