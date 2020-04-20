package shared.model.impl;

import shared.model.Card;

public class CardImpl implements Card {
    private final int suit; //entspricht der Farbe: Herz, Pik, Karo, Kreuz
    private final int rank; //entspricht dem Rang: 1(Ass)-10, Bube, Dame, König

    public CardImpl ( int val ) {
        suit = val / 13;    //gibt einen Wert von 0 bis 3 aus: z.B. 0=Pik, 1=Herz usw.
        rank = val % 13;    //gibt einen Wert von 0 bis 12 aus: z.B. 0=Ass, 10=Bub, 3= 4te Karte
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
        int codePoint = 127137 + suit * 16 + rank;  //Berechnung der Karten mittels ASCII Code

        if( rank > 10 )//Es gibt einige Karte die nicht für unser Spiel gebraucht werde, diese werden so übersprungen.
        {
            codePoint++;
        }

        return new String( Character.toChars( codePoint ) );
    }
}
