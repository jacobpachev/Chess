package chess;

public class Main {
    public static void main(String[] args) throws InvalidMoveException {
        MyBoard board = new MyBoard();
        board.resetBoard();
        ChessGame game = new GameImpl();
        game.setBoard(board);
        game.makeMove(new MyChessMove(new MyPosition(5,2), new MyPosition(5,4)));
        System.out.println(game.getBoard());
        game.makeMove(new MyChessMove(new MyPosition(7,1), new MyPosition(6,3)));
        System.out.println(game.getBoard());
        game.makeMove(new MyChessMove(new MyPosition(6,1), new MyPosition(5,2)));
        System.out.println(game.getBoard());
    }
}
