package requests;
/**
 * Join Request class, just a data container
 */
public class JoinRequest extends spark.Request{
    private String authToken;
    /**
     * Constructor with auth token
     * @param authToken authorization token
     */
    public JoinRequest(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
