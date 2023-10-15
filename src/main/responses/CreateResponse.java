package responses;

/**
 * Create Response class, just a data container
 */
public class CreateResponse extends spark.Response{
    private int gameID;
    private String message;

    /**
     * constructor with gameID
     * @param gameID game id number
     */
    public CreateResponse(int gameID) {
        this.gameID = gameID;
    }

    public CreateResponse(String message) {
        this.message = message;
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
