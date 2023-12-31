package requests;
/**
 * Join Request class, just a data container
 */
public class JoinRequest{
    private String authToken;
    private final String playerColor;
    private final int gameID;
    /**
     * Constructor with auth token, color, game id
     * @param authToken authorization token
     * @param playerColor color, in case of blank joins as observer
     * @param gameID game id
     */
    public JoinRequest(String authToken, String playerColor, int gameID) {
        this.authToken = authToken;
        if(playerColor.isEmpty()) {
            this.playerColor = null;
        }
        else {
            this.playerColor = playerColor;
        }
        this.gameID = gameID;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public int getGameID() {
        return gameID;
    }

    public String getPlayerColor() {
        return playerColor;
    }


}
