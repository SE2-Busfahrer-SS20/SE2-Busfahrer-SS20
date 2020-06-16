package at.aau.common.shared.networking.dto;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import shared.model.PlayerDTO;
import shared.model.impl.PlayerDTOImpl;
import shared.networking.dto.BaseMessage;
import shared.networking.dto.StartGameMessage;

public class StartGameMessageDTOTest {
    private StartGameMessage startGameMessage;
    private List<PlayerDTO> playerList;

    @Before
    public void setup(){
        this.startGameMessage = new StartGameMessage();
        this.playerList = new ArrayList<>();

        playerList.add(new PlayerDTOImpl("Lars",12,false));
        playerList.add(new PlayerDTOImpl("Larissa",134,true));

    }
    @Test
    public void testInheritance() {
        Assert.assertTrue(startGameMessage instanceof BaseMessage);
    }
    @Test
    public void constructorTest(){
        startGameMessage = new StartGameMessage(playerList);
        Assert.assertEquals(playerList,startGameMessage.getPlayerList());
    }
    @Test
    public void setterTest(){
        startGameMessage = new StartGameMessage();
        startGameMessage.setPlayerList(playerList);
        Assert.assertEquals(playerList,startGameMessage.getPlayerList());
    }
}
