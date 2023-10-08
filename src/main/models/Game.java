package models;

import chess.ChessGame;

/**
 * model of Chess Game
 */
public class Game {
    private int gameID;
    private String whiteUsername;
    private String blackUsername;
    private String gameName;
    private ChessGame game;

    public Game(String whiteUsername, String blackUsername, String gameName, ChessGame game) {
        this.gameID = generateID();
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
        this.game = game;
    }
    public Game(String gameName, ChessGame game) {
        this.gameID = generateID();
        this.gameName = gameName;
        this.game = game;
    }

    /**
     * generates an game ID
     * @return game ID
     */
    private int generateID() {
        return 0;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public void setWhiteUsername(String whiteUsername) {
        this.whiteUsername = whiteUsername;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    public void setBlackUsername(String blackUsername) {
        this.blackUsername = blackUsername;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public ChessGame getGame() {
        return game;
    }

    public void setGame(ChessGame game) {
        this.game = game;
    }
}