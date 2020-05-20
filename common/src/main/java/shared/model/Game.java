package shared.model;

import com.esotericsoftware.kryonet.Connection;

import java.util.List;
import java.util.Set;

public interface Game {

    GameState getState();
    void setState(GameState state);
    int getPlayerCount();
    List<Player> getPlayerList();
    void setPlayerList(List<Player> playerList);
    Player addPlayer(String name, String MACAdress, Connection connection);
    Card[] getPlayersCards(int player);

    public Deck getCardStack();
    public void setCardStack(Deck cardStack);
    /**
     * Cards for Pyramidenrunde.
     * @return Card[]
     */
    public Card[] getpCards();
    void addPointsToPlayer(int tempID, int points);

    int getCurrentPlayer();
    void setCurrentPlayer(int currentPlayer);

    /**
     * Returns counter of gamers, which are already dealed there points.
     * @return plapFinishedCounter.
     */
    int getPlapFinishedCount();

    /**
     * increases the finished counter per 1.
     */
    void playerFinishedPLap();

}