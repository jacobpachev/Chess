package server.websocket;

import chess.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import models.AuthToken;
import models.Game;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import javax.websocket.OnError;
import java.io.IOException;


@WebSocket
public class WSHandler {

    private final ConnectionManager connectionManager = new ConnectionManager();
    private final Gson json = new Gson();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        if(message.contains("Error")) {
            session.getRemote().sendString(new Gson().toJson(new Error(message)));
            System.out.println("Sending: "+new Gson().toJson(new Error(message)));
            return;
        }
        var gameCmd = json.fromJson(message, UserGameCommand.class);
        switch (gameCmd.getCommandType()) {
            case JOIN_OBSERVER -> joinObserver(session, message);
            case JOIN_PLAYER -> joinPlayer(session, message);
            case MAKE_MOVE -> move(session, message);
            case RESIGN -> resign(session, message);
            case LEAVE -> leave(session, message);
        }
    }

    @OnError
    public void onError(Session session, Throwable t) throws IOException {
        System.out.println("sending "+t.getMessage());
        session.getRemote().sendString("Error "+t.getMessage());
    }

    private void joinObserver(Session session, String message) throws Exception {
        var gameCmd = json.fromJson(message, JoinObserver.class);
        var gameId = gameCmd.getGameID();
        var gameDao = new GameDAO();
        var game = gameDao.find(gameId);
        if(game.getGameName() == null) {
            session.getRemote().sendString(json.toJson(new Error("Error 403: Bad request, game id not in database")));
            return;
        }
        AuthToken auth;
        try {
            auth = new AuthDAO().findByToken(gameCmd.getAuthString());
        }
        catch (DataAccessException e) {
            session.getRemote().sendString(json.toJson(new Error("Error 401: Unauthorized")));
            throw new DataAccessException(e.getMessage());
        }
        var userName = auth.getUsername();
        var notification = new Notification(userName+" now observing");

        connectionManager.add(userName, session);
        connectionManager.sendToAll(userName, notification);
        session.getRemote().sendString(json.toJson(new LoadGame(game.getGame())));

    }

    private void joinPlayer(Session session, String message) throws Exception {
        var gameCmd = json.fromJson(message, JoinPlayer.class);
        var color = gameCmd.getPlayerColor();
        var gameId = gameCmd.getGameID();
        var gameDao = new GameDAO();
        var game = gameDao.find(gameId);
        var userInGame = (color.equals(ChessGame.TeamColor.WHITE)) ? game.getWhiteUsername() : game.getBlackUsername();
        var userName = findUser(session, gameCmd.getAuthString());
        if(!userName.equals(userInGame)) {
            session.getRemote().sendString(json.toJson(new Error("Error 403: Color already taken")));
            return;
        }
        var notification = new Notification(userName+" joined as "+color);
        connectionManager.add(userName, session);
        connectionManager.sendToAll(userName, notification);
        session.getRemote().sendString(json.toJson(new LoadGame(game.getGame())));
    }

    private void move(Session session, String message) throws Exception {
        var gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ChessPosition.class, new PosAdapter());
        gsonBuilder.registerTypeAdapter(ChessMove.class, new MoveAdapter());

        var gameCmd = gsonBuilder.create().fromJson(message, MakeMove.class);
        var userName = findUser(session, gameCmd.getAuthString());
        var gameDao = new GameDAO();
        var game = gameDao.find(gameCmd.getGameID());
        var board = game.getGame().getBoard();
        var empty = true;
        for (int i = 8; i >= 1; i--) {
            for (int j = 1; j <= 8; j++) {
                if(board.getPiece(new MyPosition(i, j)) != null) {
                    empty = false;
                }
            }
        }
        if(empty) {
            board.resetBoard();
            game.getGame().setBoard(board);
        }
        try {
            game.getGame().makeMove(gameCmd.getMove());
        }
        catch (Exception e) {
            session.getRemote().sendString(new Gson().toJson(new Error(message)));
            return;
        }
        game.setGame(game.getGame());
        gameDao.setGame(game);
        var notification = new Notification(userName+" moved "+gameCmd.getMove());
        connectionManager.sendToAll("", new LoadGame(game.getGame()));
        connectionManager.sendToAll(userName, notification);

        if(game.getGame().isInCheck(ChessGame.TeamColor.WHITE)) {
            connectionManager.sendToAll("", new Notification(game.getWhiteUsername()+" is in check!"));
        }

        if(game.getGame().isInCheck(ChessGame.TeamColor.BLACK)) {
            connectionManager.sendToAll("", new Notification(game.getBlackUsername()+" is in check!"));
        }

        if(game.getGame().isInCheckmate(ChessGame.TeamColor.WHITE)) {
            connectionManager.sendToAll("", new Notification(game.getWhiteUsername()+" got checkmated!"));
        }

        if(game.getGame().isInCheckmate(ChessGame.TeamColor.BLACK)) {
            connectionManager.sendToAll("", new Notification(game.getBlackUsername()+" got checkmated!"));
        }

    }

    private void resign(Session session, String message) throws Exception {
        var gameCmd = json.fromJson(message, Resign.class);
        var userName = findUser(session, gameCmd.getAuthString());
        connectionManager.remove(userName);
        var notification = new Notification(userName+" resigned");
        connectionManager.sendToAll(userName, notification);
    }

    private void leave(Session session, String message) throws Exception {
        var gameCmd = json.fromJson(message, Leave.class);
        var userName = findUser(session, gameCmd.getAuthString());
        connectionManager.remove(userName);
        var gameDao = new GameDAO();
        var game = gameDao.find(gameCmd.getGameID());
        String color = "";
        var whiteUser = game.getWhiteUsername();
        var blackUser = game.getBlackUsername();
        if(whiteUser != null) {
            if (whiteUser.equals(userName)) {
                color = "white";
            }
        }
        if(blackUser != null) {
            if (blackUser.equals(userName)) {
                color = "black";
            }
        }
        if(color.isEmpty()) {
            gameDao.removeObserver(userName, game.getGameID());
        }
        else {
            gameDao.claimPlayerSpot(null, gameCmd.getGameID(), color);
        }
        var notification = new Notification(userName+" left the game");
        connectionManager.sendToAll(userName, notification);
    }

    private String findUser(Session session, String token) throws Exception {
        var auth = new AuthToken("");
        try {
            auth = new AuthDAO().findByToken(token);
        }
        catch (Exception e) {
            session.getRemote().sendString(new Gson().toJson(new Error("Error 401: unauthorized")));
            throw new Exception("Error 401: unauthorized");
        }
        return auth.getUsername();
    }

}
