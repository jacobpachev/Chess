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
        Game game = null;
        try {
            AuthDAO authData = new AuthDAO();
            GameDAO gameData = new GameDAO();
            if(authData.findByToken(req.getAuthToken()) != null) {
                game = new Game(req.getGameName(), new GameImpl());
                gameData.insert(game);
                game.setGameID(gameData.getID(game.getGameName()));
            }
            if(authData.findByToken(req.getAuthToken()).getAuthToken() == null) {
                throw new DataAccessException("Failed to find auth token");
            }
        }
        catch (DataAccessException e) {
            if(e.getMessage().equals("Failed to find auth token")) {
                return new CreateResponse("Error: unauthorized");
            }
            return new CreateResponse("Error: " +e.getMessage());
        }
        assert game != null;
        System.out.println(game);
        return new CreateResponse(game.getGameID());
    }

    /**
     * Join game
     * @param req request
     * @return response
     */
    public JoinResponse join(JoinRequest req){
        var token = req.getAuthToken();
        var color = req.getPlayerColor();
        var gameID = req.getGameID();
        System.out.println(gameID);
        if(gameID < 1000) {
            return new JoinResponse("Error: bad request");
        }
        if(color != null) {
            color = color.toLowerCase();
            if ((!color.equals("white") && !color.equals("black") && !color.isEmpty())) {
                return new JoinResponse("Error: bad request");
            }
        }
        try {
            var gameDAO = new GameDAO();
            var authDAO = new AuthDAO();
            Game game = gameDAO.find(req.getGameID());
            String username = authDAO.findByToken(token).getUsername();
            if(color == null || color.isEmpty()) {
                gameDAO.find(req.getGameID()).setBlackUsername(null);
                gameDAO.find(req.getGameID()).setWhiteUsername(null);
                gameDAO.claimPlayerSpot(username, req.getGameID(), "");
                return new JoinResponse();
            }
            if(color.equals("white")) {
                if(game.getWhiteUsername() != null) {
                    if (!game.getWhiteUsername().isEmpty()) {
                        throw new DataAccessException("already taken");
                    }
                }
            }
            if(color.equals("black")) {
                if(game.getBlackUsername() != null) {
                    if (!game.getBlackUsername().isEmpty()) {
                        throw new DataAccessException("already taken");
                    }
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
        try {
            AuthDAO authDAO = new AuthDAO();
            authDAO.findByToken(req.getAuthToken());
        }
        catch(DataAccessException e) {
            if(e.getMessage().equals("Failed to find auth token")) {
                return new ListResponse("Error: unauthorized");
            }
            return new ListResponse("Error: " +e.getMessage());
        }
        try {
            GameDAO gameDAO = new GameDAO();
            return new ListResponse(gameDAO.findAll());
        }
        catch(DataAccessException e) {
            return new ListResponse("Error: " +e.getMessage());
        }
    }

}
