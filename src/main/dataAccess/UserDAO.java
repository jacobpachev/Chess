package dataAccess;

import models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Access user data
 */
public class UserDAO {
    private static List<User> userData = new ArrayList<>();
    /**
     * place a User in data
     * @param user User to place
     */
    public void insert(User user) throws DataAccessException {
        userData.add(user);
        System.out.println(userData);
    }

    /**
     * find a user's ID
     * @param username user to find ID
     * @return int
     */
    public User find(String username) throws DataAccessException {
        for(User user : userData) {
            if(user.getUserName().equals(username)) {
                return user;
            }
        }
        throw new DataAccessException("Could not find user");
    }

    /**
     * Find all Users in data
     * @return list of all Users
     */
    public List<User> findAll() throws DataAccessException {
        return userData;
    }

    /**
     * clear all Users from data
     */
    public void clear() throws DataAccessException {
        userData = new ArrayList<>();
    }

    /**
     * Removes user of @param name from data
     * @param name username to remove
     */
    public void remove(String name) throws DataAccessException {
        try {
            User user = find(name);
            userData.remove(user);
        }
        catch(DataAccessException e) {
            throw new DataAccessException("Could not remove user(not found)");
        }

    }

}
