package dataAccess;

import models.AuthToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Access authentication data
 */
public class AuthDAO {

    private static List<AuthToken> tokenData = new ArrayList<>();

    /**
     * place an AuthToken in data
     * @param authToken AuthToken to place
     */
    public void insert(AuthToken authToken) throws DataAccessException {
        tokenData.add(authToken);
    }

    /**
     * find a user's auth token
     * @param username user to find auth token
     * @return int
     */
    public AuthToken findByName(String username) throws DataAccessException {
        for(AuthToken token : tokenData) {
            if(token.getUsername().equals(username)) return token;
        }
        throw new DataAccessException("Failed to find auth token");
    }

    public AuthToken findByToken(String authToken) throws DataAccessException {
        for(AuthToken token : tokenData) {
            if(token.getAuthToken().equals(authToken)) return token;
        }
        throw new DataAccessException("Failed to find auth token");
    }


    /**
     * Find all auth tokens in data
     * @return list of all auth tokens
     */
    public List<AuthToken> findAll() throws DataAccessException {
        return tokenData;
    }

    /**
     * clear all auth tokens from data
     */
    public void clear() throws DataAccessException {
        tokenData = new ArrayList<>();
    }

    /**
     * Removes authToken from data
     * @param name username of token to remove
     */
    public void remove(String name) throws DataAccessException {
        try {
            tokenData.remove(findByName(name));
        }
        catch (DataAccessException e) {
            throw new DataAccessException("Could not remove token(not found)");
        }
    }
}
