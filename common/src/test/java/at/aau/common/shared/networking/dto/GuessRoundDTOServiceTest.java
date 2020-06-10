package at.aau.common.shared.networking.dto;

import shared.model.GameState;
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
    private List<PlayerDTO> playerList;

    @Before
    public void setup(){
        this.startGameMessage=new StartGameMessage();
        this.playedMessage=new PlayedMessage();
        this.updateMessage=new UpdateMessage();
        this.playerList = new ArrayList<PlayerDTO>();

        PlayerDTO player1 = new PlayerDTOImpl("one", 5, false);
        PlayerDTO player2 = new PlayerDTOImpl("two", 6, true);
        PlayerDTO player3 = new PlayerDTOImpl("three", 2, false);
        playerList.add(player1);
        playerList.add(player2);
        playerList.add(player3);
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

    @Test
    public void testPlayedMessage(){
        //Test Constructor and getter
        PlayedMessage pm2 = new PlayedMessage(GameState.LAP1A, 1, true);
        Assert.assertEquals(GameState.LAP1A,pm2.getLap());
        Assert.assertEquals(1,pm2.getTempID());
        Assert.assertTrue(pm2.scored());

        //Test Setter, Getter
        this.playedMessage.setLap(GameState.LAP2);
        this.playedMessage.setTempID(2);
        this.playedMessage.setScored(false);
        Assert.assertEquals(GameState.LAP2,playedMessage.getLap());
        Assert.assertEquals(2,playedMessage.getTempID());
        Assert.assertFalse(playedMessage.scored());
    }

    @Test
    public void testUpdateMessage(){
        //Test >Constructor and getter
        UpdateMessage um2 = new UpdateMessage(4,playerList);
        Assert.assertEquals(4, um2.getCurrentPlayer());
        Assert.assertEquals(playerList, um2.getPlayerList());

        //Test Setter, Getter
        this.updateMessage.setCurrentPlayer(6);
        this.updateMessage.setPlayerList(this.playerList);
        Assert.assertEquals(6,this.updateMessage.getCurrentPlayer());
        Assert.assertEquals(this.playerList,this.updateMessage.getPlayerList());
    }
}
