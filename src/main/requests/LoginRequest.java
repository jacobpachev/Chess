package requests;

/**
 * RegisterRequest class, this is basically a container class
 * just stores a request to register in class form
 */
public class LoginRequest extends spark.Request{
    private String message;
    private String username;
    private String password;

    /**
     * Constructor that takes
     * @param username username
     * @param password password
     * @param message error/success message
     */
    public LoginRequest( String username, String password) {
        this.username = username;
        this.password = password;
    }

    public LoginRequest(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
