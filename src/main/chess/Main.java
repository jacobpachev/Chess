package chess;

public class Main {
    public static void main(String[] args) throws InvalidMoveException {
        ChessBoard board = new MyBoard();
        ChessGame game = new GameImpl();
        board.addPiece(new MyPosition(7,2), new Pawn(ChessGame.TeamColor.WHITE));
//        board.addPiece(new MyPosition(6,5), new Pawn(ChessGame.TeamColor.WHITE));
//        board.addPiece(new MyPosition(2,7), new Pawn(ChessGame.TeamColor.BLACK));
        game.setTeamTurn(ChessGame.TeamColor.WHITE);
        game.setBoard(board);
        System.out.println(board);
        System.out.println(game.validMoves(new MyPosition(7,2)));
        game.makeMove(new MyChessMove(ChessPiece.PieceType.QUEEN, new MyPosition(7,2), new MyPosition(8,2)));
//        System.out.println(game.validMoves(new MyPosition(2,7)));



    }
}
