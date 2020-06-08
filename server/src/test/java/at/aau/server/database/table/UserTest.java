package at.aau.server.database.table;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UserTest {
    private User user;

    @Before
    public void setup(){
        user= new User(3, "85:de:36:d6:5a:d2", "Max Mustermann");
    }

    @Test
    public void getIdTest() {
        Assert.assertEquals(3, user.getId());
    }
    @Test
    public void setIdTest() {
        Assert.assertEquals(3, user.getId());
        user.setId(5);
        Assert.assertEquals(5, user.getId());
    }
    @Test
    public void getMacTest() {
        Assert.assertEquals("85:de:36:d6:5a:d2", user.getMac());
    }
    @Test
    public void setMacTest() {
        Assert.assertEquals("85:de:36:d6:5a:d2", user.getMac());
        user.setMac("ab:de:36:d6:5a:e4");
        Assert.assertEquals("ab:de:36:d6:5a:e4", user.getMac());
    }
    @Test
    public void getNameTest() {
        Assert.assertEquals("Max Mustermann", user.getName());
    }
    @Test
    public void setNameTest() {
        Assert.assertEquals("Max Mustermann", user.getName());
        user.setName("Vorname Nachname");
        Assert.assertEquals("Vorname Nachname", user.getName());
    }
}
