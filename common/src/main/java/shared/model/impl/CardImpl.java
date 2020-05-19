package shared.model.impl;

import shared.model.Card;

public class CardImpl implements Card {
    private final int suit; //corresponds to the color: heart, spades, diamonds, clubs
    private final int rank; //corresponds to the rank: 1 (ace) -10, jack, queen, king

    public CardImpl ( int val ) {
        suit = val / 13;    //outputs a value from 0 to 3: e.g. 0 = spades, 1 = hearts etc.
        rank = val % 13;    //outputs a value from 0 to 12: e.g. 0 = ace, 10 = boy, 3 = 4th card
    }

    public CardImpl( int st, int rnk ) {
        suit = st;
        rank = rnk;
    }


    public CardImpl() { //just for test purpose to solve error: " Caused by: com.esotericsoftware.kryo.KryoException: Class cannot be created (missing no-arg constructor): shared.model.impl.CardImpl"
        suit = 1;   //Herz
        rank = 1;   //Ass
    }
    public int getSuit( )
    {
        return suit;
    }

    public int getRank( )
    {
        return rank;
    }

    public String toString( )
    {
        int codePoint = 127137 + suit * 16 + rank;  //Calculation of the cards using ASCII code

        if( rank > 10 )//The card Knight (German: Bube) is one card that is not used for our game, so it is skipped.
        {
            codePoint++;
        }

        return new String( Character.toChars( codePoint ) );
    }
}
