package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import requests.*;
import services.GameService;
import spark.Request;
import spark.Response;


public class GameHandler {
    Gson gson;
    GameService gameService;
    public GameHandler() {
        gson = new Gson();
        gameService = new GameService();
    }
    public Response list(Request request, Response response) {
        var jsonBody = new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return (f.getName().equals("observers") || f.getName().equals("game"));
                    }
                    @Override
                    public boolean shouldSkipClass(Class<?> aClass) {
                        return false;
                    }
                }).create();
        var listRequest = new ListRequest(request.headers("Authorization"));
        var listResponse = gameService.list(listRequest);
        switch (listResponse.getMessage()) {
            case null:
                response.status(200);
            case "Error: unauthorized":
                response.status(401);
            default:
                response.status(500);
        }
        System.out.println(jsonBody.toJson(listResponse));
        response.body(jsonBody.toJson(listResponse));
        return response;
    }

    public Response create(Request request, Response response) {
        var gameName = request.body().split(":")[1].replace("}", "");
        gameName = gameName.strip();
        var createRequest = new CreateRequest(request.headers("Authorization"), gameName);
        System.out.println(createRequest.getGameName());
        var createResponse = gameService.create(createRequest);
        if(createResponse.getGameID() == 0)
            response.body(gson.toJson(createResponse.getMessage()));
        response.body(gson.toJson(createResponse.getGameID()));
        switch (createResponse.getMessage()) {
            case null:
                response.status(200);
            case "Error: unauthorized":
                response.status(401);
            case "Error: bad request":
                response.status(400);
            default:
                response.status(500);
        }
        return response;
    }

    public Response join(Request request, Response response) {
        var color = request.body().split(":")[1].split(",")[0].strip();
        var gameID = Integer.parseInt(request.body().split(":")[2].replace("}", "").strip());
        var joinRequest = new JoinRequest(request.headers("Authorization"), color, gameID);
        var joinResponse = gameService.join(joinRequest);
        switch (joinResponse.getMessage()) {
            case null:
                response.status(200);
            case "Error: unauthorized":
                response.status(401);
            case "Error: bad request":
                response.status(400);
            case "Error: already taken":
                response.status(403);
            default:
                response.status(500);
        }
        response.body(gson.toJson(joinResponse));

        return response;
    }
}
