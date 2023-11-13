import chess.GameImpl;
import models.Game;
import models.User;
import requests.CreateRequest;
import responses.CreateResponse;
import ui.Repl;
import serverFacade.ServerFacade;
public class ClientMain {

    public static void main(String[] args) throws Exception {
        var serverFacade = new ServerFacade("http://localhost:8080");
        var user = serverFacade.addUser(new User("test", "jacob", "jap@byu.edu"));
        var token = user.getAuthToken();
        serverFacade.addGame(new CreateRequest("test", token));
    }
}
