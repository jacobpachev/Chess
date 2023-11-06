package responses;

import models.AuthToken;

/**
 * Login Response class, just a data container
 */
public class LoginResponse {
    private String authToken;
    private String username;
    private String message;
    public LoginResponse(AuthToken authToken, String username) {
        this.authToken = authToken.getAuthToken();
        this.username = username;
    }

    public LoginResponse(String message) {
        this.message = message;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
