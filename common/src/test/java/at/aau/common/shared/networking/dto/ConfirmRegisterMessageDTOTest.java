package at.aau.common.shared.networking.dto;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import shared.model.Card;
import shared.model.Deck;
import shared.model.Player;
import shared.model.impl.CardImpl;
import shared.model.impl.DeckImpl;
import shared.model.impl.PlayerImpl;
import shared.networking.dto.BaseMessage;
import shared.networking.dto.ConfirmRegisterMessage;

public class ConfirmRegisterMessageDTOTest {
    private ConfirmRegisterMessage confirmRegisterMessage;
    private Deck deck;
    private Card[] cards;
    private Player player;

    @Before
    public void setup(){
        this.deck = new DeckImpl();
        this.cards = new CardImpl[4];
        for (int i = 0; i < 4; i++) {
            cards[i] = deck.drawCard();
        }
        this.confirmRegisterMessage = new ConfirmRegisterMessage(cards);
        this.player = new PlayerImpl("Hansi","TestAdresse",cards,null);
        player.setTempID(2);
    }
    @Test
    public void testInheritance() {
        confirmRegisterMessage = new ConfirmRegisterMessage();
        Assert.assertTrue(confirmRegisterMessage instanceof BaseMessage);
    }
    @Test
    public void getCardsTest(){
        Assert.assertEquals(cards,confirmRegisterMessage.getCards());
    }
    @Test
    public void setCardsTest(){
        confirmRegisterMessage.setCards(cards);
        Assert.assertEquals(cards,confirmRegisterMessage.getCards());
    }
    @Test
    public void newDTOwithParameterPlayerCardTest(){
        confirmRegisterMessage = new ConfirmRegisterMessage(player);
        Assert.assertEquals(cards,confirmRegisterMessage.getCards());
    }
    @Test
    public void newDTOwithParameterPlayergetIDTest(){
        confirmRegisterMessage = new ConfirmRegisterMessage(player);
        Assert.assertEquals(2,confirmRegisterMessage.getID());
    }
    @Test
    public void newDTOwithParameterPlayersetIDTest(){
        confirmRegisterMessage = new ConfirmRegisterMessage(player);
        confirmRegisterMessage.setID(1);
        Assert.assertEquals(1,confirmRegisterMessage.getID());
    }
    @Test
    public void newDTOwithParameterPlayerMastergetIDTest(){
        confirmRegisterMessage = new ConfirmRegisterMessage(player,true);
        Assert.assertEquals(2,confirmRegisterMessage.getID());
    }
    @Test
    public void newDTOwithParameterPlayerMastersetIDTest(){
        confirmRegisterMessage = new ConfirmRegisterMessage(player,true);
        confirmRegisterMessage.setID(1);
        Assert.assertEquals(1,confirmRegisterMessage.getID());
    }
    @Test
    public void newDTOwithParameterPlayerMastersetMasterTest(){
        confirmRegisterMessage = new ConfirmRegisterMessage(player,true);
        confirmRegisterMessage.setMaster(false);
        Assert.assertEquals(false,confirmRegisterMessage.isMaster());
    }
    @Test
    public void newDTOwithParameterPlayerMastergetMasterTest(){
        confirmRegisterMessage = new ConfirmRegisterMessage(player,true);
        Assert.assertEquals(true,confirmRegisterMessage.isMaster());
    }
}
