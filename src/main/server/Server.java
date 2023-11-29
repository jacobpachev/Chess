package server;

import handlers.AdminHandler;
import handlers.UserHandler;
import handlers.GameHandler;

import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import spark.Spark;

@WebSocket
public class Server {
    public static void main(String[] args) {
        int port = 8080;
        Spark.port(port);

        var adminHandler = new AdminHandler();
        var gameHandler = new GameHandler();
        var userHandler = new UserHandler();
        Spark.externalStaticFileLocation("web");
        Spark.webSocket("/connect", Server.class);
        Spark.get("/echo:msg", (req, res) -> "HTTP response: " + req.params(":msg"));
        Spark.delete("/db", (req, res) -> adminHandler.clear(res));
        Spark.post("/user", userHandler::register);
        Spark.post("/session", userHandler::login);
        Spark.delete("/session", userHandler::logout);
        Spark.post("/game", gameHandler::create);
        Spark.get("/game", gameHandler::list);
        Spark.put("/game", gameHandler::join);

        Spark.options("/*", (request, response) -> {

            String accessControlRequestHeaders = request
                    .headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers",
                        accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request
                    .headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods",
                        accessControlRequestMethod);
            }

            return "OK";
        });

        Spark.before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        System.out.printf("Received: %s", message);
        session.getRemote().sendString("WebSocket response: " + message);
    }

}
