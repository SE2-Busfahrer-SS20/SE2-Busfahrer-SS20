package shared.model.impl;

import java.util.ArrayList;

import shared.model.Card;
import shared.model.GameState;

public class playersStorage {

    /* this class is only used to store things in common because they can not be stored in app
     * since the listener which receives them from the server is in NetwerkClientKryo.java and from there
     * it is not possible to access objects in the app
    */

    private static Card[] cards;
    private static ArrayList<String> playerNames = new ArrayList<String>();
    private static boolean master=false;
    private static GameState state=GameState.INIT;

    public static Card[] getCards() {
        return cards;
    }
    public static void setCards(Card[] cards) {
        playersStorage.cards = cards;
    }

    public static String getPlayerName(int index){
        return playerNames.get(index);
    }
    public static ArrayList<String> getPlayerNames(){
        return playerNames;
    }
    public static void addPlayerName(String name){
        playerNames.add(name);
    }

    public static boolean isMaster() {
        return master;
    }
    public static void setMaster(boolean master) {
        playersStorage.master = master;
    }

    public static GameState getState() {
        return state;
    }
    public static void setState(GameState state) {
        playersStorage.state = state;
    }
}
