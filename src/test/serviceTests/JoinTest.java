package serviceTests;

import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import requests.CreateRequest;
import requests.JoinRequest;
import requests.ListRequest;
import requests.RegisterRequest;
import services.GameService;
import services.UserService;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class JoinTest {
    @Test
    @DisplayName("Successful Join")
    public void successJoin() {
        var registerRequest = new RegisterRequest("jap", "jap123", "jap@byu.edu");
        var userService = new UserService();
        var gameService = new GameService();
        var gameDAO = new GameDAO();
        var registerResponse = userService.register(registerRequest);
        var createResponse = gameService.create(new CreateRequest(registerResponse.getAuthToken(), "test"));
        var gameID = createResponse.getGameID();
        var blankResponse = gameService.join(new JoinRequest(registerResponse.getAuthToken(), "", gameID));
        var whiteResponse = gameService.join(new JoinRequest(registerResponse.getAuthToken(), "white", gameID));
        var blackResponse = gameService.join(new JoinRequest(registerResponse.getAuthToken(), "black", gameID));

        assertNull(blankResponse.getMessage());
        assertNull(whiteResponse.getMessage());
        assertNull(blackResponse.getMessage());

        try {
            assertEquals("jap", gameDAO.find(gameID).getObservers().get(0));
            assertEquals("jap", gameDAO.find(gameID).getWhiteUsername());
            assertEquals("jap", gameDAO.find(gameID).getBlackUsername());
        }
        catch(DataAccessException e) {
            System.out.println("Data access error");
        }
    }

    @Test
    @DisplayName("Failed Join")
    public void failJoin() {
        var registerRequest = new RegisterRequest("Jap", "jap123", "jap@byu.edu");
        var userService = new UserService();
        var gameService = new GameService();
        var gameDAO = new GameDAO();
        var registerResponse = userService.register(registerRequest);
        var createResponse = gameService.create(new CreateRequest(registerResponse.getAuthToken(), "test"));
        var gameID = createResponse.getGameID();
        var whiteResponse = gameService.join(new JoinRequest(registerResponse.getAuthToken(), "white", gameID));
        var whiteResponseDupl = gameService.join(new JoinRequest(registerResponse.getAuthToken(), "white", gameID));
        assertEquals("Error: already taken", whiteResponseDupl.getMessage());

        var whiteResponseNotAuth = gameService.join(new JoinRequest(UUID.randomUUID().toString(), "white", gameID));
        assertEquals("Error: unauthorized", whiteResponseDupl.getMessage());

        var whiteResponseBad = gameService.join(new JoinRequest(registerResponse.getAuthToken(), "purple", gameID));
        assertEquals("Error: bad request", whiteResponseDupl.getMessage());

    }
}
