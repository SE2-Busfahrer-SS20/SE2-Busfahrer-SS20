package at.aau.busfahrer.service;

import shared.model.Card;
import shared.model.GameState;

public interface GamePlayService {

    void createGame(int playercount);

    void playGame(String name, String MACAddress);

    void startGame();

    //GuessRound
    boolean guessColor(Card card, boolean guessBlack);
    boolean guessHigherLower(Card card, Card reference, boolean guessHigher);
    boolean guessBetweenOutside(Card card, Card refOne, Card refTwo, boolean guessBetween);
    boolean guessSuit(Card card, int suit);

    void nextPlayer(final GameState lap, final int tempID, final boolean scored);
    void sendMsgCheated(final int playerId, final boolean cheated, final long timeStamp, final int cheatType);
    void setHostName(String hostname);
    void sendMsgCought(final int indexCheater, final int indexCought, final int scoreCheater, final int scoreCought);

}
