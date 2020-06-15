package shared.model.impl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import shared.model.*;

public class PlayersStorageImpl implements PlayersStorage {

    private int tempID; //This ID equals the Index in playerList (ArrayList in Game object)
    private Card[] cards;
    private GameState state;


    private boolean master=false;
    private int currentTurn;
    private List<PlayerDTO> playerList;


  public static void reset(){
      PlayersStorageImpl.instance=null;
  }

    public void resetPlayers() {
        playerList= new ArrayList<>();
    }

    //Singleton Pattern
    private static PlayersStorageImpl instance;


    private PlayersStorageImpl(){
        playerList= new ArrayList<>();
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
        new Thread(() -> {
            if (preGameListener != null) {
                preGameListener.onAdditionalPlayer();
            }
        }).start();
    }

    private void gameStateChangedToReady() {
        new Thread(() -> {
            if (preGameListener != null) {
                preGameListener.onGameStart();
            }
        }).start();
    }

    //Callback for GuessActivity
    private GuessRoundListener guessRoundListener;

    public void registerGuessRoundListener(GuessRoundListener guessRoundListener) {
        this.guessRoundListener = guessRoundListener;
    }

    private void nextPlayersTurn() {

        new Thread(() -> {
            if (guessRoundListener != null) {
                guessRoundListener.onUpdateMessage();
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

        for(int i=0; i<playerList.size();i++){
            playerNames.add(playerList.get(i).getName());
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
    @Override
    public int getTempID() {
        return tempID;
    }

    @Override
    public void setTempID(int tempID) {
        this.tempID = tempID;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
    }

    public void setPlayerList(List<PlayerDTO> players) {
        this.playerList = players;
    }
    public List<PlayerDTO> getPlayerList(){
        return this.playerList;
    }
    public List<PlayerDTO> getPlayerListAscending(){
        List<PlayerDTO> playerListAscending= this.playerList;

                Collections.sort(playerListAscending, (p1, p2) -> {
                    return p1.getScore() - p2.getScore(); // Ascending
                });
        return playerListAscending;
    }

    @Override
    public List<Integer> getScoreList() {
        ArrayList<Integer> playerScores= new ArrayList<>();
        for(int i=0; i<playerList.size();i++){
            playerScores.add(playerList.get(i).getScore());
        }
        return playerScores;
    }

    //UpdateScoreBushmenActivity
    @Override
    public void addScoreToCurrentPlayer(int score) {
        PlayerDTO currentPlayer = playerList.get(tempID);

        if (currentPlayer != null) {
            currentPlayer.setScore(currentPlayer.getScore() + score);
        }
    }

    public void updateOnMessage(List<PlayerDTO> playerList, int currentTurn){
        this.playerList=playerList;
        this.currentTurn=currentTurn;
        if(getState() != GameState.LAP2){
            nextPlayersTurn(); //Callback
        }
    }
    public void setPlayerFromDTO(List<PlayerDTO> playerDTOList){
        this.playerList=playerDTOList;
    }

    public boolean isCheating(int index){
        return playerList.get(index).isCheating();
    }
    public void setCheating(int index){
        playerList.get(index).setCheating(true);
    }


}

