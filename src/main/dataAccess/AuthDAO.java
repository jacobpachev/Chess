package dataAccess;

import models.AuthToken;
import models.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Access authentication data
 */
public class AuthDAO {
    private final Database dataBase;

    public AuthDAO() throws DataAccessException {
        this.dataBase = new Database();
        try (var conn = dataBase.getConnection()) {
            conn.setCatalog("chess");

            var createPetTable = """
                CREATE TABLE  IF NOT EXISTS auth (
                token VARCHAR(36) NOT NULL,
                username VARCHAR(255) NOT NULL,
                PRIMARY KEY (token)
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
     * place an AuthToken in data
     *
     * @param authToken AuthToken to place
     */
    public void insert(AuthToken authToken) throws DataAccessException {
        try(var conn = dataBase.getConnection()) {
            try(var preparedStatement = conn.prepareStatement("INSERT INTO auth (token, username) VALUES(?, ?)")) {
                preparedStatement.setString(1, authToken.getAuthToken());
                preparedStatement.setString(2, authToken.getUsername());
                preparedStatement.executeUpdate();
            }
        }
        catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }

    }

    /**
     * find a user's auth token
     *
     * @param username user to find auth token
     * @return int
     */
    public AuthToken findByName(String username) throws DataAccessException {
        var token = "";
        try(var conn = dataBase.getConnection()) {
            try(var preparedStatement = conn.prepareStatement("SELECT * FROM auth WHERE name = ?")) {
                preparedStatement.setString(1, username);
                var query = preparedStatement.executeQuery();
                while(query.next()) {
                    token = query.getString("token");
                }
            }
        }
        catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return new AuthToken(username, token);
    }

    public AuthToken findByToken(String token) throws DataAccessException {
        var username = "";
        try(var conn = dataBase.getConnection()) {
            try(var preparedStatement = conn.prepareStatement("SELECT * FROM auth WHERE token = ?")) {
                preparedStatement.setString(1, token);
                var query = preparedStatement.executeQuery();
                while(query.next()) {
                    username = query.getString("username");
                }
            }
        }
        catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return new AuthToken(username, token);
    }


    /**
     * Find all auth tokens in data
     *
     * @return list of all auth tokens
     */
    public List<AuthToken> findAll() throws DataAccessException {
        var allUsers = new ArrayList<AuthToken>();
        try(var conn = dataBase.getConnection()) {
            try(var preparedStatement = conn.prepareStatement("SELECT * FROM auth")) {
                var query = preparedStatement.executeQuery();
                while(query.next()) {
                    var username = query.getString("username");
                    var token = query.getString("token");
                    allUsers.add(new AuthToken(username, token));
                }
            }
        }
        catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return allUsers;
    }

    /**
     * clear all auth tokens from data
     */
    public void clear() throws DataAccessException {
        try(var conn = dataBase.getConnection()) {
            try(var preparedStatement = conn.prepareStatement("DROP TABLE auth")) {
                preparedStatement.executeUpdate();
            }
        }
        catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Removes authToken from data
     *
     * @param name username of token to remove
     */
    public void remove(String name) throws DataAccessException {
        try(var conn = dataBase.getConnection()) {
            try(var preparedStatement = conn.prepareStatement("DELETE FROM auth WHERE username = ?")) {
                preparedStatement.setString(1, name);
                preparedStatement.executeUpdate();
            }
        }
        catch(SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
