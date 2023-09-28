package chess;

public class Main {
    public static void main(String[] args) throws InvalidMoveException {
        MyBoard board = new MyBoard();
        board.addPiece(new MyPosition(4,4), new Pawn(ChessGame.TeamColor.BLACK));
        System.out.println(board.getPiece(new MyPosition(4,4)).pieceMoves(board, new MyPosition(4,4)));
        System.out.println(board);



    }
}
