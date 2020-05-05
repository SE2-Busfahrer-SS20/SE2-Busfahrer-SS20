package at.aau.busfahrer.service;

import android.widget.TextView;

import shared.model.Card;
import shared.networking.Callback;

public interface PLabService {

    Card checkCardMatch(String cardString, Card[] cards);
    int getMatchCount();
    void registerCardCallback(Callback<Card[]> callback);
    Card[] getPCards();
}
