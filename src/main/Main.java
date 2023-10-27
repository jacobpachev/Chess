import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import models.User;

import java.io.*;
import java.net.URL;

import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) {
        try {
            UserDAO userDAO = new UserDAO();
            userDAO.insert(new User("Jacob", "howdy", "howdy@gmail.com"));
            userDAO.clear();
        }
        catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    private static String register() throws IOException {
        var client = (HttpURLConnection) (new URL("http://localhost:8080/user").openConnection());
        client.setRequestMethod("POST");
        client.addRequestProperty("Accept", "application/json");
        client.setDoOutput(true);
        OutputStream os = client.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8);
        osw.write("{ \"username\":\"jap2\", \"password\":\"jap123\", \"email\":\"gmail.com\" }");
        osw.flush();
        osw.close();
        os.close();
        client.connect();
        var response = getInput(client.getInputStream());
        return (response.split(":"))[2].replaceAll("\"", "").replace("}", "");
    }

    private static void join(String authToken, String gameID) throws IOException {
        var client = (HttpURLConnection) (new URL("http://localhost:8080/game").openConnection());
        client.setRequestMethod("PUT");
        client.addRequestProperty("Accept", "application/json");
        client.addRequestProperty("Authorization", authToken);
        client.setDoOutput(true);
        OutputStream os = client.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8);
        osw.write("{\"playerColor\":\"\", \"gameID\":\""+gameID+"\"}");
        osw.flush();
        osw.close();
        os.close();
        client.connect();
        var response = getInput(client.getInputStream());
        System.out.println(response);
    }

    private static void logout(String authToken) throws IOException {
        var client = (HttpURLConnection) (new URL("http://localhost:8080/session").openConnection());
        client.setRequestMethod("DELETE");
        client.setDoOutput(false);
        client.addRequestProperty("Accept", "application/json");
        client.addRequestProperty("Authorization", authToken);
        client.connect();
        System.out.println(client.getResponseCode());

    }

    private static String create(String authToken) throws IOException {
        var client = (HttpURLConnection) (new URL("http://localhost:8080/game").openConnection());
        client.setRequestMethod("POST");
        client.addRequestProperty("Accept", "application/json");
        client.addRequestProperty("Authorization", authToken);
        client.setDoOutput(true);
        OutputStream os = client.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8);
        osw.write("{ \"gameName\":\"test1\"}");
        osw.flush();
        osw.close();
        os.close();
        client.connect();
        var response = getInput(client.getInputStream());
        return (response.split(":"))[1].replaceAll("\"", "").replace("}", "");

    }

    private static void clear() throws IOException {
        var client = (HttpURLConnection) (new URL("http://localhost:8080/db").openConnection());
        client.setRequestMethod("DELETE");
        client.connect();
    }

    private static String getInput(InputStream var0) throws IOException {
        StringBuilder var1 = new StringBuilder();
        InputStreamReader var4 = new InputStreamReader(var0);
        char[] var2 = new char[1024];

        int var3;
        while((var3 = var4.read(var2)) > 0) {
            var1.append(var2, 0, var3);
        }

        return var1.toString();
    }
}
