package dataAccess;

import models.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Access user data
 */
public class UserDAO {
    private final Database dataBase;
    public UserDAO() throws DataAccessException{
        this.dataBase = new Database();
        try(var conn = dataBase.getConnection()) {
            conn.setCatalog("chess");

            var createPetTable = """
            CREATE TABLE  IF NOT EXISTS user (
                id INT NOT NULL AUTO_INCREMENT,
                name VARCHAR(255) NOT NULL,
                password VARCHAR(255) NOT NULL,
                email VARCHAR(255) NOT NULL,
                PRIMARY KEY (id)
            )""";

            try (var createTableStatement = conn.prepareStatement(createPetTable)) {
                createTableStatement.executeUpdate();
            }
        }
        catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * place a User in data
     * @param user User to place
     */
    public void insert(User user) throws DataAccessException {
        try(var conn = dataBase.getConnection()) {
            try(var preparedStatement = conn.prepareStatement("INSERT INTO user (name, password, email) VALUES(?, ?, ?)")) {
                preparedStatement.setString(1, user.getUserName());
                preparedStatement.setString(2, user.getPassword());
                preparedStatement.setString(3, user.getEmail());

                preparedStatement.executeUpdate();
            }
        }
        catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }

    }

    /**
     * find a user's ID
     * @param username user to find ID
     * @return int
     */
    public User find(String username) throws DataAccessException {
        var password = "";
        var email = "";
        try(var conn = dataBase.getConnection()) {
            try(var preparedStatement = conn.prepareStatement("SELECT * FROM user WHERE name = ?")) {
                preparedStatement.setString(1, username);
                var query = preparedStatement.executeQuery();
                while(query.next()) {
                    password = query.getString("password");
                    email = query.getString("email");
                }
            }
        }
        catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return new User(username, password, email);
    }

    /**
     * Find all Users in data
     * @return list of all Users
     */
    public List<User> findAll() throws DataAccessException {
        var allUsers = new ArrayList<User>();
        try(var conn = dataBase.getConnection()) {
            try(var preparedStatement = conn.prepareStatement("SELECT * FROM user")) {
                var query = preparedStatement.executeQuery();
                while(query.next()) {
                    var username = query.getString("name");
                    var password = query.getString("password");
                    var email = query.getString("email");
                    allUsers.add(new User(username, password, email));
                }
            }
        }
        catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return allUsers;
    }

    /**
     * clear all Users from data
     */
    public void clear() throws DataAccessException {
        try(var conn = dataBase.getConnection()) {
            try(var preparedStatement = conn.prepareStatement("DROP TABLE user")) {
                preparedStatement.executeUpdate();
            }
        }
        catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }


}
