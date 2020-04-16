package at.aau.busfahrer.model;

import at.aau.busfahrer.model.impl.CardImpl;

public interface Deck {
    CardImpl drawCard();
    void refill();
    void printDeck();
}
