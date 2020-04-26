package shared.model;

import shared.model.impl.CardImpl;

public interface Deck {
    CardImpl drawCard();
    void refill();
    void printDeck();
    int size();
}
