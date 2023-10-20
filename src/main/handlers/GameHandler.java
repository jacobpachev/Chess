package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import requests.*;
import responses.JoinResponse;
import services.GameService;
import spark.Request;
import spark.Response;

import java.util.Arrays;


public class GameHandler {
    Gson gson;
    GameService gameService;
    public GameHandler() {
        gson = new Gson();
        gameService = new GameService();
    }
    public Object list(Request request, Response response) {
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
            case null -> response.status(200);
            case "Error: unauthorized" -> response.status(401);
            default -> response.status(500);
        }
        response.body(jsonBody.toJson(listResponse));
        return jsonBody.toJson(listResponse);
    }

    public Object create(Request request, Response response) {
        var gameName = "";
        try {
            gameName = request.body().split(":")[1].replace("}", "");
        }
        catch (ArrayIndexOutOfBoundsException e) {
            response.status(400);
            return "{\"message\":\"Error: bad request\"}";
        }

        gameName = gameName.strip();
        var createRequest = new CreateRequest(request.headers("Authorization"), gameName);
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
        var jsonBody = "";
        JoinRequest joinRequest;
        var authToken = request.headers("Authorization");
        if(authToken.length() != 36) {
            response.status(401);
            return gson.toJson(new JoinResponse("Error: unauthorized"));
        }
        if(request.body().split(":")[0].contains("playerColor")) {
            System.out.println(request.body().split(":")[1]);
            jsonBody = "{\"authToken\": \"" + authToken + "\", " + request.body().replace("{", "");
        }
        else {
            jsonBody = "{\"authToken\": \"" + authToken + "\", \"playerColor\": null, " + request.body().replace("{", "");
        }
        System.out.println(jsonBody);

        joinRequest = gson.fromJson(jsonBody, JoinRequest.class);
        var joinResponse = gameService.join(joinRequest);
        switch (joinResponse.getMessage()) {
            case null -> response.status(200);
            case "Error: unauthorized" -> response.status(401);
            case "Error: bad request" -> response.status(400);
            case "Error: already taken" -> response.status(403);
            default -> response.status(500);
        }
        GameDAO gameDAO = new GameDAO();
        try {
            System.out.println(gameDAO.find(joinRequest.getGameID()).getWhiteUsername());
        }
        catch (DataAccessException e) {
            System.out.println("data error");
        }
        return gson.toJson(joinResponse);
    }
}
