package serverFacadeTests;

import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import dataAccess.AuthDAO;
import models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.LoginRequest;
import serverFacade.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    ServerFacade serverFacade;
    UserDAO userDAO;
    AuthDAO authDAO;
    public UserTest() {
        serverFacade = new ServerFacade("http://localhost:8080");
        try {
            userDAO = new UserDAO();
            authDAO = new AuthDAO();
        }
        catch (DataAccessException e) {
            System.out.println("Internal database error");
        }
    }
    @BeforeEach
    @AfterEach
    void cleanUp() {
        try {
            serverFacade.clear();
        }
        catch (Exception e) {
            fail(e.getMessage());
        }
    }
    @Test
    public void successRegister() {
        try {
            serverFacade.addUser(new User("jap", "jap123", "jap@byu.edu"));
            assertNotNull(userDAO.find("jap"));
            assertNotNull(authDAO.findByName("jap"));
        }
        catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void failRegister() {
        try {
            serverFacade.addUser(new User("jap", "jap123", "jap@byu.edu"));
            serverFacade.addUser(new User("jap", "jap123", "jap@byu.edu"));
        }
        catch (Exception e) {
            assertEquals("Error 403 Forbidden", e.getMessage());
        }
    }

    @Test
    public void successLogin() {
        try {
            var response = serverFacade.addUser(new User("jap", "jap123", "jap@byu.edu"));
            serverFacade.logoutUser(response.getAuthToken());
            serverFacade.loginUser(new LoginRequest("jap", "jap123"));
            assertNotNull(authDAO.findByName("jap"));
            assertNotNull(userDAO.find("jap"));
        }
        catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void failedLogin() {
        try {
            serverFacade.loginUser(new LoginRequest("jap", "jap123"));
        }
        catch (Exception e) {
            assertEquals("Error 401 Unauthorized", e.getMessage());
        }
    }

    @Test
    public void successLogout() {
        try {
            var response = serverFacade.addUser(new User("jap", "jap123", "jap@byu.edu"));
            serverFacade.logoutUser(response.getAuthToken());
            assertNull(authDAO.findByName("jap"));
        }
        catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void failedLogout() {
        try {
            serverFacade.logoutUser("random token");
        }
        catch (Exception e) {
            assertEquals("Error 401 Unauthorized", e.getMessage());
        }
    }
}
