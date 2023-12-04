package websocket;

import chess.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.Game;
import ui.ChessClient;
import ui.Repl;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;

import javax.websocket.*;
import java.net.URI;

public class WSFacade extends Endpoint {

    public Session session;
    public WSFacade(ChessClient clientUi) throws Exception {
        URI uri = new URI("ws://localhost:8080/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message) {
                ChessGame game = null;
                if(message.contains("board")) {
                    var gsonBuilder = new GsonBuilder();
                    gsonBuilder.registerTypeAdapter(ChessGame.class, new GameAdapter());
                    game = gsonBuilder.create().fromJson(message, ChessGame.class);
                }
                if(game == null) {
                    clientUi.notify("\n" + message);
                }
                else {
                    clientUi.redraw(game, null, null);
                }
            }
        });
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }

}
