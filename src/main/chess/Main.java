package chess;

public class Main {
    public static void main(String[] args) throws InvalidMoveException {
        MyBoard board = new MyBoard();
        board.addPiece(new MyPosition(5,2), new Bishop(ChessGame.TeamColor.BLACK));
        board.addPiece(new MyPosition(4,1), new Rook(ChessGame.TeamColor.BLACK));
        board.addPiece(new MyPosition(2,5), new Pawn(ChessGame.TeamColor.WHITE));
        board.addPiece(new MyPosition(7,4), new Queen(ChessGame.TeamColor.WHITE));
        System.out.println(board.getPiece(new MyPosition(5,2)).pieceMoves(board, new MyPosition(5,2)));
        System.out.println(board);



    }
}
