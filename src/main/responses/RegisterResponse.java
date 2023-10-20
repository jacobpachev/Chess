package responses;
/**
 * Register Response class, just a data container
 */
public class RegisterResponse {
    private String username;
    private String authToken;
    private String message;

    /**
     * Constructor with username, token, message
     * @param username username
     * @param authToken authentication token
     */
    public RegisterResponse(String username, String authToken) {
        this.username = username;
        this.authToken = authToken;
    }

    public RegisterResponse(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
