package shared.model.impl;

import com.esotericsoftware.kryonet.Connection;
import java.util.ArrayList;
import java.util.List;

import shared.model.Card;
import shared.model.Deck;
import shared.model.Game;
import shared.model.GameState;
import shared.model.Player;

public class GameImpl implements Game {

    // define constants for MAX Players
    private final static int PLAYER_LIMIT_MAX = 8;
    private final static int PLAYER_LIMIT_MIN = 2;

    private GameState state;
    private List<Player> playerList;
    private Deck cardStack;

    private int currentPlayer; //It is this players turn //value=Index of playerList



    public GameImpl() {
        this.state = GameState.INIT;
        this.playerList = new ArrayList<>();
        cardStack=new DeckImpl();//Add 52 Cards to Stack
        currentPlayer=0; //in first Guess-Round the player who was Master in WaitActivity starts
    }

    public Player addPlayer(String name, String MACAdress, Connection connection){
        if(playerList.size()<8) {
            Card[] cards = new CardImpl[4];
            for (int i = 0; i < 4; i++) {
                cards[i] = cardStack.drawCard();
            }
            Player newPlayer = new PlayerImpl(name, MACAdress, cards, connection);
            newPlayer.setTempID(playerList.size()); //Index starts with 0, so size returns highest index + 1
            playerList.add(newPlayer);
            return newPlayer;
        }
        return null;
    }

    @Override
    public Card[] getPlayersCards(int player) {
        return playerList.get(player).getCards();
    }

    @Override
    public GameState getState() {
        return state;
    }

    @Override
    public void setState(GameState state) {
        this.state = state;
    }

    @Override
    public int getPlayerCount() {
        return this.playerList.size();
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

    public static int getPlayerLimitMax() {
        return PLAYER_LIMIT_MAX;
    }

    public static int getPlayerLimitMin() {
        return PLAYER_LIMIT_MIN;
    }

    public Deck getCardStack() {
        return cardStack;
    }

    public void setCardStack(Deck cardStack) {
        this.cardStack = cardStack;
    }

    public void addPointsToPlayer(int tempID, int points){
        playerList.get(tempID).addPoints(points);
    }
}
