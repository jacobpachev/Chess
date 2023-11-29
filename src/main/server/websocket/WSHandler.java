package server.websocket;

import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;

import javax.websocket.Session;



public class WSHandler {

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws Exception {
        System.out.println(msg);
    }

}
