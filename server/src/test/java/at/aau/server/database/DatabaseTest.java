package at.aau.server.database;

import at.aau.server.database.Table.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;

public class DatabaseTest {

    private Database db;
    private User bestUser;

    @Before
    public void setup() {
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
    public void addUserTest() throws IOException, InterruptedException {
        User user = db.addUser("test-mac", "Username");
        assertEquals(6, db.getNumberOfAllUsers());
        User user2 = db.addUser("test-mac2", "Username2");
        assertEquals(7, db.getNumberOfAllUsers());
    }
    @Test
    public void delUserTest() throws IOException, InterruptedException {
        db.deleteUser(db.getUserByMAC("34g654ght").getId());
        assertEquals(4, db.getNumberOfAllUsers());
    }
    @Test
    public void addScoreTest() throws IOException, InterruptedException {
        db.addScore(12,123);
        assertEquals(6, db.getNumberOfAllScores());
    }
    @Test
    public void getAllScoresTest() throws IOException, InterruptedException {
        assertEquals(2,db.getAllScores(db.getUserByMAC("34g654ght").getId()).size());
    }
    @Test
    public void getAllUsersTest() throws IOException, InterruptedException {
        assertEquals(5,db.getAllUsers().size());
    }
    @Test
    public void getBestUserTest() throws IOException, InterruptedException {
        assertEquals(bestUser.getId(),db.getBestUser().getId());
    }
    @Test
    public void getUserByIDTest() throws IOException, InterruptedException {
        assertEquals(bestUser.getId(),db.getUserByID(bestUser.getId()).getId());
    }
    @Test
    public void getUserByMACTest() throws IOException, InterruptedException {
        assertEquals(bestUser.getId(),db.getUserByMAC(bestUser.getMac()).getId());
    }
    @Test
    public void emptyUserTest() throws IOException, InterruptedException {
        db.emptyUserTable();
        assertEquals(db.getNumberOfAllUsers(), 0);
    }
    public void emptyScoreTest() throws IOException, InterruptedException {
        db.emptyScoreTable();
        assertEquals(db.getNumberOfAllScores(), 0);
    }
}
