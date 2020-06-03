package shared.model.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import shared.model.Deck;

public class DeckImpl implements Deck {
    private static final int NUM_CARDS_IN_DECK = 52;    //4 (colour) * 13(Number of cards per color) = 52 cards total

    private List<CardImpl> cards = new ArrayList<>( ); //ArrayList because it is much more dynamic to handle than an array

    public DeckImpl( ) {
        //Erstelle des Kartendecks
        for( int i = 0; i < NUM_CARDS_IN_DECK; i++ )
        {
            cards.add( new CardImpl( i ) );
        }
        Collections.shuffle( cards );   //Shuffling the cards, randomly arranges all elements of the list
    }

    public CardImpl drawCard( ) {
        //Issue and remove the first card from the deck
        return cards.remove( 0 );
    }

    public boolean isEmpty( ) {
        return cards.isEmpty();
    }

    public void refill(){
        //Refill the deck when empty.
        int size=cards.size();
        for( int i = 0; i < NUM_CARDS_IN_DECK; i++ )
        {
            CardImpl c= new CardImpl(i);
            boolean exist=false;
            //Check whether cards are already in the deck.
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
    //outputs a List with the hole cards.
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
