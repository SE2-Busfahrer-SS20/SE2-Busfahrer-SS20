package at.aau.common.shared.networking.dto;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import shared.model.PlayerDTO;
import shared.model.impl.PlayerDTOImpl;
import shared.networking.dto.BaseMessage;
import shared.networking.dto.NewPlayerMessage;

public class NewPlayerMessageDToTest {
    private NewPlayerMessage newPlayerMessage;
    private PlayerDTO playerDTO;
    private PlayerDTO playerTest;

    @Before
    public void setup(){
        this.newPlayerMessage = new NewPlayerMessage();
        this.playerDTO = new PlayerDTOImpl("Larissa",30,false);
        this.playerTest = new PlayerDTOImpl("Lars",12,true);
    }

    @Test
    public void testInheritance() {
        Assert.assertTrue(newPlayerMessage instanceof BaseMessage);
    }
    @Test
    public void constrcutorTest(){

        newPlayerMessage = new NewPlayerMessage(playerDTO);
        Assert.assertEquals(playerDTO,newPlayerMessage.getPlayer());
        Assert.assertTrue(newPlayerMessage.getPlayer() instanceof  PlayerDTOImpl);
    }
    @Test
    public void gettersetterTest(){
        newPlayerMessage = new NewPlayerMessage(playerDTO);
        newPlayerMessage.setPlayer(playerTest);
        Assert.assertEquals(playerTest,newPlayerMessage.getPlayer());
        Assert.assertTrue(newPlayerMessage.getPlayer() instanceof  PlayerDTOImpl);
    }
}
