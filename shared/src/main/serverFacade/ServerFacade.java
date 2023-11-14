package serverFacade;

import chess.ChessGame;
import chess.GameAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.User;
import requests.*;
import responses.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.*;

public class ServerFacade {
    String serverUrl;
    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public ClearResponse clear() throws Exception {
        var path = "/db";
        var url = (new URI(serverUrl+path).toURL());
        var http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod("DELETE");
        http.setDoOutput(true);
        http.connect();
        if(http.getResponseCode() != 200) {
            throw new Exception("Error "+http.getResponseCode()+" "+http.getResponseMessage());
        }
        return readBody(ClearResponse.class, http);
    }

    public RegisterResponse addUser(User user) throws Exception {
        return makeRequest("/user", "POST", user, RegisterResponse.class, "");
    }

    public LoginResponse loginUser(LoginRequest user) throws Exception {
        return makeRequest("/session", "POST", user, LoginResponse.class, "");
    }

    public LogoutResponse logoutUser(String token) throws Exception {
        return makeRequest("/session", "DELETE", null, LogoutResponse.class, token);
    }

    public CreateResponse addGame(CreateRequest req) throws Exception {
        return makeRequest("/game", "POST", req, CreateResponse.class, req.getAuthToken());
    }

    public JoinResponse joinGame(JoinRequest req) throws Exception {
        return makeRequest("/game", "PUT", req, JoinResponse.class, req.getAuthToken());
    }

    public ListResponse listGames(ListRequest req) throws Exception {
        return makeRequest("/game", "GET", null, ListResponse.class, req.getAuthToken());
    }


    private <T> T makeRequest(String path, String method, Object req, Class<T> T, String token) throws Exception {
        var url = (new URI(serverUrl+path).toURL());
        var http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod(method);
        if(!token.isEmpty()) {
            http.addRequestProperty("Authorization", token);
        }
        http.setDoOutput(true);
        if(req != null) {
            writeBody(req, http);
        }
        http.connect();
        if(http.getResponseCode() != 200) {
            throw new Exception("Error "+http.getResponseCode()+" "+http.getResponseMessage());
        }
        return readBody(T, http);
    }

    private static void writeBody(Object req, HttpURLConnection http) throws IOException {
        if(req != null) {
            http.addRequestProperty("Content-Type", "application/json");

            var reqData = new Gson().toJson(req);
            try (var reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private static <T> T readBody(Class<T> responseClass, HttpURLConnection connection) throws IOException {
        T response = null;
        var gsonBuilder = new GsonBuilder();
        if(connection.getContentLength() < 0) {
            try(var inputStream = connection.getInputStream()) {
                var reader = new InputStreamReader(inputStream);
                if(responseClass != null) {
                    gsonBuilder.registerTypeAdapter(ChessGame.class, new GameAdapter());
                    response = gsonBuilder.create().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }
}
