package serverFacadeTests;

import dataAccess.GameDAO;
import dataAccess.AuthDAO;
import models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.CreateRequest;
import requests.JoinRequest;
import requests.ListRequest;
import serverFacade.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    ServerFacade serverFacade;
    GameDAO gameDAO;
    AuthDAO authDAO;
    public GameTest() {
        serverFacade = new ServerFacade("http://localhost:8080");
        try {
            gameDAO = new GameDAO();
            authDAO = new AuthDAO();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
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
    public void successCreate() {
        try {
            String authToken = serverFacade.addUser(new User("jap", "jap123", "jap@byu.edu")).getAuthToken();
            serverFacade.addGame(new CreateRequest("test", authToken));
            assertNotNull(gameDAO.find("test"));
        }
        catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void failedCreate() {
        try {
            serverFacade.addGame(new CreateRequest("fail", "random token"));
        }
        catch (Exception e) {
            assertEquals("Error 401 Unauthorized", e.getMessage());
        }
    }

    @Test
    public void successJoin() {
        try {
            var authToken = serverFacade.addUser(new User("jap", "jap123", "jap@byu.edu")).getAuthToken();
            var game = serverFacade.addGame(new CreateRequest("test", authToken));
            serverFacade.joinGame(new JoinRequest(authToken, "WHITE", game.getGameID()));
            serverFacade.joinGame(new JoinRequest(authToken, "BLACK", game.getGameID()));
            serverFacade.joinGame(new JoinRequest(authToken, "", game.getGameID()));
            assertEquals("jap", gameDAO.find("test").getWhiteUsername());
            assertEquals("jap", gameDAO.find("test").getBlackUsername());
            assertEquals(1, gameDAO.find("test").getObservers().size());
        }
        catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void failJoin() {
        try {
            var authToken = serverFacade.addUser(new User("jap", "jap123", "jap@byu.edu")).getAuthToken();
            serverFacade.joinGame(new JoinRequest(authToken, "WHITE", 0));
        } catch (Exception e) {
            assertEquals("Error 400 Bad Request", e.getMessage());
        }
    }

    @Test
    public void successList() {
        try {
            var authToken = serverFacade.addUser(new User("jap", "jap123", "jap@byu.edu")).getAuthToken();
            serverFacade.addGame(new CreateRequest("test1", authToken));
            serverFacade.addGame(new CreateRequest("test2", authToken));
            var listResponse = serverFacade.listGames(new ListRequest(authToken));
            assertEquals(2, listResponse.getGames().size());
        }
        catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public  void failList() {
        try {
            serverFacade.listGames(new ListRequest("random token"));
        }
        catch (Exception e) {
            assertEquals("Error 401 Unauthorized", e.getMessage());
        }
    }
}
