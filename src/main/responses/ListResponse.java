package responses;

import models.Game;

import java.util.List;

/**
 * List Response class, just a data container
 */
public class ListResponse {
    private List<Game> games;
    private String message;
    /**
     * Constructor with list of games
     * @param games list of games
     */
    public ListResponse(List<Game> games) {
        this.games = games;
    }
    public ListResponse(String message) { this.message = message; }

    public List<Game> getGames() {
        return games;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
