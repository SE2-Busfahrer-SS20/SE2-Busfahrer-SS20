package shared.model;

import java.util.List;
import shared.model.impl.CardImpl;
import java.util.List;


public interface Deck {
    CardImpl drawCard();
    void refill();
    List<String> printDeck();
    int size();
}
