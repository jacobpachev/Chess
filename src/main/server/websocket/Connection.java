package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import java.io.IOException;

public class Connection {
    public Session session;
    public String userName;

    public Connection(Session session, String userName){
        this.session = session;
        this.userName = userName;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }

}
