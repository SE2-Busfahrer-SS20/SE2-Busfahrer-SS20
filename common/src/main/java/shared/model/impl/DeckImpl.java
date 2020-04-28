package shared.model.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import shared.model.Deck;
import shared.model.impl.CardImpl;

public class DeckImpl implements Deck {
    private static final int NUM_CARDS_IN_DECK = 52;    //4 (Farben) * 13(Anzahl Karten pro Farbe) = 52 Karten gesamt

    private List<CardImpl> cards = new ArrayList<>( ); //ArrayList, weil es viel dynamischer zu handhaben ist, als ein Array

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
        return cards.size()==0;
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
                CardImpl a = cards.get(j);
                String card1 = a.toString();
                String card2 = c.toString();
                if(card1.equals(card2))
                    exist=true;
            }
            if(!exist)
                cards.add(c);
        }
        Collections.shuffle( cards );
    }

    public List<String> printDeck() {
        List<String> strings = new ArrayList<>();

        for(int i=0;i<NUM_CARDS_IN_DECK;i++){
            strings.add(cards.get(i).toString());
        }
        return strings;
    }
    public List<CardImpl> getCards(){
        return cards;
    }
    public int size() {
        return cards.size();
    }
}
