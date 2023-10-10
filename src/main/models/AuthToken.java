package models;

import java.util.Objects;
import java.util.Random;

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
     * format 10 random letters
     * @return token
     */
    private String generateToken() {
        Random rand = new Random();
        StringBuilder token = new StringBuilder();
        for(int i = 1; i<=5; i++) {
            token.append((char) (rand.nextInt(26) + 'A') );
            token.append((char) (rand.nextInt(26) + 'a') );
        }
        return token.toString();
    }
}
