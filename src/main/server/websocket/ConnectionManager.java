package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.Notification;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    //Concurrent to be thread safe
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String username, Session session) {
        var connection = new Connection(session, username);
        connections.put(username, connection);
    }

    public void remove(String username) {
        connections.remove(username);
    }

    public void sendToAll(String exceptUserName, Notification notification) throws IOException {
        var connectionsToRemove = new ArrayList<Connection>();
        for(var connection : connections.values()) {
            if(connection.session.isOpen()) {
                if(!connection.userName.equals(exceptUserName)) {
                    connection.send(new Gson().toJson(notification));
                }
            }
            else {
                connectionsToRemove.add(connection);
            }
        }

        for(var connection : connectionsToRemove) {
            connections.remove(connection.userName);
        }
    }

}
