package serviceTests;

import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import requests.RegisterRequest;
import responses.RegisterResponse;
import services.AdminService;
import services.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class RegisterTest {
    @Test
    @DisplayName("Successful Register")
    public void successRegister() throws DataAccessException {
        UserService service = new UserService();
        RegisterRequest request = new RegisterRequest("jac", "jap123", "jap@byu.edu");
        UserDAO userDAO = new UserDAO();
        RegisterResponse response =  service.register(request);
        assertEquals("jac", response.getUsername(), "Unexpected username");
        assertEquals(36,  response.getAuthToken().length(),  "Auth token wrong len");
        assertNotEquals(null, userDAO.find("jac"),  "User not added to data");
    }
    @Test
    @DisplayName("Failed Register")
    public void failedRegister() {
        UserService service = new UserService();
        RegisterRequest request = new RegisterRequest("jap2", "jap123", "jap@byu.edu");
        service.register(request);
        RegisterResponse response =  service.register(request);
        assertEquals( "Error: already taken", response.getMessage(), "Duplicate requests");

        RegisterResponse responseBad =  service.register(new RegisterRequest("", "", ""));
        assertEquals( "Error: bad request", responseBad.getMessage(), "Empty request");
        var clearService = new AdminService();
        clearService.clear();
    }


}
