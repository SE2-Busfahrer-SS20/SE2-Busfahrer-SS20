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

    @Before
    public void initGamePlayServiceTest(){
        GamePlayService=GamePlayServiceImpl.getInstance();
        Spade=new ArrayList<>();
        Heart=new ArrayList<>();
        Diamond=new ArrayList<>();
        Club=new ArrayList<>();

        for(int i=0; i<13 ;i++){
            Spade.add(new CardImpl  (0,i));//Spade
            Heart.add(new CardImpl  (1,i));//Heart
            Diamond.add(new CardImpl(2,i));//Diamond
            Club.add(new CardImpl   (3,i));//Club
        }
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
    public void guesHigherLower1(){ //Tests if ace is allways seen as highest card

        Card[] aces=new Card[4];
        for(int i=0;i<4;i++){
            aces[i]=new CardImpl(i,0);
        }

        for(int i=0;i<4;i++) {   //loop all suits
            for (int j = 1; j < 13; j++) { //loop all ranks

                Card card = new CardImpl(i, j);
                for (int k = 0; k < 4; k++) {   //loop all suits for reference card
                    for (int l = j+1; l < 13; l++) {  //loop all higher ranks for reference card
                        Card reference = new CardImpl(k, l);
                        Assert.assertFalse(GamePlayService.guessHigherLower(card, reference, true));
                    }

                }
            }
        }

        for(int i=0;i<4;i++) {
            //Ace higher than all other cards
            for (int j = 0; j < Spade.size(); j++) {
                //test if return true when guessing right
                Assert.assertTrue(GamePlayService.guessHigherLower(aces[i], Spade.get(j),   true));
                Assert.assertTrue(GamePlayService.guessHigherLower(aces[i], Heart.get(j),   true));
                Assert.assertTrue(GamePlayService.guessHigherLower(aces[i], Diamond.get(j), true));
                Assert.assertTrue(GamePlayService.guessHigherLower(aces[i], Club.get(j),    true));

                //test if return false when guessing wrong

                Assert.assertFalse(GamePlayService.guessHigherLower(aces[i], Spade.get(j),   false));
                Assert.assertFalse(GamePlayService.guessHigherLower(aces[i], Heart.get(j),   false));
                Assert.assertFalse(GamePlayService.guessHigherLower(aces[i], Diamond.get(j), false));
                Assert.assertFalse(GamePlayService.guessHigherLower(aces[i], Club.get(j),    false));
            }

            //All other Cards lower than ace
            for (int j = 1; j < Spade.size(); j++) {//loop all cards but aces
                //test if return true when guessing right
                Assert.assertTrue(GamePlayService.guessHigherLower(Spade.get(j),    aces[i], false));
                Assert.assertTrue(GamePlayService.guessHigherLower(Heart.get(j),    aces[i], false));
                Assert.assertTrue(GamePlayService.guessHigherLower(Diamond.get(j),  aces[i], false));
                Assert.assertTrue(GamePlayService.guessHigherLower(Club.get(j),     aces[i], false));

                //test if return false when guessing wrong
                Assert.assertFalse(GamePlayService.guessHigherLower(Spade.get(j),    aces[i], false));
                Assert.assertFalse(GamePlayService.guessHigherLower(Heart.get(j),    aces[i], false));
                Assert.assertFalse(GamePlayService.guessHigherLower(Diamond.get(j),  aces[i], false));
                Assert.assertFalse(GamePlayService.guessHigherLower(Club.get(j),     aces[i], false));
            }
        }
    }

}
