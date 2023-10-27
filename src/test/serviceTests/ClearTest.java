package serviceTests;

import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import dataAccess.AuthDAO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import services.AdminService;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClearTest {
    @Test
    @DisplayName("Successful Clear")
    public void successClear() {
        var clearService = new AdminService();
        clearService.clear();
        try {
            var userDAO = new UserDAO();
            var authDAO = new AuthDAO();
            var gameDAO = new GameDAO();
            assertTrue(userDAO.findAll().isEmpty(), "user data not empty");
            assertTrue(authDAO.findAll().isEmpty(), "auth data not empty");
            assertTrue(gameDAO.findAll().isEmpty(), "game data not empty");
        }
        catch(DataAccessException e) {
            System.out.println("Data access error");
        }
    }

}
