package daoTests;

import chess.GameImpl;
import dataAccess.DataAccessException;
import dataAccess.AuthDAO;
import models.AuthToken;
import models.Game;
import models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class authDaoTest {
    @BeforeEach
    @AfterEach
    void clear() {
        try {
            var authDAO = new AuthDAO();
            authDAO.clear();
        } catch (DataAccessException e) {
            System.out.println("Data access error");
        }
    }

    @Test
    @DisplayName("Successful Clear")
    public void successClear() {
        clear();
        try {
            var authDao = new AuthDAO();
            var authToken = new AuthToken("Jacob");
            authDao.insert(authToken);
            authDao.clear();
            authDao.findByName("Jacob");
        } catch (DataAccessException e) {
            assertEquals("Table 'chess.auth' doesn't exist", e.getMessage());
        }
    }

    @Test
    @DisplayName("Successful Insert")
    public void successInsert() {
        try {
            var authDao = new AuthDAO();
            var authToken = new AuthToken("Jacob");
            authDao.insert(authToken);
            assertEquals("Jacob", authDao.findByName("Jacob").getUsername());
        } catch (DataAccessException e) {
            fail("Database Error");
        }
    }

    @Test
    @DisplayName("Failed Insert")
    public void failInsert() {
        try {
            var authDao = new AuthDAO();
            var authToken = new AuthToken("Jacob");
            authDao.insert(authToken);
            authDao.insert(authToken);
        } catch (DataAccessException e) {
            assertEquals("Token already in database", e.getMessage());
        }
    }

    @Test
    @DisplayName("Successful Find")
    public void successFind() {
        try {
            var authDAO = new AuthDAO();
            var authToken = new AuthToken("Jacob");
            var token = authToken.getAuthToken();
            authDAO.insert(authToken);
            var foundAuth = authDAO.findByName("Jacob");
            assertEquals("Jacob", foundAuth.getUsername());
            assertEquals(token, foundAuth.getAuthToken());
        } catch (DataAccessException e) {
            fail("Database Error");
        }
    }

    @Test
    @DisplayName("Failed Find")
    public void failFind() {
        try {
            var authDAO = new AuthDAO();
            assertNull(authDAO.findByName("Jacob"), "Found a nonexistent game");
        } catch (DataAccessException e) {
            fail("Database Error");
        }
    }

    @Test
    @DisplayName("Successful Find All")
    public void successFindAll() {
        try {
            var authDAO = new AuthDAO();
            var authToken1 = new AuthToken("Jacob");
            authDAO.insert(authToken1);
            var authToken2 = new AuthToken("Momo");
            authDAO.insert(authToken2);
            var foundAuths = authDAO.findAll();
            assertEquals(2, foundAuths.size());
            assertTrue(foundAuths.get(0).getUsername().equals("Jacob") || foundAuths.get(0).getUsername().equals("Momo"));
            assertTrue(foundAuths.get(1).getUsername().equals("Jacob") || foundAuths.get(1).getUsername().equals("Momo"));

        } catch (DataAccessException e) {
            fail("Database Error");
        }
    }

    @Test
    @DisplayName("Failed Find All")
    public void failFindAll() {
        try {
            var authDAO = new AuthDAO();
            var auths = authDAO.findAll();
            assertEquals(0, auths.size(), "Found games when there are none");
        } catch (DataAccessException e) {
            fail("Database Error");
        }
    }

    @Test
    @DisplayName("Successful Remove")
    public void successRemove() {
        try {
            var authDAO = new AuthDAO();
            var authToken = new AuthToken("Jacob");
            authDAO.insert(authToken);
            authDAO.remove("Jacob");
            assertNull(authDAO.findByName("Jacob"));
        } catch (DataAccessException e) {
            fail("Database Error");
        }
    }

    @Test
    @DisplayName("Failed Remove")
    public void failRemove() {
        try {
            var authDAO = new AuthDAO();
            authDAO.remove("Jacob");

        } catch (DataAccessException e) {
            fail("Database Error");

        }
    }
}