package shared.model;

import com.esotericsoftware.kryonet.Connection;

import java.util.List;

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

    void addPointsToPlayer(int tempID, int points);
}