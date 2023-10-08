package requests;
/**
 *Create Request class, just a data container
 */
public class CreateRequest extends spark.Request{
    private String authToken;
    /**
     * Constructor with token
     * @param authToken authentication token
     */
    public CreateRequest(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
