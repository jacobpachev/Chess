package models;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * model of Chess Game
 */
public class Game {
    private Integer gameID;
    private String whiteUsername;
    private String blackUsername;
    private String gameName;
    private final List<String> observers;
    private ChessGame game;

    public Game(String whiteUsername, String blackUsername, String gameName, ChessGame game) {
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
        this.game = game;
        this.observers = new ArrayList<>();
    }
    public Game(String gameName, ChessGame game) {
        this.gameName = gameName;
        this.game = game;
        this.observers = new ArrayList<>();
    }

    public Integer getGameID() {
        return gameID;
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


    public ChessGame getGame() {
        return game;
    }

    public void setGame(ChessGame game) {
        this.game = game;
    }

    public List<String> getObservers() {
        return observers;
    }

    /**
     * Adds an observers
     * @param obsName name of observer to be added
     */
    public void addObserver(String obsName) {
        observers.add(obsName);
    }

    public void setGameID(Integer gameID) {
        this.gameID = gameID;
    }
}
