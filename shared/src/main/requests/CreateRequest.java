package requests;
/**
 *Create Request class, just a data container
 */
public class CreateRequest{
    private String authtoken;
    private final String gameName;

    /**
     * Constructor with token, game name
     * @param gameName game name
     * @param authtoken authentication token
     */
    public CreateRequest(String gameName, String authtoken) {
        this.authtoken = authtoken;
        this.gameName = gameName;
    }

    public String getAuthToken() {
        return authtoken;
    }

    public void setAuthToken(String authtoken) {
        this.authtoken = authtoken;
    }

    public String getGameName() {
        return gameName;
    }

}
