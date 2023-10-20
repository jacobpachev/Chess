package requests;
/**
 *Logout Request class, just a data container
 */
public class LogoutRequest {
    private String authToken;
    /**
     * Constructor with token
     * @param authToken authentication token
     */
    public LogoutRequest(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
