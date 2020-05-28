package shared.model;

import java.util.ArrayList;

public interface PlayersStorage {


    void registerPreGameListener(PreGameListener additionalPlayerListener);

    Card[] getCards();
    void setCards(Card[] cards);

    String getPlayerName(int index);
    ArrayList<String> getPlayerNamesList();
    //void addPlayerName(String name);

    boolean isMaster();
    void setMaster(boolean master);

    GameState getState();
    void setState(GameState state);



}
