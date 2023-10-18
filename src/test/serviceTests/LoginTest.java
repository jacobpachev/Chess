package serviceTests;

import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import dataAccess.AuthDAO;
import requests.LoginRequest;
import requests.RegisterRequest;
import responses.LoginResponse;
import services.AdminService;
import services.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LoginTest {
    @Test
    @DisplayName("Successful Login")
    public void successLogin() {
        RegisterRequest registerRequest = new RegisterRequest("jap", "jap123", "jap@byu.edu");
        UserService userService = new UserService();
        userService.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest("jap", "jap123");
        UserDAO userDAO = new UserDAO();
        AuthDAO authDAO = new AuthDAO();
        LoginResponse response = userService.login(loginRequest);
        assertEquals("jap", response.getUsername(), "Wrong username");
        assertEquals(36, response.getAuthToken().length(), "Wrong auth token len");
        try {
            assertNotNull(userDAO.find("jap"));
            assertNotNull(authDAO.findByName("jap"));
        }
        catch (DataAccessException e) {
            System.out.println("Data Access error");
        }

    }

    @Test
    @DisplayName("Failed Login")
    public void failedLogin() {
        RegisterRequest registerRequest = new RegisterRequest("jap", "jap123", "jap@byu.edu");
        UserService userService = new UserService();
        userService.register(registerRequest);

        LoginRequest wrongNameReq = new LoginRequest("jac", "jap123");
        LoginRequest wrongPwdReq = new LoginRequest("jap", "jap00");
        LoginResponse wrongNameResponse  = userService.login(wrongNameReq);
        assertEquals("Error: unauthorized", wrongNameResponse.getMessage(), "Name not found");

        LoginResponse wrongPwdResponse = userService.login(wrongPwdReq);
        assertEquals("Error: unauthorized", wrongPwdResponse.getMessage(), "Wrong password");
        var clearService = new AdminService();
        clearService.clear();
    }
}
