package at.aau.server.database;

import at.aau.server.database.Table.User;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import shared.model.PlayerDTO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class DatabaseTest {

    private Database db;
    private User bestUser;

    @Before
    public void setup() throws SQLException {
        db= Database.getInstance();
        db.emptyUserTable();
        db.emptyScoreTable();
        db.addUser("65h454565", "Username1");
        bestUser = db.addUser("bz6b5z654", "Username2");
        db.addUser("34g654ght", "Username3");
        db.addUser("fhdz65htg", "Username4");
        db.addUser("l75375lk6", "Username5");
        db.addScore(db.getUserByMAC("65h454565").getId(), 123);
        db.addScore(db.getUserByMAC("bz6b5z654").getId(), 234);
        db.addScore(db.getUserByMAC("34g654ght").getId(), 94);
        db.addScore(db.getUserByMAC("34g654ght").getId(), 114);
        db.addScore(db.getUserByMAC("l75375lk6").getId(), 1);
    }

    @Test
    public void addUserTest() throws SQLException {
        User user = db.addUser("test-mac", "Username");
        assertEquals(6, db.getNumberOfAllUsers());
        User user2 = db.addUser("test-mac2", "Username2");
        assertEquals(7, db.getNumberOfAllUsers());
    }
    @Test
    public void delUserTest() throws SQLException {
        db.deleteUser(db.getUserByMAC("34g654ght").getId());
        assertEquals(4, db.getNumberOfAllUsers());
    }
    @Test
    public void addScoreTest() throws SQLException {
        db.addScore(12,123);
        assertEquals(6, db.getNumberOfAllScores());
    }
    @Test
    public void getAllScoresTest() throws SQLException {
        assertEquals(2,db.getAllScores(db.getUserByMAC("34g654ght").getId()).size());
    }
    @Test
    public void getAllUsersTest() throws SQLException {
        assertEquals(5,db.getAllUsers().size());
    }
    @Test
    public void getBestUserTest() throws SQLException {
        assertEquals(bestUser.getId(),db.getBestUser().getId());
    }
    @Test
    public void getUserByIDTest() throws SQLException {
        assertEquals(bestUser.getId(),db.getUserByID(bestUser.getId()).getId());
    }
    @Test
    public void getUserByMACTest() throws SQLException {
        assertEquals(bestUser.getId(),db.getUserByMAC(bestUser.getMac()).getId());
    }
    @Test
    public void emptyUserTest() throws SQLException {
        db.emptyUserTable();
        assertEquals(0, db.getNumberOfAllUsers());
    }
    @Test
    public void emptyScoreTest() throws SQLException {
        db.emptyScoreTable();
        assertEquals(0, db.getNumberOfAllScores());
    }
    @Test
    public void getLeaderboardAscendingTest(){
        try {
            List<PlayerDTO> playerDTOList=db.getLeaderboardAscending();
            Assert.assertEquals(5, playerDTOList.size());
            Assert.assertNotEquals(playerDTOList.get(0).getScore(), playerDTOList.get(4).getScore());
        }catch (Exception e){
            Assert.fail();
        }

    }
}
