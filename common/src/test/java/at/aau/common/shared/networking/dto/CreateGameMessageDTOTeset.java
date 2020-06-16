package at.aau.common.shared.networking.dto;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import shared.networking.dto.BaseMessage;
import shared.networking.dto.CreateGameMessage;

public class CreateGameMessageDTOTeset {
    private CreateGameMessage createGameMessage;

    @Before
    public void setup(){
        this.createGameMessage = new CreateGameMessage();
    }

    @Test
    public void testInheritance() {
        Assert.assertTrue(createGameMessage instanceof BaseMessage);
    }

    @Test
    public void constructorTestandGetterTest(){
        createGameMessage = new CreateGameMessage(4);
        Assert.assertEquals(4,createGameMessage.getPlayerCount());
    }
}
