package at.aau.common.shared.networking.dto;

import shared.model.PlayerDTO;
import shared.model.impl.PlayerDTOImpl;
import shared.networking.dto.PlayedMessage;
import shared.networking.dto.StartGameMessage;
import shared.networking.dto.UpdateMessage;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import shared.networking.dto.BaseMessage;


public class GuessRoundDTOServiceTest {

        //Test:
        //StartGameMessage
        //PlayedMessage
        //UpdateMessage

    private StartGameMessage startGameMessage;
    private PlayedMessage playedMessage;
    private UpdateMessage updateMessage;

    @Before
    public void setup(){
        this.startGameMessage=new StartGameMessage();
        this.playedMessage=new PlayedMessage();
        this.updateMessage=new UpdateMessage();
    }

    @Test
    public void testInheritance(){
        Assert.assertTrue(startGameMessage instanceof BaseMessage);
        Assert.assertTrue(playedMessage instanceof BaseMessage);
        Assert.assertTrue(updateMessage instanceof BaseMessage);
    }

    @Test
    public void testStartGameMessage(){
        PlayerDTO player1 = new PlayerDTOImpl("one", 5, false);
        PlayerDTO player2 = new PlayerDTOImpl("two", 6, true);
        PlayerDTO player3 = new PlayerDTOImpl("three", 2, false);
        List<PlayerDTO> playerList = new ArrayList<PlayerDTO>();
        playerList.add(player1);
        playerList.add(player2);
        playerList.add(player3);

        this.startGameMessage.setPlayerList(playerList);
        Assert.assertEquals(playerList, this.startGameMessage.getPlayerList());
    }

}
