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

    private ArrayList<Card> Spade;  //Black
    private ArrayList<Card> Heart;  //Red
    private ArrayList<Card> Diamond;//Red
    private ArrayList<Card> Club;   //Black

    private ArrayList<shared.model.Card>[] allCards;

    @Before
    public void initGamePlayServiceTest(){
        GamePlayService=GamePlayServiceImpl.getInstance();
        Spade=new ArrayList<>();
        Heart=new ArrayList<>();
        Diamond=new ArrayList<>();
        Club=new ArrayList<>();
        allCards=new ArrayList[4];

        for(int i=0; i<13 ;i++){
            Spade.add(new CardImpl  (0,i));//Spade
            Heart.add(new CardImpl  (1,i));//Heart
            Diamond.add(new CardImpl(2,i));//Diamond
            Club.add(new CardImpl   (3,i));//Club
        }
        allCards[0]=Spade;
        allCards[1]=Heart;
        allCards[2]=Diamond;
        allCards[3]=Club;
    }

    //Test GuessColor
    @Test
    public void GuessColor_BlackTrue(){
        for(int i=0; i<Spade.size();i++){
            Assert.assertTrue(GamePlayService.guessColor(Spade.get(i),true));
        }
        for(int i=0; i<Club.size();i++){
            Assert.assertTrue(GamePlayService.guessColor(Club.get(i),true));
        }
    }

    @Test
    public void GuessColor_BlackFalse(){
        for(int i=0; i<Spade.size();i++){
            Assert.assertFalse(GamePlayService.guessColor(Spade.get(i),false));
        }
        for(int i=0; i<Club.size();i++){
            Assert.assertFalse(GamePlayService.guessColor(Club.get(i),false));
        }
    }

    @Test
    public void GuessColor_RedTrue(){
        for(int i=0; i<Heart.size();i++){
            Assert.assertTrue(GamePlayService.guessColor(Heart.get(i),false));
        }
        for(int i=0; i<Diamond.size();i++){
            Assert.assertTrue(GamePlayService.guessColor(Diamond.get(i),false));
        }
    }

    @Test
    public void GuessColor_RedFalse(){
        for(int i=0; i<Heart.size();i++){
            Assert.assertFalse(GamePlayService.guessColor(Heart.get(i),true));
        }
        for(int i=0; i<Diamond.size();i++){
            Assert.assertFalse(GamePlayService.guessColor(Diamond.get(i),true));
        }
    }

    //Test GuessHigherLower
    @Test
    public void guesHigherLower_TestAces(){ //Tests if ace is allways seen as highest card

        Card[] aces=new Card[4];
        for(int i=0;i<4;i++){
            aces[i]=new CardImpl(i,0);
        }

        for(int i=0;i<4;i++) {
            //Ace higher than all other cards
            for (int j = 0; j < Spade.size(); j++) {
                //test if return true when guessing right
                for (ArrayList<Card> allCard : allCards) {
                    Assert.assertTrue(GamePlayService.guessHigherLower(aces[i], allCard.get(j), true));
                }
                //test if return false when guessing wrong
                if(j!=0) {//Because same cards allways return true
                    for (ArrayList<Card> allCard : allCards) {
                        Assert.assertFalse(GamePlayService.guessHigherLower(aces[i], allCard.get(j), false));
                    }
                }
            }

            //All other Cards lower than ace
            for (int j = 1; j < Spade.size(); j++) {//loop all cards but aces
                //test if return true when guessing right
                for (ArrayList<Card> allCard : allCards) {
                    Assert.assertTrue(GamePlayService.guessHigherLower(allCard.get(j), aces[i], false));
                }

                //test if return false when guessing wrong
                for (ArrayList<Card> allCard : allCards) {
                    Assert.assertFalse(GamePlayService.guessHigherLower(allCard.get(j), aces[i], true));
                }
            }
        }
    }

    @Test
    public void guessHigherLower_TestAllCards(){
        for(int i=0;i<4;i++) {   //loop all suits
            for (int j = 1; j < 13; j++) { //loop all ranks

                Card lower = new CardImpl(i, j);
                for (int k = 0; k < 4; k++) {   //loop all suits for reference lower
                    for (int l = j+1; l < 13; l++) {  //loop all higher ranks for reference-card
                        Card higher = new CardImpl(k, l);

                        Assert.assertTrue(GamePlayService.guessHigherLower(higher, lower, true)); //TestCase: card is higher, guessed correct
                        Assert.assertFalse(GamePlayService.guessHigherLower(higher, lower, false)); //TestCase: card is higher, guessed wrong

                        Assert.assertTrue(GamePlayService.guessHigherLower(lower, higher, false)); //TestCase: card is lower, guessed correct
                        Assert.assertFalse(GamePlayService.guessHigherLower(lower, higher, true)); //TestCase: card is lower, guessed wrong
                    }
                }
            }
        }
    }
}
