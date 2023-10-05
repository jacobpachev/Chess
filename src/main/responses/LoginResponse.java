package responses;

public class LoginResponse extends spark.Response{
    private String authToken;
    private String username;
    public LoginResponse() {
        authToken = "";
        username = "";
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
