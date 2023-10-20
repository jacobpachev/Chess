package services;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import models.AuthToken;
import models.User;
import requests.LogoutRequest;
import requests.RegisterRequest;
import responses.CreateResponse;
import responses.LogoutResponse;
import responses.RegisterResponse;
import requests.LoginRequest;
import responses.LoginResponse;

/**
 * User Service class, supports registering, log out and log in
 */
public class UserService {
    /**
     * Register
     * @param req request
     * @return response
     */
    public RegisterResponse register(RegisterRequest req){
        if(req.getUsername() == null || req.getPassword() == null || req.getEmail() == null) {
            return new RegisterResponse("Error: bad request");
        }
        if(req.getUsername().isEmpty() || req.getPassword().isEmpty() || req.getEmail().isEmpty()) {
            return new RegisterResponse("Error: bad request");
        }
        UserDAO userData = new UserDAO();
        AuthDAO authData = new AuthDAO();
        AuthToken token = new AuthToken(req.getUsername());
        try {
            userData.insert(new User(req.getUsername(), req.getPassword(), req.getEmail()));
            authData.insert(token);
        }
        catch (DataAccessException e) {
            return new RegisterResponse("Error: "+e.getMessage());
        }
        return new RegisterResponse(req.getUsername(), token.getAuthToken());
    }

    /**
     * Login
     * @param req request
     * @return response
     */
    public LoginResponse login(LoginRequest req) {
        UserDAO userDAO = new UserDAO();
        AuthDAO authDAO = new AuthDAO();
        User user;
        if(req.getUsername() == null || req.getPassword() == null) {
            return new LoginResponse("Error: bad request");
        }
        if(req.getUsername().isEmpty() || req.getPassword().isEmpty()) {
            return new LoginResponse("Error: bad request");
        }
        try {
            user = userDAO.find(req.getUsername());
        } catch (DataAccessException e) {
            return new LoginResponse("Error: " + e.getMessage());
        }
        if (user == null) {
            return new LoginResponse("Error: unauthorized");
        }
        if (!user.getPassword().equals(req.getPassword())) {
            return new LoginResponse("Error: unauthorized");
        }
        AuthToken userToken = new AuthToken(req.getUsername());
        try {
            authDAO.insert(userToken);
        } catch (DataAccessException e) {
            return new LoginResponse("Error: " + e.getMessage());
        }
        return new LoginResponse(userToken, req.getUsername());
    }

    /**
     * Logout
     * @param req request
     * @return response
     */
    public LogoutResponse logout(LogoutRequest req){
        AuthDAO authData = new AuthDAO();
        String userToken;
        String userName;
        if(req.getAuthToken() == null) {
            return new LogoutResponse("Error: bad request");
        }
        if(req.getAuthToken().isEmpty()) {
            return new LogoutResponse("Error: bad request");
        }
        try {
            userName = authData.findByToken(req.getAuthToken()).getUsername();
            userToken = authData.findByToken(req.getAuthToken()).getAuthToken();
            if(!userToken.equals(req.getAuthToken())) {
                return new LogoutResponse("Error: unauthorized");
            }
            authData.remove(userName);
        }
        catch(DataAccessException e) {
            if(e.getMessage().equals("Failed to find auth token")) {
                return new LogoutResponse("Error: unauthorized");
            }
            return new LogoutResponse("Error: " +e.getMessage());
        }
        return new LogoutResponse();
    }
}
