package shared.model;

import java.util.List;

public interface Game {

    GameState getState();
    void setState(GameState state);
    int getPlayerCount();
    List<Player> getPlayerList();
    void setPlayerList(List<Player> playerList);
    boolean addPlayer(String name, String MACAdress);
    Card[] getPlayersCards(int player);
}
