package at.aau.busfahrer.service.impl;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import at.aau.busfahrer.service.BushmenService;
import at.aau.busfahrer.service.GamePlayService;
import shared.model.Card;
import shared.networking.NetworkClient;
import shared.networking.dto.BushmenCardMessage;
import shared.networking.dto.BushmenMessage;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

public class BushmenServiceImpl implements BushmenService {

    private NetworkClient networkClient;
    private List<Card> cards;
    private GamePlayService gamePlayService;
    private int kartenCounter;
    private int punkteAnzahlBusfahrer;
    private boolean isLooser; // is true in case that the player is a looser.

    public BushmenServiceImpl(NetworkClient networkClient) {
        this.networkClient = networkClient;
        kartenCounter = 0;
        punkteAnzahlBusfahrer = 10;
        isLooser = true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)

    @Override
    public void setUpCardTurnCallback(BiConsumer<Integer, Card> callback) {
        // Callback der aufgerufen wird wenn Client die Karten erhalten hat
        networkClient.registerCallback(BushmenMessage.class, msg -> {
            BushmenMessage bushmenMessage = (BushmenMessage) msg;
            this.cards = Arrays.asList(bushmenMessage.getCards());
            Log.i("Bushmen", "BushmenCards" + bushmenMessage.getCards().length);
        });

        // Callback nach jedem mal Karten umdrehen
        networkClient.registerCallback(BushmenCardMessage.class, msg -> {
            BushmenCardMessage bushmenCardMessage = (BushmenCardMessage) msg;
            Log.i("Bushmen", "BushmenCards recieved" + bushmenCardMessage.getCardId());
            callback.accept(bushmenCardMessage.getCardId(), bushmenCardMessage.getCard());
        });
    }

    @Override
    public List<Card> getCards() {
        return cards;
    }

    @Override
    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    @Override
    public void startBushmanRound() {
        this.networkClient.sendMessage(new BushmenMessage());
    }

    @Override
    public void turnCard(int cardId) {
        this.networkClient.sendMessage(new BushmenCardMessage(cardId, cards.get(cardId)));
    }

    @Override
    public void setLooser(boolean isLooser) {
        this.isLooser = isLooser;
    }

    @Override
    public boolean isLooser() {
        return isLooser;
    }

    @Override
    public void addPunkteAnzahlBusfahrer(int punkte) {
        punkteAnzahlBusfahrer += punkte;
    }

    @Override
    public int getPunkteAnzahlBusfahrer() {
        return punkteAnzahlBusfahrer;
    }

    @Override
    public void resetPunkteAnzahlBusfahrer() {
        // Kommt man zum Busfahrer startet man mit 10 Punkten
        punkteAnzahlBusfahrer = 10;
    }

    @Override
    public void incrementKartenCounter() {
        kartenCounter++;
    }

    @Override
    public void resetKartenCounter() {
        kartenCounter = 0;
    }

    @Override
    public int getKartenCounter() {
        return kartenCounter;
    }

    @Override
    public boolean hasWon() {
        return kartenCounter == 4;
    }

}
