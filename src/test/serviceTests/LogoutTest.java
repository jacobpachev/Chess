package serviceTests;

import dataAccess.DataAccessException;
import dataAccess.AuthDAO;
import dataAccess.UserDAO;
import requests.LogoutRequest;
import requests.RegisterRequest;
import responses.LoginResponse;
import responses.LogoutResponse;
import services.UserService;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LogoutTest {

    @Test
    public void successLogout() {
        var authDAO = new AuthDAO();
        var userDAO = new UserDAO();
        var userReq = new RegisterRequest("Jap", "jap123", "jap@byu.edu");
        var userService = new UserService();
        if(userService.register(userReq).getMessage() != null) {
            System.out.println("Database error");
            return;
        }
        String token;
        try {
            token = authDAO.findByName(userReq.getUsername()).getAuthToken();
        }
        catch (DataAccessException e) {
            System.out.println("Database error");
            return;
        }

        LogoutResponse logoutResponse = userService.logout(new LogoutRequest(token));
        assertNull(logoutResponse.getMessage(), "Error message received");

        try {
            assertNull(authDAO.findByName("jap"));
        }
        catch (DataAccessException e) {
            System.out.println("Data Access error");
        }
    }

    @Test
    public void failLogout() {
        var userService = new UserService();
        String token = UUID.randomUUID().toString();
        var logoutResponse = userService.logout(new LogoutRequest(token));
        assertEquals("Error: unauthorized", logoutResponse.getMessage());
    }
}
