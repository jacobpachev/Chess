package serviceTests;

import dataAccess.DataAccessException;
import dataAccess.AuthDAO;
import requests.LogoutRequest;
import requests.RegisterRequest;
import responses.LoginResponse;
import responses.LogoutResponse;
import services.UserService;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class LogoutTest {

    @Test
    public void successLogout() {
        AuthDAO authData = new AuthDAO();
        RegisterRequest userReq = new RegisterRequest("Jap", "jap123", "jap@byu.edu");
        UserService userService = new UserService();
        if(userService.register(userReq).getMessage() != null) {
            System.out.println("Database error");
            return;
        }
        String token;
        try {
            token = authData.findByName(userReq.getUsername()).getAuthToken();
        }
        catch (DataAccessException e) {
            System.out.println("Database error");
            return;
        }

        LogoutResponse logoutResponse = userService.logout(new LogoutRequest(token));
        assertNull(logoutResponse.getMessage(), "Error message received");
    }

    @Test
    public void failLogout() {
        UserService userService = new UserService();
        String token = UUID.randomUUID().toString();
        LogoutResponse logoutResponse = userService.logout(new LogoutRequest(token));
        assertEquals("Error: unauthorized", logoutResponse.getMessage());
    }
}
