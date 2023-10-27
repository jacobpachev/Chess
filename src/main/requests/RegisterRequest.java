package requests;

/**
 * RegisterRequest class, this is basically a container class
 * just stores a request to register in class form
 */
public class RegisterRequest {
    private String username;
    private final String password;
    private final String email;

    /**
     * Constructor that takes
     * @param username username
     * @param password password
     * @param email email
     */
    public RegisterRequest(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
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


    public String getEmail() {
        return email;
    }


    @Override
    public String toString() {
        return "RegisterRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
