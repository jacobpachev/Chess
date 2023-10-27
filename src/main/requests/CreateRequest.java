package requests;
/**
 *Create Request class, just a data container
 */
public class CreateRequest{
    private String authToken;
    private final String gameName;

    /**
     * Constructor with token, game name
     * @param authToken authentication token
     * @param gameName game name
     */
    public CreateRequest(String authToken, String gameName) {
        this.authToken = authToken;
        this.gameName = gameName;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getGameName() {
        return gameName;
    }

}
