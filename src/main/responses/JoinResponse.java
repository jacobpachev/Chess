package responses;

public class JoinResponse extends spark.Response{
    private String teamColor;
    private int gameId;
    private String message;

    /**
     * Constructor if no color specified
     * @param gameId game id num
     */
    public JoinResponse(int gameId) {
        this.gameId = gameId;
    }

    /**
     * Constructor if team color specified
     * @param teamColor team color
     * @param gameId game id num
     */
    public JoinResponse(String teamColor, int gameId) {
        this.teamColor = teamColor;
        this.gameId = gameId;
    }

    public String getTeamColor() {
        return teamColor;
    }

    public void setTeamColor(String teamColor) {
        this.teamColor = teamColor;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
