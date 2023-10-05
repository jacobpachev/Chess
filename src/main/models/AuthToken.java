package models;

import java.util.Objects;

/**
 * Model of Authentication Token
 */
public class AuthToken {
    private String authToken;
    private String username;

    public AuthToken(String username) {
        this.authToken = generateToken();
        this.username = username;
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

    /**
     * generates an authentication token
     * @return token
     */
    private String generateToken() {return null;}
}
