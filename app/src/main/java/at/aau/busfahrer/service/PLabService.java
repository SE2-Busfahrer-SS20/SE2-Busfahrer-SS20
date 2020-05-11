package at.aau.busfahrer.service;

import android.widget.TextView;

import java.util.List;

import shared.model.Card;
import shared.networking.Callback;

public interface PLabService {

    Card checkCardMatch(String cardString, Card[] cards, int row);
    int getMatchCount();
    void registerCardCallback(Callback<Card[]> callback);
    void registerFinishedLabCallback(Callback<Boolean> callback);
    Card[] getPCards();
    void finish();
    List<String> getPlayerNames();
    void startLab();
}
