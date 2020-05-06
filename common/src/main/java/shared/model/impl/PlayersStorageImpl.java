package shared.model.impl;
import java.util.ArrayList;

import shared.model.Card;
import shared.model.GameState;
import shared.model.PreGameListener;
import shared.model.PlayersStorage;

public class PlayersStorageImpl implements PlayersStorage {

    private int tempID; //This ID equals the Index in playerList (ArrayList in Game object)
    private Card[] cards;
    private ArrayList<String> playerNames;
    private boolean master=false;
    private GameState state;

    //Callback stuff
    private PreGameListener preGameListener;

    public void registerOnAdditionalPlayerListener(PreGameListener additionalPlayerListener){
        this.preGameListener=additionalPlayerListener;
    }
    private void updatePlayerList(){
        new Thread(new Runnable(){
            public void run(){
                if (preGameListener !=null){
                    preGameListener.onAdditionalPlayer();
                }
            }
        }).start();
    }

    private void gameStateChangedToReady(){
        new Thread(new Runnable(){
            public void run(){
                if (preGameListener !=null){
                    preGameListener.onGameStart();
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
        //if game changes from init to ready, the WaitActivity needs to receive a callback to start the game!
        if(this.state==GameState.INIT&&state==GameState.READY){
            this.state = state;
            gameStateChangedToReady();
        }
        else
            this.state = state;
    }

    public int getTempID() {
        return tempID;
    }

    public void setTempID(int tempID) {
        this.tempID = tempID;
    }
}
