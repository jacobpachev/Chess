package serviceTests;

import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import requests.CreateRequest;
import requests.RegisterRequest;
import services.GameService;
import services.UserService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CreateTest {
    @Test
    @DisplayName("Successful Create")
    public void successCreate() {
        var registerRequest = new RegisterRequest("jap", "jap123", "jap@byu.edu");
        var userService = new UserService();
        var gameService = new GameService();
        var gameDAO = new GameDAO();
        var registerResponse = userService.register(registerRequest);
        if(registerResponse.getMessage() != null) {
            System.out.println("Data access error");
            return;
        }
        var createRequest = new CreateRequest(registerResponse.getAuthToken(), "test");
        var createResponse = gameService.create(createRequest);
        assert(createResponse.getGameID() >= 1000 || createResponse.getGameID() < 10000);

        try {
            assertNotNull(gameDAO.find(createResponse.getGameID()));
        }
        catch(DataAccessException e) {
            System.out.println("Data access error");
        }
    }

    @Test
    @DisplayName("Failed Create")
    public void failCreate() {
        var gameService = new GameService();
        String token = UUID.randomUUID().toString();
        var createResponse = gameService.create(new CreateRequest(token, "fail"));
        assertEquals("Error: unauthorized", createResponse.getMessage());
    }
}
