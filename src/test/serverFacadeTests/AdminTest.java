package serverFacadeTests;

import dataAccess.UserDAO;
import dataAccess.GameDAO;
import dataAccess.AuthDAO;
import models.User;
import org.junit.jupiter.api.Test;
import serverFacade.ServerFacade;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class AdminTest {
    @Test
    public void testClear() {
        try {
            var serverFacade = new ServerFacade("http://localhost:8080");
            serverFacade.addUser(new User("jap", "jap123", "jap@byu.edu"));
            serverFacade.clear();
            var userDAO = new UserDAO();
            var authDAO = new AuthDAO();
            var gameDAO = new GameDAO();
            assertTrue(userDAO.findAll().isEmpty(), "user data not empty");
            assertTrue(authDAO.findAll().isEmpty(), "auth data not empty");
            assertTrue(gameDAO.findAll().isEmpty(), "game data not empty");
        }
        catch(Exception e) {
            fail();
        }
    }
}
