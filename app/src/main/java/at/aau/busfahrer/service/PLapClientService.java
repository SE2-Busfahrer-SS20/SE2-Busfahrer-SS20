package at.aau.busfahrer.service;

import android.widget.TextView;

import java.util.List;

import shared.model.Card;
import shared.networking.Callback;

public interface PLapClientService {

    Card checkCardMatch(String cardString, Card[] cards, int row);
    int getMatchCount();
    void registerCardCallback(Callback<Card[]> callback);
    void registerFinishedLabCallback(Callback<Boolean> callback);
    Card[] getPlayerCards();
    void dealPoints(String playerName);
    List<String> getPlayerNames();
    void startLab();
    Card[] getPCards();
}
