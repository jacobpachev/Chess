package serviceTests;

import dataAccess.DataAccessException;
import dataAccess.AuthDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import requests.LogoutRequest;
import requests.RegisterRequest;
import responses.LogoutResponse;
import services.AdminService;
import services.UserService;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class LogoutTest {
    @BeforeEach
    @AfterEach
    void clear() {
        var adminService = new AdminService();
        adminService.clear();
    }

    @Test
    public void
    successLogout() {
        var userReq = new RegisterRequest("Jap", "jap123", "jap@byu.edu");
        var userService = new UserService();
        if(userService.register(userReq).getMessage() != null) {
            System.out.println("Database error");
            return;
        }
        String token;
        try {
            var authDAO = new AuthDAO();
            token = authDAO.findByName(userReq.getUsername()).getAuthToken();
            LogoutResponse logoutResponse = userService.logout(new LogoutRequest(token));
            assertNull(logoutResponse.getMessage(), "Error message received");

            assertNull(authDAO.findByName("Jap"));
        }
        catch (DataAccessException e) {
            System.out.println("Database error");
        }
    }

    @Test
    public void failLogout() {
        var userService = new UserService();
        String token = UUID.randomUUID().toString();
        var logoutResponse = userService.logout(new LogoutRequest(token));
        assertEquals("Error: unauthorized", logoutResponse.getMessage());
        var clearService = new AdminService();
        clearService.clear();
    }
}
