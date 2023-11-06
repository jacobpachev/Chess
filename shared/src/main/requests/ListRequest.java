package requests;
/**
 *List Request class, just a data container
 */
public class ListRequest{
    private String authToken;
    /**
     * Constructor with token
     * @param authToken authentication token
     */
    public ListRequest(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
