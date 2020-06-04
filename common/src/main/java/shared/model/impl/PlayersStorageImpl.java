package shared.model.impl;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import shared.model.*;

public class PlayersStorageImpl implements PlayersStorage {

    private int tempID; //This ID equals the Index in playerList (ArrayList in Game object)
    private Card[] cards;
    private GameState state;

    private Card[] bushmenCards;
   //private ArrayList<String> playerNames;
    private boolean master=false;
    private int currentTurn;
    //private ArrayList<Integer> score;
    //private ArrayList<Boolean> isCheating;
    private List<PlayerDTO> playerList;


    public static void reset() {
        PlayersStorageImpl.instance = null;
    }

    //Singleton Pattern
    private static PlayersStorageImpl instance;


    private PlayersStorageImpl(){
//        playerNames = new ArrayList<String>();
//        score= new ArrayList<Integer>();
        playerList= new ArrayList<PlayerDTO>();
        state=GameState.INIT;

    }

    public static synchronized PlayersStorageImpl getInstance() {
        if (PlayersStorageImpl.instance == null) {
            PlayersStorageImpl.instance = new PlayersStorageImpl();
        }
        return PlayersStorageImpl.instance;
    }

    //Callback for WaitActivity
    private PreGameListener preGameListener;

    public void registerPreGameListener(PreGameListener preGameListener) {
        this.preGameListener = preGameListener;
    }

    private void updatePlayerList() {
        new Thread(new Runnable() {
            public void run() {
                if (preGameListener != null) {
                    preGameListener.onAdditionalPlayer();
                }
            }
        }).start();
    }

    private void gameStateChangedToReady() {
        new Thread(new Runnable() {
            public void run() {
                if (preGameListener != null) {
                    preGameListener.onGameStart();
                }
            }
        }).start();
    }

    //Callback for GuessActivity
    private GuessRoundListener guessRoundListener;

    public void registerGuessRoundListener(GuessRoundListener guessRoundListener) {
        this.guessRoundListener = guessRoundListener;
    }

    private void nextPlayersTurn() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (guessRoundListener != null) {
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
        return playerList.get(index).getName();
    }
    public ArrayList<String> getPlayerNamesList(){
        ArrayList<String> playerNames= new ArrayList<>();
        System.out.println("\n\n"+playerList.size());
        for(int i=0; i<playerList.size();i++){
            playerNames.add(playerList.get(i).getName());
            System.out.println(playerList.get(i).getName());
        }
        return playerNames;
    }
    public void addPlayer(PlayerDTO player){
        playerList.add(player);
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
        if (this.state == GameState.INIT && state == GameState.READY) {
            this.state = state;
            gameStateChangedToReady();
        } else
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

    public void setPlayerList(ArrayList<PlayerDTO> players) {
        this.playerList = players;
    }
    public List<PlayerDTO> getPlayerList(){
        return this.playerList;
    }


    public ArrayList<Integer> getScoreList() {
        ArrayList<Integer> playerScores= new ArrayList<>();
        for(int i=0; i<playerList.size();i++){
            playerScores.add(playerList.get(i).getScore());
        }
        return playerScores;
    }

//    public void setScore(ArrayList<Integer> score) {
//        this.score = score;
//    }
//    public void initScores(){
//        ArrayList<Integer> scores= new ArrayList<Integer>();
//        for(int i=0;i< getPlayerNames().size();i++){
//            scores.add(0);
//        }
//        this.score=scores;
//    }

    public void updateOnMessage(List<PlayerDTO> playerList, int currentTurn){
        this.playerList=playerList;
        this.currentTurn=currentTurn;
        nextPlayersTurn(); //Callback
    }
    public void setPlayerFromDTO(List<PlayerDTO> playerDTOList){
//        ArrayList<String> names= new ArrayList<>();
//        ArrayList<Integer> scores= new ArrayList<>();
//        ArrayList<Boolean> isCheating= new ArrayList<>();
//
//
//        for(int i=0;i<playerDTOList.size();i++){
//            names.add(playerDTOList.get(i).getName());
//            scores.add(playerDTOList.get(i).getScore());
//        }
//        this.playerNames=names;
//        this.score= scores;
//        this.isCheating=isCheating;
        this.playerList=playerDTOList;
    }

    public boolean isCheating(int index){
        return playerList.get(index).isCheating();
    }
    public void setCheating(int index){
        playerList.get(index).setCheating(true);
        System.out.println("\n\n\n"+playerList.get(index).isCheating()+"\n\n\n");
    }


}

