package at.aau.busfahrer.model;

import java.util.ArrayList;

import at.aau.busfahrer.model.impl.CardImpl;

public interface Deck {
    CardImpl drawCard();
    void refill();
    ArrayList printDeck();
}
