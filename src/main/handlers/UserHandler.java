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
    public Response register(Request req, Response response) {
        System.out.println("Register was called!");
        var registerRequest = gson.fromJson(req.body(), RegisterRequest.class);
        var registerResponse = userService.register(registerRequest);
        switch (registerResponse.getMessage()) {
            case null:
                response.status(200);
            case "Error: bad request":
                response.status(400);
            case "Error: already taken":
                response.status(403);
            default:
                response.status(500);
        }
        response.body(gson.toJson(registerResponse));
        System.out.println(response.body());
        System.out.println(response.status());
        return response;
    }

    public Response login(Request request, Response response) {
        var loginRequest = gson.fromJson(request.body(), LoginRequest.class);
        var loginResponse = userService.login(loginRequest);
        switch (loginResponse.getMessage()) {
            case null:
                response.status(200);
            case "Error: unauthorized":
                response.status(401);
            default:
                response.status(500);
        }
        response.body(gson.toJson(loginResponse));
        System.out.println(response.body());
        System.out.println(response.status());
        return response;
    }

    public Response logout(Request request, Response response) {
        var logoutRequest = new LogoutRequest(request.headers("Authorization"));
        var logoutResponse = userService.logout(logoutRequest);
        switch (logoutResponse.getMessage()) {
            case null:
                response.status(200);
            case "Error: unauthorized":
                response.status(401);
            default:
                response.status(500);
        }
        System.out.println(response.status());
        return response;
    }
}
