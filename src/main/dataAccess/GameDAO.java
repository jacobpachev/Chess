package dataAccess;

import chess.ChessGame;
import models.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * Access game data
 */
public class GameDAO {
    private static List<Game> gameData = new ArrayList<>();

    /**
     * place a Game in data
     * @param game Game to place
     */
    public void insert(Game game) throws DataAccessException {
        gameData.add(game);
    }

    /**
     * find a Game by gameID
     * @param gameID id to find game
     * @return null
     */
    public Game find(int gameID) throws DataAccessException {
        for(Game game : gameData) {
            if(game.getGameID() == gameID) {
                return game;
            }
        }
        throw new DataAccessException("Could not find game");
    }

    /**
     * Find all games in data
     * @return list of all games
     */
    public List<Game> findAll() throws DataAccessException {
        return gameData;
    }

    /**
     * clear all auth tokens from data
     */
    public void clear() throws DataAccessException {
        gameData = new ArrayList<>();
    }

    /**
     * Removes Game from data
     * @param gameID GameID to remove
     */
    public void remove(int gameID) throws DataAccessException {
        try {
            Game game = find(gameID);
            gameData.remove(game);
        }
        catch(DataAccessException e) {
            throw new DataAccessException("Could not remove game(not found)");
        }
    }

    /**
     * Places a user as whitePlayer or blackPlayer
     * @param username user to be placed in game spot
     */
    public void claimPlayerSpot(String username, int gameID, String color) throws DataAccessException {
        color = color.toLowerCase();
        if(color.equals("white")) find(gameID).setWhiteUsername(username);
        if(color.equals("black")) find(gameID).setBlackUsername(username);
        if(color.isEmpty()) find(gameID).addObserver(username);
    }

    /**
     * update gameID with new gameName
     * @param gameId username to update
     * @param gameName new name
     */
    public void updateName(int gameId, String gameName) throws DataAccessException {
        find(gameId).setGameName(gameName);
    }
    /**
     * update gameID with new game
     * @param gameId username to update
     * @param game new game
     */
    public void updateGame(int gameId, ChessGame game) throws DataAccessException {
        find(gameId).setGame(game);
    }
}
