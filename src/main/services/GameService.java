package services;

import chess.GameImpl;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import models.Game;
import requests.CreateRequest;
import requests.JoinRequest;
import requests.ListRequest;
import responses.CreateResponse;
import responses.JoinResponse;
import responses.ListResponse;

/**
 * Game Service class, can make a various requests for game, than returns response
 */
public class GameService {
    /**
     * Create game
     * @param req request
     * @return response
     */
    public CreateResponse create(CreateRequest req){
        AuthDAO authData = new AuthDAO();
        GameDAO gameData = new GameDAO();
        Game game = null;
        try {
            if(authData.findByToken(req.getAuthToken()) != null) {
                game = new Game(req.getGameName(), new GameImpl());
                gameData.insert(game);
            }
        }
        catch (DataAccessException e) {
            if(e.getMessage().equals("Failed to find auth token")) {
                return new CreateResponse("Error: unauthorized");
            }
            return new CreateResponse("Error: " +e.getMessage());
        }
        assert game != null;
        return new CreateResponse(game.getGameID());
    }

    /**
     * Join game
     * @param req request
     * @return response
     */
    public JoinResponse join(JoinRequest req){
        return null;
    }

    /**
     * List games
     * @param req request
     * @return response
     */
    public ListResponse list(ListRequest req){
        AuthDAO authDAO = new AuthDAO();
        GameDAO gameDAO = new GameDAO();
        try {
            authDAO.findByToken(req.getAuthToken());
        }
        catch(DataAccessException e) {
            if(e.getMessage().equals("Failed to find auth token")) {
                return new ListResponse("Error: unauthorized");
            }
            return new ListResponse("Error: " +e.getMessage());
        }
        try {
            return new ListResponse(gameDAO.findAll());
        }
        catch(DataAccessException e) {
            return new ListResponse("Error: " +e.getMessage());
        }
    }

}
