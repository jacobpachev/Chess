package ui;

import chess.GameImpl;
import models.User;
import requests.*;
import serverFacade.ServerFacade;

import java.util.*;

public class ChessClient {
    boolean loggedIn = false;
    String token = null;
    ServerFacade server;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
    }

    public String eval(String line) throws Exception {
        var allValues = line.toLowerCase().split(" ");
        var command = (allValues.length > 0) ? allValues[0] : "help";
        var params = Arrays.copyOfRange(allValues, 1, allValues.length);
        return switch (command) {
            default -> help();
            case "quit" -> quit();
            case "register" -> register(params);
            case "login" -> login(params);
            case "logout" -> logout();
            case "create" -> create(params);
            case "list" -> list();
            case "join" -> join(params);
        };
    }

    public String register(String[] params) throws Exception {
        if(loggedIn) {
            throw new Exception("400 Already signed in");
        }
        if(params.length >= 1) {
            loggedIn = true;
            var username = params[0];
            var password = params[1];
            var email = params[2];
            token = server.addUser(new User(username, password, email)).getAuthToken();
            return "Registered "+username+"\n";
        }
        throw new Exception("400 Bad Format");
    }

    public String login(String[] params) throws Exception {
        if(loggedIn) {
            throw new Exception("400 Only one user can be signed in");
        }
        if(params.length >= 2) {
            var username = params[0];
            var password = params[1];
            token = server.loginUser(new LoginRequest(username, password)).getAuthToken();
            loggedIn = true;
            return "Logged in as "+username+"\n";
        }
        throw new Exception("400 Bad Format");
    }

    public String create(String[] params) throws Exception {
        if(!loggedIn) {
            throw new Exception("400 Not signed in");
        }
        if(params.length >= 1) {
            var gameName = params[0];
            server.addGame(new CreateRequest(gameName, token));
            return "Created "+gameName+"\n";
        }
        throw new Exception("400 Bad Format");
    }

    public String list() throws Exception {
        if(!loggedIn) {
            throw new Exception("400 Not signed in");
        }
        var games = server.listGames(new ListRequest(token)).getGames();
        var gamesListStr = new StringBuilder();
        for (var game : games) {
            gamesListStr.append("Game ").append(game.getGameName()).append("\n");
            gamesListStr.append("\tId: ").append(game.getGameID()).append("\n");
            gamesListStr.append("\tWhite player: ").append(game.getWhiteUsername()).append("\n");
            gamesListStr.append("\tBlack player: ").append(game.getBlackUsername()).append("\n");
            gamesListStr.append("\tObservers: ").append(game.getObservers()).append("\n");
        }
        return gamesListStr.toString();
    }

    public String join(String[] params) throws Exception {
        if(!loggedIn) {
            throw new Exception("400 Not signed in");
        }
        if(params.length >= 1) {
            var color = "";
            var gameID = params[0];
            if(params[0].equals("white") || params[0].equals("black")) {
                color = params[0];
                gameID = params[1];
            }
            server.joinGame(new JoinRequest(token, color, Integer.parseInt(gameID)));
            color = (color.isEmpty()) ? "observer" : color;
            var board = new RenderBoard();
            var game = new GameImpl();
            game.getBoard().resetBoard();
            System.out.println("White\n");
            board.renderBoard(game.getBoard(), "white");
            System.out.println("Black\n");
            board.renderBoard(game.getBoard(), "black");
            if(color.equals("observer")) {
                System.out.println();
            }
            return "Joined game " + gameID + " as " + color +"\n";
        }
        throw new Exception("400 Bad Format");
    }

    public String logout() throws Exception {
        if(!loggedIn) {
            throw new Exception("400 Not signed in");
        }
        server.logoutUser(token);
        loggedIn = false;
        return "Logged out";
    }

    public String quit() throws Exception{
        if(loggedIn) {
            logout();
        }
        System.out.println("Goodbye!");
        return "quit";
    }

    public String help() {
        if(!loggedIn) {
            return """
                    - help Displays this message
                    - quit Exit the program
                    - login <username> <password> Logs an already registered user in
                    - register <username> <password> <email> Registers a user with given info
                    """;
        }
        return """
                - help Displays this message
                - logout Logs user out, transitions back to pre game
                - create <name> Create a new game
                - list List all games
                - join <WHITE/BLACK> <gameID> Join game as white or black
                - join <empty> <gameID> Join game as observer
                """;
    }
}
