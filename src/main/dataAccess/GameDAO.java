package dataAccess;

import models.Game;

import java.util.List;
/**
 * Access game data
 */
public class GameDAO {

    /**
     * place a Game in data
     * @param game Game to place
     */
    public void insert(Game game) throws DataAccessException {
    }

    /**
     * find a Game by gameID
     * @param gameID id to find game
     * @return null
     */
    public Game find(int gameID) throws DataAccessException {
        return null;
    }

    /**
     * Find all games in data
     * @return list of all games
     */
    public List<Game> findAll() throws DataAccessException {
        return null;
    }

    /**
     * clear all auth tokens from data
     */
    public void clear() throws DataAccessException {
    }

    /**
     * Removes Game from data
     * @param game Game to remove
     */
    public void remove(String game) throws DataAccessException {}

    /**
     * Places a user as whitePlayer or blackPlayer
     * @param username user to be placed in game spot
     */
    public void claimPlayerSpot(String username) {}

    /**
     * update gameID with new gameName
     * @param gameId username to update
     * @param gameName new name
     */
    public void update(int gameId, String gameName) throws DataAccessException {}
}
