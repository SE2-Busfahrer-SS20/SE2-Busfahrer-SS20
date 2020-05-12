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
    @Before
    public void setup() {
        db= Database.getInstance();
    }

    @Test
    public void addUserTest() throws IOException, InterruptedException {
        User user = db.addUser("test-mac", "Username");
        //System.out.println("USER ADDED: ID= "+user.getId()+" MAC= "+user.getMac()+" NAME= "+user.getName() );
        //database.deleteUser(15);
        System.out.println("scores of user"+db.getAllScores(19).toString());
    }
    @Test
    public void delUserTest() throws IOException, InterruptedException {
    }
    @Test
    public void addScoreTest() throws IOException, InterruptedException {
        db.addScore(12,123);
    }    @Test
    public void getAllScoresTest() throws IOException, InterruptedException {
    }    @Test
    public void getAllUsersTest() throws IOException, InterruptedException {
    }    @Test
    public void getBestUserTest() throws IOException, InterruptedException {
    }    @Test
    public void getUserByIDTest() throws IOException, InterruptedException {
    }    @Test
    public void getUserByMACTest() throws IOException, InterruptedException {
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
