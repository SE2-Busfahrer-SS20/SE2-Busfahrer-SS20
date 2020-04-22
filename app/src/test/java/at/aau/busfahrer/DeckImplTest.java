package at.aau.busfahrer;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import at.aau.busfahrer.model.impl.CardImpl;
import at.aau.busfahrer.model.impl.DeckImpl;
import at.aau.busfahrer.model.impl.DeckImpl.*;

import static org.junit.Assert.*;


public class DeckImplTest {
    private List<CardImpl> cards = new ArrayList<>();
    private DeckImpl deck;

    @Before
    public void init(){
        deck = new DeckImpl();
        cards = deck.getCards();
    }
    @After
    public void setDown(){
        cards= null;
    }
    @Test
    public void DeckImplConstructorTest(){
        Assert.assertEquals(52,cards.size() );
    }
    @Test
    public void drawCardTest(){
        deck.drawCard();
        Assert.assertEquals(51,cards.size());
    }
    @Test
    public void isEmptyTestFalse(){
        Assert.assertEquals(false,deck.isEmpty());
    }
    @Test
    public void isEmptyTestTrue(){
        for(int i =0;i<52;i++){
            deck.drawCard();
        }
        Assert.assertEquals(true,deck.isEmpty());
    }
    @Test
    public void refillTest(){
        for(int i = 0;i<40;i++){
            deck.drawCard();
        }
        deck.refill();
        Assert.assertEquals(52,cards.size());
    }
    @Test
    public void printDeckTest(){
        List<String> strings = deck.printDeck();
        Assert.assertEquals(52,strings.size());

    }
}
