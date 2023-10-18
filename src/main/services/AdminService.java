package services;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import responses.ClearResponse;

/**
 * Clear Service class, makes a clear request, than returns response
 */
public class AdminService {
    /**
     * Clear
     * @return response
     */
    public ClearResponse clear(){
        var userDAO = new UserDAO();
        var authDAO = new AuthDAO();
        var gameDAO = new GameDAO();
        try {
            userDAO.clear();
            authDAO.clear();
            gameDAO.clear();
        }
        catch(DataAccessException e) {
            return new ClearResponse("Error: "+e.getMessage());
        }
        return new ClearResponse();
    }
}
