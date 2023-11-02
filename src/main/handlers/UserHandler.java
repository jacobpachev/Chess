package handlers;

import requests.LoginRequest;
import requests.LogoutRequest;
import requests.RegisterRequest;
import services.UserService;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;

public class UserHandler {
    Gson gson;
    UserService userService;
    public UserHandler() {
        gson = new Gson();
        userService = new UserService();
    }
    public Object register(Request req, Response response) {
        var registerRequest = gson.fromJson(req.body(), RegisterRequest.class);

        var registerResponse = userService.register(registerRequest);
        System.out.println(registerResponse.getMessage());
        switch (registerResponse.getMessage()) {
            case null -> response.status(200);
            case "Error: bad request" -> response.status(400);
            case "Error: already taken", "Error: unauthorized", "Error: User already in database" -> response.status(403);
            default -> response.status(500);
        }
        System.out.println(response.status());
        return gson.toJson(registerResponse);
    }

    public Object login(Request request, Response response) {
        var loginRequest = gson.fromJson(request.body(), LoginRequest.class);
        var loginResponse = userService.login(loginRequest);
        System.out.println(loginResponse.getMessage());
        switch (loginResponse.getMessage()) {
            case null -> response.status(200);
            case "Error: unauthorized" -> response.status(401);
            default -> response.status(500);
        }
        return gson.toJson(loginResponse);
    }

    public Object logout(Request request, Response response) {
        var logoutRequest = new LogoutRequest(request.headers("Authorization"));
        var logoutResponse = userService.logout(logoutRequest);
        switch (logoutResponse.getMessage()) {
            case null -> response.status(200);
            case "Error: unauthorized" -> response.status(401);
            default -> response.status(500);
        }
        System.out.println("Logging out");
        return gson.toJson(logoutResponse);
    }
}
