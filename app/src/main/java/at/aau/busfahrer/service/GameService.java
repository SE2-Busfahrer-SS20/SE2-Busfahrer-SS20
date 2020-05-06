package at.aau.busfahrer.service;

import shared.model.Card;

public interface GameService {

    void createGame(int playercount);

    void playGame(String name, String MACAddress);

    void startGame();

    boolean guessColor(int tempID, Card card, boolean guessBlack);
}
