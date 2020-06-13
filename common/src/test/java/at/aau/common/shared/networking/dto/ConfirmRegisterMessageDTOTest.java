package at.aau.common.shared.networking.dto;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import shared.model.Card;
import shared.model.Deck;
import shared.model.impl.CardImpl;
import shared.model.impl.DeckImpl;
import shared.networking.dto.BaseMessage;
import shared.networking.dto.ConfirmRegisterMessage;

public class ConfirmRegisterMessageDTOTest {
    private ConfirmRegisterMessage confirmRegisterMessage;
    private Deck deck;
    private Card[] cards;



    @Before
    public void setup(){
        this.deck = new DeckImpl();
        this.cards = new CardImpl[4];
        for (int i = 0; i < 4; i++) {
            cards[i] = deck.drawCard();
        }
        this.confirmRegisterMessage = new ConfirmRegisterMessage(cards);
    }
    @Test
    public void testInheritance() {
        Assert.assertTrue(confirmRegisterMessage instanceof BaseMessage);
    }
}
