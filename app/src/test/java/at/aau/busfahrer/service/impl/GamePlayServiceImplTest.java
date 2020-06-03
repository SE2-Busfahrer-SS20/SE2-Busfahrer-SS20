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

    private ArrayList[] allCards;

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

    //Test GuessBetweenOutside
    @Test
    public void guessBetweenOutside_Outside_TestAces() {
        for (int i = 0; i < 4; i++) {   //loop all suits
            Card Ace = (Card) allCards[i].get(0);
            for (int j = 0; j < 13; j++) {
                Card ref1 = new CardImpl(i, j);
                for (int k = 0; k < 4; k++) {   //loop all suits for reference two
                    for (int l = j + 1; l < 13; l++) {
                        Card ref2 = new CardImpl(k, l);
                        //Aces can only be between two cards when one of the reference cards also is an ace
                        if (ref1.getRank() == 0 || ref2.getRank() == 0) {
                            Assert.assertTrue(GamePlayService.guessBetweenOutside(Ace, ref1, ref2, true)); //Test: one reference is ace and guessed between
                            Assert.assertTrue(GamePlayService.guessBetweenOutside(Ace, ref1, ref2, false)); //Test: one reference is ace and guessed outside
                        } else {
                            Assert.assertTrue(GamePlayService.guessBetweenOutside(Ace, ref1, ref2, false)); //Test: no reference is ace and guessed correct
                        }
                    }
                }

            }
        }
    }

    @Test
    public void guessBetweenOutside_Outside_TestAllCards(){
            for (int i = 0; i < 4; i++) {   //loop all suits
                Card Ace = (Card) allCards[i].get(0);
                Card Two = (Card) allCards[i].get(1);
                for (int j = 1; j < 13; j++) { //loop all ranks except of ace (because it is index 0 and the highest rank)

                    Card card = new CardImpl(i, j); //tests are executed for each possible card

                    for (int k = 0; k < 4; k++) {   //loop all suits for reference lower
                        //refTwo is Ace
                        for (int l = j + 1; l < 13; l++) {
                            Card higher = new CardImpl(k, l);//is allways higher than card

                            Assert.assertTrue(GamePlayService.guessBetweenOutside(card, higher, Ace, false)); //Test: card is lower than both references, guessed correct
                            Assert.assertFalse(GamePlayService.guessBetweenOutside(card, higher, Ace, true)); //Test: card is lower than both references, guessed wrong
                            Assert.assertTrue(GamePlayService.guessBetweenOutside(higher, card, Ace, true)); //Test: card is higher than one references and lower than the other, guessed correct
                            Assert.assertFalse(GamePlayService.guessBetweenOutside(higher, card, Ace, false)); //Test: card is higher than one references and lower than the other, guessed wrong
                            Assert.assertTrue(GamePlayService.guessBetweenOutside(higher, card, Two, false)); //Test: card is higher than both references, guessed correct
                            Assert.assertFalse(GamePlayService.guessBetweenOutside(higher, card, Two, true)); //Test: card is higher than both references, guessed wrong

                            //Repeat all tests from above with switched references
                            Assert.assertTrue(GamePlayService.guessBetweenOutside(card, Ace, higher, false)); //Test: card is lower than both references, guessed correct
                            Assert.assertFalse(GamePlayService.guessBetweenOutside(card, Ace, higher, true)); //Test: card is lower than both references, guessed wrong
                            Assert.assertTrue(GamePlayService.guessBetweenOutside(higher, Ace, card, true)); //Test: card is higher than one references and lower than the other, guessed correct
                            Assert.assertFalse(GamePlayService.guessBetweenOutside(higher, Ace, card, false)); //Test: card is higher than one references and lower than the other, guessed wrong
                            Assert.assertTrue(GamePlayService.guessBetweenOutside(higher, Two, card, false)); //Test: card is higher than both references, guessed correct
                            Assert.assertFalse(GamePlayService.guessBetweenOutside(higher, Two, card, true)); //Test: card is higher than both references, guessed wrong
                        }
                    }
                }
            }
    }

    //Test GuessSuit
    @Test
    public void guessSuit_allCards() {
        for (int i = 0; i < 4; i++) {   //loop all suits
            for (int j = 1; j < 13; j++) { //loop all ranks

                Card card=new CardImpl(i,j);
                for(int suit=0;suit<4;suit++){
                    if(card.getSuit()==suit){
                        Assert.assertTrue(GamePlayService.guessSuit(card,suit)); //Testcase: cards suit equals guessed suit
                    }else{
                        Assert.assertFalse(GamePlayService.guessSuit(card,suit)); //Testcase: cards suit does not equal guessed suit
                    }
                }
            }
        }
    }
}
