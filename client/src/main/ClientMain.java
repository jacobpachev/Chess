import chess.GameImpl;
import models.Game;
import models.User;
import requests.CreateRequest;
import responses.CreateResponse;
import ui.Repl;
import serverFacade.ServerFacade;
public class ClientMain {

    public static void main(String[] args) throws Exception {
        var repl = new Repl();
        repl.run();
    }
}
