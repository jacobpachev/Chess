package ui;

import chess.*;
import com.google.gson.Gson;
import models.Game;
import models.User;
import requests.*;
import serverFacade.ServerFacade;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinObserver;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.Leave;
import webSocketMessages.userCommands.MakeMove;
import websocket.WSFacade;

import java.util.*;

import static ui.EscapeSequences.UNICODE_ESCAPE;

public class ChessClient {
    boolean loggedIn = false;
    boolean playing = false;
    String token = null;
    ServerFacade server;
    String curUser;
    String curColor;
    WSFacade wsFacade;
    ChessGame game;
    Integer curID;
    Gson json = new Gson();

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        try {
            wsFacade = new WSFacade(this);
        }
        catch(Exception e) {

            System.out.println(e.getMessage());
        }
    }

    public void notify(String message) {
        if(message.contains("NOTIFICATION")) {
            message = new Gson().fromJson(message, Notification.class).getMessage();
        }
        if(message.contains("ERROR")) {
            message = new Gson().fromJson(message, Error.class).getErrorMessage();
        }
        System.out.println("\n"+EscapeSequences.SET_TEXT_BOLD+EscapeSequences.SET_TEXT_COLOR_RED+message);
        System.out.println(UNICODE_ESCAPE+"[0m");
        System.out.print(">>>");
    }

    public String redraw(ChessGame game, ArrayList<ChessPosition> highlights, ChessPosition pieceToHighlight) {
        var empty = true;
        for (int i = 8; i >= 1; i--) {
            for (int j = 1; j <= 8; j++) {
                if(game.getBoard().getPiece(new MyPosition(i, j)) != null) {
                    empty = false;
                }
            }
        }
        if(empty) {
            game.getBoard().resetBoard();
        }
        if(curColor.equals("white") || curColor.equals("observer")) {
            System.out.println("\nwhite");
            new RenderBoard().renderBoard(game.getBoard(), "white", highlights, pieceToHighlight);
        }
        System.out.println();
        if(curColor.equals("black")) {
            System.out.println("black");
            new RenderBoard().renderBoard(game.getBoard(), "black", highlights, pieceToHighlight);
        }
        System.out.print(">>> ");
        return "";
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
            case "redraw" -> redraw(game, null, null);
            case "highlight" -> highlight(params);
            case "move" -> move(params);
            case "leave" -> leave();
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
            curUser = username;
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
            curUser = username;
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
            try {
                server.joinGame(new JoinRequest(token, color, Integer.parseInt(gameID)));
            }
            catch(Exception e) {
                wsFacade.send(e.getMessage());
                throw new Exception(e.getMessage());
            }
            color = (color.isEmpty()) ? "observer" : color;
            curColor = color;
            var teamColor = (color.equals("white")) ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
            var board = new RenderBoard();
            var game = new GameImpl();
            game.getBoard().resetBoard();
            if(color.equals("white") || color.equals("observer")) {
                System.out.println("White\n");
                board.renderBoard(game.getBoard(), "white", null, null);
            }
            if(color.equals("black")) {
                System.out.println("Black\n");
                board.renderBoard(game.getBoard(), "black", null, null);
            }
            if(color.equals("observer")) {
                System.out.println();
                wsFacade.send(json.toJson(new JoinObserver(token, Integer.parseInt(gameID), curUser)));
            }

            if(!color.equals("observer")) {
                wsFacade.send(json.toJson(new JoinPlayer(token, Integer.parseInt(gameID), teamColor, curUser)));
            }
            playing = true;
            this.game = game;
            curID = Integer.valueOf(gameID);
            return "Joined game " + gameID + " as " + color +"\n";
        }
        throw new Exception("400 Bad Format");
    }

    public String highlight(String[] params) throws Exception {
        if(!playing) {
            throw new Exception("Must be in a game to highlight legal moves");
        }
        var piecePos = params[0];
        if(piecePos.length() != 2) {
            throw new Exception("400 Bad Format");
        }
        var colLetter = piecePos.substring(0,1);
        var col = (int) colLetter.charAt(0)-96;
        var row = Integer.parseInt(piecePos.substring(1));
        var pos = new MyPosition(row, col);
        var highlights = new ArrayList<ChessPosition>();
        for(var move : game.validMoves(pos)) {
            highlights.add(move.getEndPosition());
        }
        redraw(game, highlights, pos);
        return "\n";
    }

    public String move(String[] params) throws Exception {
        var startStr = params[0];
        var endStr = params[1];
        var colStart = (int) startStr.substring(0,1).charAt(0)-96;
        var rowStart = Integer.parseInt(startStr.substring(1));
        var startPos = new MyPosition(rowStart, colStart);
        var colEnd = (int) endStr.substring(0,1).charAt(0)-96;
        var rowEnd = Integer.parseInt(endStr.substring(1));
        var endPos = new MyPosition(rowEnd, colEnd);
        var move = new MyChessMove(startPos, endPos);
        wsFacade.send(new Gson().toJson(new MakeMove(token, curID, move)));
        return "Moved from "+startStr+" to "+endStr;
    }

    public String leave() throws Exception {
        if(!playing) {
            throw new Exception("Must be in a game to leave it");
        }
        playing = false;
        wsFacade.send(new Gson().toJson(new Leave(token, curID)));
        return "Left game";
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
        else if (playing) {
            return """
            - help Displays this message
            - redraw Redraws the board
            - leave Leaves current game
            - move <start-pos> <end-pos> Makes a move from start-pos to end-pos(e.g. e2 e4)
            - resign Resigns current game
            - highlight <piece-pos> Highlights all legal moves for selected piece
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
