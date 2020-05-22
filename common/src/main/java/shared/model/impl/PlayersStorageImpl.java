package shared.model.impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import shared.model.Card;
import shared.model.GameState;
import shared.model.GuessRoundListener;
import shared.model.Player;
import shared.model.PreGameListener;
import shared.model.PlayersStorage;

public class PlayersStorageImpl implements PlayersStorage {

    private int tempID; //This ID equals the Index in playerList (ArrayList in Game object)
    private Card[] cards;
    private GameState state;
    private ArrayList<String> playerNames;
    private boolean master=false;
    private int currentTurn;
    private ArrayList<Integer> score;
    private List<Player> playersList;
    private Card[] bushmenCards;


    //Singleton Pattern
    private static PlayersStorageImpl instance;

    private PlayersStorageImpl(){
        playerNames = new ArrayList<String>();
        score= new ArrayList<Integer>();
        state=GameState.INIT;
    }

    public static synchronized PlayersStorageImpl getInstance(){
        if(PlayersStorageImpl.instance==null){
            PlayersStorageImpl.instance=new PlayersStorageImpl();
        }
        return PlayersStorageImpl.instance;
    }

    //Callback for WaitActivity
    private PreGameListener preGameListener;

    public void registerPreGameListener(PreGameListener preGameListener){
        this.preGameListener=preGameListener;
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

    //Callback for GuessActivity
    private GuessRoundListener guessRoundListener;

    public void registerGuessRoundListener(GuessRoundListener guessRoundListener){
        this.guessRoundListener=guessRoundListener;
    }

    private void nextPlayersTurn(){

        new Thread(new Runnable(){
            @Override
            public void run(){
                if(guessRoundListener!=null){
                    guessRoundListener.onUpdateMessage();
                }
            }
        }).start();

    }

    //Getter, Setter Methodes
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
        score.add(0);
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

    public int getCurrentTurn() {
        return currentTurn;
    }
    public void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
    }

    public void setPlayerNames(ArrayList<String> playerNames) {
        this.playerNames = playerNames;
    }

    public ArrayList<Integer> getScore() {
        return score;
    }

    public void setScore(ArrayList<Integer> score) {
        this.score = score;
    }
    public void initScores(){
        ArrayList<Integer> scores= new ArrayList<Integer>();
        for(int i=0;i< getPlayerNames().size();i++){
            scores.add(0);
        }
        this.score=scores;
    }

    public void updateOnMessage(ArrayList<Integer> score, int currentTurn){
        this.score=score;
        this.currentTurn=currentTurn;
        nextPlayersTurn(); //Callback
    }

    public List<Player> getPlayersList() {
        return playersList;
    }

    public void setPlayersList(List<Player> playersList) {
        this.playersList = playersList;
    }

    public void setBushmenCards(Card[] cards){
        this.bushmenCards=cards;
    }

    public Card[] getBushmenCards(){
        return bushmenCards;
    }
}

