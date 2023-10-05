package dataAccess;

import models.User;
import java.util.List;
/**
 * Access user data
 */
public class UserDAO {
    /**
     * place a User in data
     * @param user User to place
     */
    public void insert(User user) throws DataAccessException {
    }

    /**
     * find a user's ID
     * @param username user to find ID
     * @param password user's password
     * @return int
     */
    public int find(String username, String password) throws DataAccessException {
        return 0;
    }

    /**
     * Find all Users in data
     * @return list of all Users
     */
    public List<User> findAll() throws DataAccessException {
        return null;
    }

    /**
     * clear all Users from data
     */
    public void clear() throws DataAccessException {
    }

    /**
     * Removes user of @param name from data
     * @param name username to remove
     */
    public void remove(String name) throws DataAccessException {}

    /**
     * update name with new token
     * @param name username to update
     * @param token new token
     */
    public void update(String name, String token) throws DataAccessException {}
}
