import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import models.User;

public class Main {
    public static void main(String[] args) throws DataAccessException {
        User user = new User("jacob", "123", "jacobpachev@gmail.com");
        User user2 = new User("jacob2", "123", "jacobpachev2@gmail.com");
        UserDAO userData = new UserDAO();
        UserDAO userData2 = new UserDAO();
        userData.insert(user);
        userData2.insert(user2);


    }
}
