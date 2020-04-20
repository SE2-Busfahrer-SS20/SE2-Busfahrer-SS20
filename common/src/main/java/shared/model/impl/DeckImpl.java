package shared.model.impl;

import java.util.ArrayList;
import java.util.Collections;

import shared.model.Deck;
import shared.model.impl.CardImpl;

public class DeckImpl implements Deck {
    private static final int NUM_CARDS_IN_DECK = 52;    //4 (Farben) * 13(Anzahl Karten pro Farbe) = 52 Karten gesamt

    private ArrayList<CardImpl> cards = new ArrayList<>( ); //ArrayList, weil es viel dynamischer zu handhaben ist, als ein Array

    public DeckImpl( ) {
        //Erstelle des Kartendecks
        for( int i = 0; i < NUM_CARDS_IN_DECK; i++ )
        {
            cards.add( new CardImpl( i ) );
        }
        Collections.shuffle( cards );   //Mischen der Karten, ordnet alle Elemente der Liste zufällig an
    }

    public CardImpl drawCard( ) {
        //Ausgeben und entfernen der ersten Karte aus dem Deck
        return cards.remove( 0 );
    }

    public boolean isEmpty( ) {
        if (cards.size()<5)
            return true;
        else
            return false;
    }

    public void refill(){
        //Neufüllung des Decks,wenn leer.
        int size=cards.size();
        for( int i = 0; i < NUM_CARDS_IN_DECK; i++ )
        {
            CardImpl c= new CardImpl(i);
            boolean exist=false;
            //Überprüfung ob Karten bereits im Deck sind.
            for(int j=0;j<size;j++) {
                if(cards.get(j).equals(c))
                    exist=true;
            }
            if(!exist)
                cards.add(c);
        }
        Collections.shuffle( cards );
    }

    public void printDeck() {
        for(int i=0;i<NUM_CARDS_IN_DECK;i++){
            System.out.println("#"+i+" Suit: "+cards.get(i).getSuit()+"\tRank: "+cards.get(i).getRank()+"\t\t"+cards.get(i).toString());
        }
    }
}
