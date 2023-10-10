import chess.GameImpl;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import models.AuthToken;
import models.Game;
import models.User;

public class Main {
    public static void main(String[] args) throws DataAccessException {
        Game game = new Game("Name", new GameImpl());
        System.out.println(game.getGameID());

    }
}
