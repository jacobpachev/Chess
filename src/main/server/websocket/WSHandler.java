package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
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
import webSocketMessages.userCommands.JoinObserver;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.UserGameCommand;

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
        var auth = new AuthToken("");
        try {
            auth = new AuthDAO().findByToken(gameCmd.getAuthString());
        }
        catch (Exception e) {
            session.getRemote().sendString(new Gson().toJson(new Error("Error 401: unauthorized")));
            throw new Exception("Error 401: unauthorized");
        }
        var userName = auth.getUsername();
        if(!userName.equals(userInGame)) {
            session.getRemote().sendString(json.toJson(new Error("Error 403: Color already taken")));
            System.out.println("Sending: "+json.toJson(new Error("Error 403: Color already taken")));
            return;
        }
        var notification = new Notification(userName+" joined as "+color);
        connectionManager.add(userName, session);
        connectionManager.sendToAll(userName, notification);
        session.getRemote().sendString(json.toJson(new LoadGame(game.getGame())));
    }

}
