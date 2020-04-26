package at.aau.busfahrer;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import at.aau.busfahrer.model.impl.CardImpl;


public class CardImplTest {
    private int codePoint;
    private  int rank;
    private int suit;
    @Before
    public void init(){

    }
    @Test
    public void ConstructorValueTestRank(){
        CardImpl c = new CardImpl(13);
        Assert.assertEquals(0,c.getRank());
    }
    @Test
    public void ConstructorValueTestSuit(){
        CardImpl c = new CardImpl(13);
        Assert.assertEquals(1,c.getSuit());
    }
    @Test
    public void ConstructorRankSuitTestRank(){
        for(int i = 0;i<13;i++) {
            CardImpl c = new CardImpl(10, i);
            Assert.assertEquals(i, c.getRank());
        }
    }
    @Test
    public void ConstructorRankSuitTestSuit(){
        for(int i = 0;i<13;i++) {
            CardImpl c = new CardImpl(i, 10);
            Assert.assertEquals(i, c.getSuit());
        }
    }
    @Test
    public void toStringTestRankSmaller11(){
        CardImpl c = new CardImpl(10,10);
        codePoint= 127137 + 10 * 16 + 10;
        String test = new String(Character.toChars(codePoint));
        Assert.assertEquals(test,c.toString());
    }
    @Test
    public void toStringTestRankGreater10(){
        for(int i = 0;i<52;i++){
            CardImpl c = new CardImpl(i);
            rank = i%13;
            suit = i/13;
            codePoint= 127137 + suit * 16 + rank;
            if(rank>10){
                codePoint++;
            }
            String test = new String(Character.toChars(codePoint));
            Assert.assertEquals(test,c.toString());

        }

    }
}
