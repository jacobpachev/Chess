import chess.GameImpl;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import models.AuthToken;
import models.Game;
import models.User;
import passoffTests.obfuscatedTestClasses.TestServerFacade;
import passoffTests.testClasses.TestModels;

public class Main {
    public static void main(String[] args) throws DataAccessException {
        TestServerFacade serverFacade = new TestServerFacade("localhost", "8080");
        try {
            serverFacade.clear();
        }
        catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }
}
