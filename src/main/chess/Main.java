package chess;

public class Main {
    public static void main(String[] args) {
        MyBoard board = new MyBoard();
        //board.resetBoard();
        ChessPosition pos = new MyPosition(5,5);
        board.addPiece(pos, new Knight(ChessGame.TeamColor.WHITE));
        System.out.println(board.getPiece(pos).pieceMoves(board, pos));
        System.out.println(board);
    }
}
