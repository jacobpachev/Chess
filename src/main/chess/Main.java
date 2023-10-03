package main.chess;

public class Main {
    public static void main(String[] args) throws InvalidMoveException {
        ChessBoard board = new MyBoard();
        GameImpl game = new GameImpl();
        board.addPiece(new MyPosition(8,5), new King(ChessGame.TeamColor.BLACK));
        board.addPiece(new MyPosition(8,1), new Rook(ChessGame.TeamColor.BLACK));
        board.addPiece(new MyPosition(8,8), new Rook(ChessGame.TeamColor.BLACK));
        board.addPiece(new MyPosition(4,6), new Rook(ChessGame.TeamColor.WHITE));game.setTeamTurn(ChessGame.TeamColor.BLACK);
        game.setBoard(board);
        System.out.println(game.validMoves(new MyPosition(8,5)));
        System.out.println(board);
        game.makeMove(new MyChessMove(new MyPosition(8,5), new MyPosition(8,7)));
        System.out.println(board);



    }
}
