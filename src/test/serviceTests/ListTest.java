package serviceTests;

import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import models.Game;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import requests.CreateRequest;
import requests.ListRequest;
import requests.RegisterRequest;
import services.GameService;
import services.UserService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListTest {
    @Test
    @DisplayName("Successful List")
    public void successList() {
        var registerRequest = new RegisterRequest("jap", "jap123", "jap@byu.edu");
        var userService = new UserService();
        var gameService = new GameService();
        var registerResponse = userService.register(registerRequest);
        var createResponse1 = new CreateRequest(registerResponse.getAuthToken(), "test1");
        var createResponse2 = new CreateRequest(registerResponse.getAuthToken(), "test2");
        gameService.create(createResponse1);
        gameService.create(createResponse2);

        var listResponse = gameService.list(new ListRequest(registerResponse.getAuthToken()));
        assertEquals(listResponse.getGames().get(0).getGameName(), "test1");
        assertEquals(listResponse.getGames().get(1).getGameName(), "test2");
    }

    @Test
    @DisplayName("Failed List")
    public void failList() {
        var registerRequest = new RegisterRequest("Jap", "jap123", "jap@byu.edu");
        var userService = new UserService();
        var gameService = new GameService();
        var registerResponse = userService.register(registerRequest);
        if(registerResponse.getMessage() != null) {
            System.out.println("Data access error");
            return;
        }
        var createRequest = new CreateRequest(registerResponse.getAuthToken(), "fail");
        var createResponse = gameService.create(createRequest);

        String token = UUID.randomUUID().toString();
        var listResponse = gameService.list(new ListRequest(token));
        assertEquals("Error: unauthorized", listResponse.getMessage());
    }
}
