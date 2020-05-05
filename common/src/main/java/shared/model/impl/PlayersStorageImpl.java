package shared.model.impl;

import java.util.ArrayList;

import shared.model.Card;
import shared.model.GameState;
import shared.model.OnAdditionalPlayerListener;
import shared.model.PlayersStorage;


public class PlayersStorageImpl implements PlayersStorage {

    /* this class is only used to store things in common because they can not be stored in app
     * since the listener which receives them from the server is in NetwerkClientKryo.java and from there
     * it is not possible to access objects in the app
    */
    private Card[] cards;
    private ArrayList<String> playerNames;
    private boolean master=false;
    private GameState state;

    //Callback stuff
    private OnAdditionalPlayerListener additionalPlayerListener;

    public void registerOnAdditionalPlayerListener(OnAdditionalPlayerListener additionalPlayerListener){
        this.additionalPlayerListener=additionalPlayerListener;
    }
    private void updatePlayerList(){
        new Thread(new Runnable(){
            public void run(){
                System.out.println("CALLBACK in PlayersStorageImpl!!!");
                if (additionalPlayerListener !=null){
                    additionalPlayerListener.onAdditionalPlayer();
                }
            }
        }).start();
    }

    //Singleton Pattern
    private static PlayersStorageImpl instance;

    private PlayersStorageImpl(){
        playerNames = new ArrayList<String>();
        state=GameState.INIT;
    };

    public static synchronized PlayersStorageImpl getInstance(){
        if(PlayersStorageImpl.instance==null){
            PlayersStorageImpl.instance=new PlayersStorageImpl();
        }
        return PlayersStorageImpl.instance;
    }

    public Card[] getCards() {
        return cards;
    }
    public void setCards(Card[] cards) {
        this.cards = cards;
    }

    public String getPlayerName(int index){
        return playerNames.get(index);
    }
    public ArrayList<String> getPlayerNames(){
        return playerNames;
    }
    public  void addPlayerName(String name){
        playerNames.add(name);
        updatePlayerList();
    }

    public boolean isMaster() {
        return master;
    }
    public void setMaster(boolean master) {
        this.master = master;
    }
    public GameState getState() {
        return state;
    }
    public void setState(GameState state) {
        this.state = state;
    }

}
