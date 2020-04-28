package shared.model;

import java.util.List;
import shared.model.impl.CardImpl;

public interface Deck {
    CardImpl drawCard();
    void refill();
    List<String> printDeck();
}
