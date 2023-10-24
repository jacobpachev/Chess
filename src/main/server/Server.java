package server;

import handlers.AdminHandler;
import handlers.UserHandler;
import handlers.GameHandler;

import spark.Spark;
public class Server {
    public static void main(String[] args) {
        int port = 8080;
        Spark.port(port);
        var adminHandler = new AdminHandler();
        var gameHandler = new GameHandler();
        var userHandler = new UserHandler();
        Spark.externalStaticFileLocation("web");
        Spark.init();
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

}
