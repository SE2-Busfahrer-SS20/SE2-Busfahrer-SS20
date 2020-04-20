package at.aau.busfahrer.model;

import java.util.List;

import at.aau.busfahrer.model.impl.CardImpl;

public interface Deck {
    CardImpl drawCard();
    void refill();
    List<String> printDeck();
}
