package responses;

/**
 * Create Response class, just a data container
 */
public class CreateResponse extends spark.Response{
    private String gameName;
    private int gameID;
    private String message;

    /**
     * constructor with gameName and gameID
     * @param gameName name of the game
     * @param gameID game id number
     */
    public CreateResponse(String gameName, int gameID) {
        this.gameName = gameName;
        this.gameID = gameID;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
