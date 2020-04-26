package shared.model;

import shared.model.impl.CardImpl;
import java.util.List;


public interface Deck {
    CardImpl drawCard();
    void refill();
    int size();
    List<String> printDeck();
}
