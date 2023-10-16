package services;

import chess.GameImpl;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import models.AuthToken;
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
        var gameDAO = new GameDAO();
        var authDAO = new AuthDAO();
        var token = req.getAuthToken();
        var color = req.getPlayerColor();
        if(!color.equals("white") && !color.equals("black") && !color.isEmpty()){
            return new JoinResponse("Error: bad request");
        }
        try {
            Game game = gameDAO.find(req.getGameID());
            String username = authDAO.findByToken(token).getUsername();
            if(color.equals("white")) {
                if(!game.getWhiteUsername().isEmpty()) {
                    throw new DataAccessException("already taken");
                }
            }
            if(color.equals("black")) {
                if(!game.getBlackUsername().isEmpty()) {
                    throw new DataAccessException("already taken");
                }
            }
            gameDAO.claimPlayerSpot(username, req.getGameID(), color);
        }
        catch (DataAccessException e) {
            if(e.getMessage().equals("Failed to find auth token")) {
                return new JoinResponse("Error: unauthorized");
            }
            if(e.getMessage().equals("Could not find game")) {
                return new JoinResponse("Error: bad request");
            }
            return new JoinResponse("Error: " +e.getMessage());
        }

        return new JoinResponse();
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
