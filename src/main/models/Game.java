package models;

import chess.ChessGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * model of Chess Game
 */
public class Game {
    private int gameID;
    private String whiteUsername;
    private String blackUsername;
    private String gameName;
    private List<String> observers;
    private ChessGame game;

    public Game(String whiteUsername, String blackUsername, String gameName, ChessGame game) {
        this.gameID = generateID();
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
        this.game = game;
        this.observers = new ArrayList<>();
    }
    public Game(String gameName, ChessGame game) {
        this.gameID = generateID();
        this.gameName = gameName;
        this.whiteUsername = "";
        this.blackUsername = "";
        this.game = game;
        this.observers = new ArrayList<>();
    }

    /**
     * generates a game ID
     * format 5 random digits
     * @return game ID
     */
    private int generateID() {
        return new Random().nextInt(1000, 9999);
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

    public List<String> getObservers() {
        return observers;
    }

    public void setObservers(List<String> observers) {
        this.observers = observers;
    }

    /**
     * Adds an observers
     * @param obsName name of observer to be added
     */
    public void addObserver(String obsName) {
        observers.add(obsName);
    }
}
