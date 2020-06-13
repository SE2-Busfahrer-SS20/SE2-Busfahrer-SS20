package at.aau.common.shared.networking.dto;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import shared.networking.dto.BaseMessage;
import shared.networking.dto.RegisterMessage;

public class RegisterMessageDToTest {
    private RegisterMessage registerMessage;

    @Before
    public void setup() {
        this.registerMessage = new RegisterMessage();
    }

    @Test
    public void testInheritance() {
        Assert.assertTrue(registerMessage instanceof BaseMessage);
    }
    @Test
    public void getPlayerNameTest(){
        registerMessage = new RegisterMessage("Freddy","Test");
        Assert.assertEquals("Freddy",registerMessage.getPlayerName());
    }
    @Test
    public void getMACAdresseTest(){
        registerMessage = new RegisterMessage("Freddy","Test");
        Assert.assertEquals("Test",registerMessage.getMacAddress());
    }
}
