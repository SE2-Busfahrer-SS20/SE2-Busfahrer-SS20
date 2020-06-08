package shared.model;

import java.util.ArrayList;
import java.util.List;

public interface PlayersStorage {


    void registerPreGameListener(PreGameListener additionalPlayerListener);

    Card[] getCards();
    void setCards(Card[] cards);

    String getPlayerName(int index);
    ArrayList<String> getPlayerNamesList();
    List<Integer> getScoreList();
    //void addPlayerName(String name);

    void addPlayer(PlayerDTO player);

    void resetPlayers();

    boolean isMaster();
    void setMaster(boolean master);

    GameState getState();
    void setState(GameState state);

    int getCurrentTurn();
    List<PlayerDTO> getPlayerList();

}
