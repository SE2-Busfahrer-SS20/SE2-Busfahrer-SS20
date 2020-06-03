package at.aau.busfahrer.service;

import shared.model.Card;

import java.util.List;
import java.util.function.BiConsumer;

public interface BushmenService {

    void setUpCardTurnCallback(BiConsumer<Integer, Card> callback);
    void startBushmanRound();
    void turnCard(int cardId);

    List<Card> getCards();
    void setCards(List<Card> cards);

    void setLooser(boolean isLooser);
    boolean isLooser();

    void addPunkteAnzahlBusfahrer(int punkte);
    int getPunkteAnzahlBusfahrer();
    void resetPunkteAnzahlBusfahrer();

    void incrementKartenCounter();
    void resetKartenCounter();
    int getKartenCounter();

    boolean hasWon();
}
