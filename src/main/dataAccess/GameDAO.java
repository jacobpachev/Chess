package dataAccess;

import chess.GameAdapter;
import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.Game;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

/**
 * Access game data
 */
public class GameDAO {
    private final Database dataBase;
    public GameDAO() throws DataAccessException{
        this.dataBase = new Database();
        try(var conn = dataBase.getConnection()) {
            conn.setCatalog("chess");

            var createPetTable = """
            CREATE TABLE  IF NOT EXISTS game (
                gameID INT NOT NULL AUTO_INCREMENT,
                whiteUsername VARCHAR(255),
                blackUsername VARCHAR(255),
                gameName VARCHAR(255) NOT NULL,
                observers VARCHAR(255) NOT NULL,
                game VARCHAR(10000) NOT NULL,
                PRIMARY KEY (gameID)
            )""";

            try (var createTableStatement = conn.prepareStatement(createPetTable)) {
                createTableStatement.executeUpdate();
            }
            try (var alterTable = conn.prepareStatement("ALTER table game AUTO_INCREMENT = 1000")) {
                alterTable.executeUpdate();
            }
        }
        catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * place a Game in data
     * @param game Game to place
     */
    public void insert(Game game) throws DataAccessException {
        var sqlStr = """
                INSERT INTO game (whiteUsername, blackUsername, gameName, observers, game)
                 VALUES (?, ?, ?, ?, ?)
                """;
        var jsonBuilder = new GsonBuilder();
        jsonBuilder.registerTypeAdapter(ChessGame.class, new GameAdapter());
        var json = jsonBuilder.create();
        try(var conn = dataBase.getConnection()) {
            try(var preparedStatement = conn.prepareStatement(sqlStr)) {
                preparedStatement.setString(1, game.getWhiteUsername());
                preparedStatement.setString(2, game.getBlackUsername());
                preparedStatement.setString(3, game.getGameName().replaceAll("\"", ""));
                preparedStatement.setString(4, json.toJson(game.getObservers()));
                preparedStatement.setString(5, json.toJson(game.getGame()));
                preparedStatement.executeUpdate();
            }
        }
        catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * find a Game by gameID
     * @param gameID id to find game
     * @return null
     */
    public Game find(int gameID) throws DataAccessException {
        String whiteUsername = null;
        String blackUsername = null;
        String gameName = null;
        List<String> observers = null;
        var gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ChessGame.class, new GameAdapter());
        var gson = gsonBuilder.create();
        ChessGame chessGame = null;
        try(var conn = dataBase.getConnection()) {
            try(var preparedStatement = conn.prepareStatement("SELECT * FROM game WHERE gameID = ?")) {
                preparedStatement.setInt(1, gameID);
                var query = preparedStatement.executeQuery();
                while(query.next()) {
                    whiteUsername = query.getString("whiteUsername");
                    blackUsername = query.getString("blackUsername");
                    gameName = query.getString("gameName");
                    observers = gson.fromJson(query.getString("observers"), List.class);
                    chessGame = gson.fromJson(query.getString("game"), ChessGame.class);
                }
            }
        }
        catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        var game = new Game(whiteUsername, blackUsername, gameName, chessGame);
        game.setGameID(gameID);
        if(observers == null) {
            observers = List.of();
        }
        for(var observer : observers) {
            game.addObserver(observer);
        }
        return game;
    }

    public Game find(String name) throws DataAccessException {
        String whiteUsername = null;
        String blackUsername = null;
        Integer gameID = 0;
        List<String> observers = null;
        var gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ChessGame.class, new GameAdapter());
        var gson = gsonBuilder.create();
        ChessGame chessGame = null;
        try(var conn = dataBase.getConnection()) {
            try(var preparedStatement = conn.prepareStatement("SELECT * FROM game WHERE gameName = ?")) {
                preparedStatement.setString(1, name);
                var query = preparedStatement.executeQuery();
                while(query.next()) {
                    whiteUsername = query.getString("whiteUsername");
                    blackUsername = query.getString("blackUsername");
                    gameID = query.getInt("gameID");
                    observers = gson.fromJson(query.getString("observers"), List.class);
                    chessGame = gson.fromJson(query.getString("game"), ChessGame.class);
                }
            }
        }
        catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        var game = new Game(whiteUsername, blackUsername, name, chessGame);
        game.setGameID(gameID);
        if(observers == null) {
            observers = new ArrayList<String>();
        }
        for(var observer : observers) {
            game.addObserver(observer);
        }
        return game;
    }

    /**
     * Find all games in data
     * @return list of all games
     */
    public List<Game> findAll() throws DataAccessException {
        var allGames = new ArrayList<Game>();
        var gsonBuilder = new GsonBuilder();
        Integer gameID = 0;
        String whiteUsername = null;
        String blackUsername = null;
        String gameName = null;
        List<String> observers = null;
        gsonBuilder.registerTypeAdapter(ChessGame.class, new GameAdapter());
        var gson = gsonBuilder.create();
        ChessGame chessGame = null;
        try(var conn = dataBase.getConnection()) {
            try(var preparedStatement = conn.prepareStatement("SELECT * FROM game")) {
                var query = preparedStatement.executeQuery();
                while(query.next()) {
                    gameID = Integer.valueOf(query.getString("gameID"));
                    whiteUsername = query.getString("whiteUsername");
                    blackUsername = query.getString("blackUsername");
                    gameName = query.getString("gameName");
                    observers = gson.fromJson(query.getString("observers"), List.class);
                    chessGame = gson.fromJson(query.getString("game"), ChessGame.class);
                    var game = new Game(whiteUsername, blackUsername, gameName, chessGame);
                    game.setGameID(gameID);
                    for(var observer : observers) {
                        game.addObserver(observer);
                    }
                    allGames.add(game);
                }
            }
        }
        catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return allGames;
    }

    /**
     * clear all auth tokens from data
     */
    public void clear() throws DataAccessException {
        try(var conn = dataBase.getConnection()) {
            try(var preparedStatement = conn.prepareStatement("DROP TABLE game")) {
                preparedStatement.executeUpdate();
            }
        }
        catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }


    /**
     * Places a user as whitePlayer or blackPlayer
     * @param username user to be placed in game spot
     */
    public void claimPlayerSpot(String username, int gameID, String color) throws DataAccessException {
        try(var conn = dataBase.getConnection()) {
            color = color.toLowerCase();
            if(color.equals("white") || color.equals("black")) {
                var sqlStr = """
                        UPDATE game
                        SET"""
                        + " "+ color + """
                        Username  = ?
                        WHERE gameID = ?
                        """;
                try(var preparedStatement = conn.prepareStatement(sqlStr)) {
                    preparedStatement.setString(1, username);
                    preparedStatement.setInt(2, 1000);
                    preparedStatement.executeUpdate();
                }
            }
            else {
                var sqlStr = """
                        UPDATE game
                        SET observers = ?
                        WHERE gameID = ?
                        """;
                var game = find(gameID);
                var gson = new Gson();
                var observers = game.getObservers();
                observers.add(username);
                try(var preparedStatement = conn.prepareStatement(sqlStr)) {
                    preparedStatement.setString(1, gson.toJson(observers));
                    preparedStatement.setInt(2, gameID);

                    preparedStatement.executeUpdate();
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public int getID(String gameName) {
        gameName = gameName.replaceAll("\"", "");
        try {
            var games = findAll();
            for(var game : games) {
                if(game.getGameName().equals(gameName)) {
                    return game.getGameID();
                }
            }
            return 1000+games.size();
        }
        catch(DataAccessException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }


}
