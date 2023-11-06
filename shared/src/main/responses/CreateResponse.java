package responses;

/**
 * Create Response class, just a data container
 */
public class CreateResponse {
    public Integer gameID;
    private String message;

    /**
     * constructor with gameID
     * @param gameID game id number
     */
    public CreateResponse(Integer gameID) {
        this.gameID = gameID;
    }

    public CreateResponse(String message) {
        this.message = message;
    }


    public Integer getGameID() {
        return gameID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
