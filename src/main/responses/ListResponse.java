package responses;

import models.Game;

import java.util.List;

/**
 * List Response class, just a data container
 */
public class ListResponse extends spark.Response{
    private List<Game> gameList;
    private String message;
    /**
     * Constructor with list of games
     * @param gameList list of games
     */
    ListResponse(List<Game> gameList) {
        this.gameList = gameList;
    }

    public List<Game> getGameList() {
        return gameList;
    }

    public void setGameList(List<Game> gameList) {
        this.gameList = gameList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
