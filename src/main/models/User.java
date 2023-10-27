package models;

/**
 * model of User
 */
public class User {
    private final String userName;
    private final String password;
    private final String email;

    public User(String userName, String password, String email) {
        this.userName = userName;
        this.password = password;
        this.email = email;
    }


    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
    public String getEmail() {
        return email;
    }


}
