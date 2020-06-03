package at.aau.busfahrer.service.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

import at.aau.busfahrer.service.GamePlayService;
import shared.model.Card;
import shared.model.Deck;
import shared.model.impl.CardImpl;
import shared.model.impl.DeckImpl;

public class GamePlayServiceImplTest {

    private GamePlayService GamePlayService;
    private ArrayList<Card> allBlackCards;
    private ArrayList<Card> allRedCards;


    @Before
    public void initGamePlayServiceTest(){
        GamePlayService=GamePlayServiceImpl.getInstance();
        allBlackCards=new ArrayList<>();
        allRedCards=new ArrayList<>();

        for(int i=0; i<13 ;i++){
            allBlackCards.add(new CardImpl(0,i));//Spade
            allBlackCards.add(new CardImpl(3,i));//Club

            allRedCards.add(new CardImpl(1,i));//Heart
            allRedCards.add(new CardImpl(2,i));//Diamond
        }
    }

    //Test GuessColor
    @Test
    public void GuessColor_BlackTrue(){
        for(int i=0; i<allRedCards.size();i++){
            boolean answer=GamePlayService.guessColor(allBlackCards.get(i),true);
            Assert.assertEquals(answer, true);
        }
    }

    @Test
    public void GuessColor_BlackFalse(){
        for(int i=0; i<allRedCards.size();i++){
            boolean answer=GamePlayService.guessColor(allBlackCards.get(i),false);
            Assert.assertEquals(answer, false);
        }
    }

    @Test
    public void GuessColor_RedTrue(){
        for(int i=0; i<allRedCards.size();i++){
            boolean answer=GamePlayService.guessColor(allRedCards.get(i),false);
            Assert.assertEquals(answer, true);
        }
    }

    @Test
    public void GuessColor_RedFalse(){
        for(int i=0; i<allRedCards.size();i++){
            boolean answer=GamePlayService.guessColor(allRedCards.get(i),true);
            Assert.assertEquals(answer, false);
        }
    }

}
