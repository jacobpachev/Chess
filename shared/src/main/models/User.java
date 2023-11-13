package models;

/**
 * model of User
 */
public class User {
    private final String username;
    private final String password;
    private final String email;

    public User(String userName, String password, String email) {
        this.username = userName;
        this.password = password;
        this.email = email;
    }


    public String getUserName() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    public String getEmail() {
        return email;
    }


}
