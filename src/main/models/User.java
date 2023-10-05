package models;

import java.util.Objects;

/**
 * model of User
 */
public class User {
    private int userID;
    private String userName;
    private String password;
    private String email;

    public User(String userName, String password, String email) {
        this.userID = generateUserID();
        this.userName = userName;
        this.password = password;
        this.email = email;
    }

    /**
     * generate user ID
     * @return userID
     */
    private int generateUserID(){return 0;};

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
