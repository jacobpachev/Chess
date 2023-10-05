package dataAccess;

import models.AuthToken;
import java.util.List;

/**
 * Access authentication data
 */
public class AuthDAO {

    /**
     * place an AuthToken in data
     * @param authToken AuthToken to place
     */
    public void insert(AuthToken authToken) throws DataAccessException {
    }

    /**
     * find a user's auth token
     * @param username user to find auth token
     * @return int
     */
    public int find(String username) throws DataAccessException {
        return 0;
    }

    /**
     * Find all auth tokens in data
     * @return list of all auth tokens
     */
    public List<AuthToken> findAll() throws DataAccessException {
        return null;
    }

    /**
     * clear all auth tokens from data
     */
    public void clear() throws DataAccessException {
    }

    /**
     * Removes authToken from data
     * @param authToken token to remove
     */
    public void remove(String authToken) throws DataAccessException {}

    /**
     * update name with new token
     * @param name username to update
     * @param token new token
     */
    public void update(String name, String token) throws DataAccessException {}
}
