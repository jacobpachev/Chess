package daoTests;

import chess.GameImpl;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class userDaoTest {
    @BeforeEach
    @AfterEach
    void clear() {
        try {
            var userDAO = new UserDAO();
            userDAO.clear();
        } catch (DataAccessException e) {
            System.out.println("Data access error");
        }
    }

    @Test
    @DisplayName("Successful Clear")
    public void successClear() {
        clear();
        try {
            var userDao = new UserDAO();
            var user = new User("Jacob", "jacob123", "jap@byu.edu");
            userDao.insert(user);
            userDao.clear();
            userDao.find("Jacob");
        } catch (DataAccessException e) {
            assertEquals("Table 'chess.user' doesn't exist", e.getMessage());
        }
    }

    @Test
    @DisplayName("Successful Insert")
    public void successInsert() {
        try {
            var userDao = new UserDAO();
            var user = new User("Jacob", "jacob123", "jap@byu.edu");
            userDao.insert(user);
            assertEquals("Jacob", userDao.find("Jacob").getUserName(), "Wrong name");
        } catch (DataAccessException e) {
            fail("Database Error");
        }
    }

    @Test
    @DisplayName("Failed Insert")
    public void failInsert() {
        try {
            var userDao = new UserDAO();
            var user = new User("Jacob", "jacob123", "jap@byu.edu");
            userDao.insert(user);
            userDao.insert(user);
        } catch (DataAccessException e) {
            assertEquals("User already in database", e.getMessage());
        }
    }

    @Test
    @DisplayName("Successful Find")
    public void successFind() {
        try {
            var userDao = new UserDAO();
            var user = new User("Jacob", "jacob123", "jap@byu.edu");
            userDao.insert(user);
            var foundUser = userDao.find("Jacob");
            assertEquals("Jacob", foundUser.getUserName());
        } catch (DataAccessException e) {
            fail("Database Error");
        }
    }

    @Test
    @DisplayName("Failed Find")
    public void failFind() {
        try {
            var userDao = new UserDAO();
            assertNull(userDao.find("Jacob"), "Found a nonexistent game");
        } catch (DataAccessException e) {
            fail("Database Error");
        }
    }

    @Test
    @DisplayName("Successful Find All")
    public void successFindAll() {
        try {
            var userDao = new UserDAO();
            var user1 = new User("MoMo", "123", "mp@gmail.com");
            userDao.insert(user1);
            var user2 = new User("Stephen", "food", "stephen");
            userDao.insert(user2);
            var foundUsers = userDao.findAll();
            assertEquals(2, foundUsers.size());
            assertEquals("MoMo", foundUsers.get(0).getUserName());
            assertEquals("Stephen", foundUsers.get(1).getUserName());
        }

        catch(DataAccessException e) {
            fail("Database Error");
        }
    }

    @Test
    @DisplayName("Failed Find All")
    public void failFindAll() {
        try {
            var userDao = new GameDAO();
            var users = userDao.findAll();
            assertEquals(0, users.size(), "Found games when there are none");
        }

        catch(DataAccessException e) {
            fail("Database Error");
        }
    }
}
