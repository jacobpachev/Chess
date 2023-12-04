package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import requests.*;
import responses.JoinResponse;
import server.websocket.ConnectionManager;
import services.GameService;
import spark.Request;
import spark.Response;



public class GameHandler {
    Gson gson = new Gson();
    GameService gameService = new GameService();

    public Object list(Request request, Response response) {
        var jsonBody = new Gson();
        var listRequest = new ListRequest(request.headers("Authorization"));
        var listResponse = gameService.list(listRequest);
        switch (listResponse.getMessage()) {
            case null -> response.status(200);
            case "Error: unauthorized" -> response.status(401);
            default -> response.status(500);
        }
        return jsonBody.toJson(listResponse);
    }

    public Object create(Request request, Response response) {
        String gameName;
        if(!request.body().contains("{")) {
            gameName = request.body().replace("\"", "");
        }
        else {
            try {
                gameName = new Gson().fromJson(request.body(), CreateRequest.class).getGameName();
            } catch (Exception e) {
                response.status(400);
                return "{\"message\":\"Error: bad request\"}";
            }
        }
        gameName = gameName.strip().replace("\"", "");
        var createRequest = new CreateRequest(gameName,  request.headers("Authorization"));
        var createResponse = gameService.create(createRequest);
        switch (createResponse.getMessage()) {
            case null -> response.status(200);
            case "Error: unauthorized" -> response.status(401);
            case "Error: bad request" -> response.status(400);
            default -> response.status(500);
        }
        return gson.toJson(createResponse);
    }

    public Object join(Request request, Response response) {
        System.out.println("Join called");
        JoinRequest joinRequest;
        var authToken = request.headers("Authorization");
        if(authToken.length() != 36) {
            response.status(401);
            return gson.toJson(new JoinResponse("Error: unauthorized"));
        }
        joinRequest = gson.fromJson(request.body(), JoinRequest.class);
        joinRequest.setAuthToken(authToken);
        var joinResponse = gameService.join(joinRequest);
        switch (joinResponse.getMessage()) {
            case null -> response.status(200);
            case "Error: unauthorized" -> response.status(401);
            case "Error: bad request" -> response.status(400);
            case "Error: already taken" -> response.status(403);
            default -> response.status(500);
        }
        return gson.toJson(joinResponse);
    }
}
