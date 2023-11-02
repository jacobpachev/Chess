package daoTests;

import chess.GameImpl;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import models.Game;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class gameDaoTest {
    @BeforeEach
    @AfterEach
    void clear() {
        try {
            var gameDAO = new GameDAO();
            gameDAO.clear();
        }
        catch(DataAccessException e) {
            System.out.println("Data access error");
        }
    }

    @Test
    @DisplayName("Successful Clear")
    public void successClear() {
        try {
            var gameDao = new GameDAO();
            var game = new Game("Test", new GameImpl());
            gameDao.insert(game);
            gameDao.clear();
            gameDao.find(1000);
        }

        catch(DataAccessException e) {
            assertEquals("Table 'chess.game' doesn't exist", e.getMessage());
        }
    }

    @Test
    @DisplayName("Successful Insert")
    public void successInsert() {
        try {
            var gameDao = new GameDAO();
            var game = new Game("Test", new GameImpl());
            gameDao.insert(game);
            assertEquals(1000, gameDao.find("Test").getGameID(), "Wrong id");
            assertEquals("Test", gameDao.find("Test").getGameName(), "Wrong Name");
        }

        catch(DataAccessException e) {
            fail("Database Error");
        }
    }

    @Test
    @DisplayName("Failed Insert")
    public void failInsert() {
        try {
            var gameDao = new GameDAO();
            var game = new Game("Test", new GameImpl());
            gameDao.insert(game);
            game = gameDao.find("Test");
            gameDao.insert(game);
        }

        catch(DataAccessException e) {
            assertEquals("Id already in database", e.getMessage());
        }
    }

    @Test
    @DisplayName("Successful Find")
    public void successFind() {
        try {
            var gameDao = new GameDAO();
            var game = new Game("Test", new GameImpl());
            gameDao.insert(game);
            var foundGame = gameDao.find(1000);
            assertEquals(1000, foundGame.getGameID());
            assertEquals("Test", foundGame.getGameName());
        }

        catch(DataAccessException e) {
            fail("Database Error");
        }
    }

    @Test
    @DisplayName("Failed Find")
    public void failFind() {
        try {
            var gameDao = new GameDAO();
            assertNull(gameDao.find(1000).getGameName(), "Found a nonexistent game");
        }

        catch(DataAccessException e) {
            fail("Database Error");
        }
    }

    @Test
    @DisplayName("Successful Find All")
    public void successFindAll() {
        try {
            var gameDao = new GameDAO();
            var game1 = new Game("Test1", new GameImpl());
            gameDao.insert(game1);
            var game2 = new Game("Test2", new GameImpl());
            gameDao.insert(game2);
            var foundGames = gameDao.findAll();
            assertEquals(2, foundGames.size());
            assertEquals("Test1", foundGames.get(0).getGameName());
            assertEquals(1000, foundGames.get(0).getGameID());
            assertEquals("Test2", foundGames.get(1).getGameName());
            assertEquals(1001, foundGames.get(1).getGameID());
        }

        catch(DataAccessException e) {
            fail("Database Error");
        }
    }

    @Test
    @DisplayName("Failed Find All")
    public void failFindAll() {
        try {
            var gameDao = new GameDAO();
            var games = gameDao.findAll();
            assertEquals(0, games.size(), "Found games when there are none");
        }

        catch(DataAccessException e) {
            fail("Database Error");
        }
    }

    @Test
    @DisplayName("Successful Claim Spot")
    public void successClaimSpot() {
        try {
            var gameDao = new GameDAO();
            var game = new Game("Test", new GameImpl());
            gameDao.insert(game);

            gameDao.claimPlayerSpot("MoMo", 1000, "White");
            gameDao.claimPlayerSpot("Stephen", 1000, "Black");
            gameDao.claimPlayerSpot("Jacob", 1000, ""); //Observer

            assertEquals("MoMo", gameDao.find(1000).getWhiteUsername());
            assertEquals("Stephen", gameDao.find(1000).getBlackUsername());
            assertEquals("Jacob", gameDao.find(1000).getObservers().get(0));
        }

        catch(DataAccessException e) {
            fail("Database Error");
        }
    }

    @Test
    @DisplayName("Failed Claim Spot")
    public void failClaimSpot() {
        try {
            var gameDao = new GameDAO();
            var game = new Game("Test", new GameImpl());
            gameDao.insert(game);

            gameDao.claimPlayerSpot("MoMo", 1000, "Purple");
        }

        catch(DataAccessException e) {
            assertEquals("Not a valid color", e.getMessage());
        }
    }

}
