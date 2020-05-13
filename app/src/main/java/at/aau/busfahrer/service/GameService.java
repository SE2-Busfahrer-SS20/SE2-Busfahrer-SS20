package at.aau.busfahrer.service;

import shared.model.Card;

public interface GameService {

    void createGame(int playercount);

    void playGame(String name, String MACAddress);

    void startGame();

    //GuessRound
    boolean guessColor(int tempID, Card card, boolean guessBlack);
    boolean guessHigherLower(final int tempID, Card card, boolean guessHigher);
    boolean guessBetweenOutside(final int tempID, Card card, boolean guessBetween);
    boolean guessSuit(final int tempID, Card card, int suit);
    void nextPlayer(final int lap, final int tempID, final boolean scored);

    void sendMsgCheated(final int playerId, final boolean cheated, final long timeStamp, final int cheatType);

}
