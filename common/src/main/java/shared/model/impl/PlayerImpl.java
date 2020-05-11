package shared.model.impl;

import com.esotericsoftware.kryonet.Connection;

import shared.model.Card;
import shared.model.Player;

public class PlayerImpl implements Player {

    private int tempID; //=Index in playerList //-1 if not an element of this list
    private String name;
    private String MACAdress;
    private Card[] cards;
    private Connection connection;
    private int score;
    private boolean cheated;            //every player can only cheat once in the whole game - if this variable is true, the player can't cheat anymore
    private boolean cheatedThisRound;   //this variable is necessary to catch a cheater - This variable needs to be reseted after each round (because you can only catch a cheater in the round he/she cheated)


    //Constructor for Server Side
    public PlayerImpl(String name,String MACAdress, Card[] cards, Connection connection ) {
        tempID=-1;
        this.name = name;
        this.MACAdress=MACAdress;
        this.cards=cards;
        this.connection = connection;
        this.score=0;
        this.cheated=false;
        this.cheatedThisRound=false;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getMACAdress() {
        return this.MACAdress;
    }

    @Override
    public Card[] getCards() {
        return cards;
    }

    @Override
    public Card getCard(int index) {
        return cards[index];
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public void setScore(int newCount) {
        this.score = newCount;
    }

    @Override
    public void addPoints(int points){
        this.score+=points;
    }

    @Override
    public int getTempID() {
        return tempID;
    }

    @Override
    public void setTempID(int tempID) {
        this.tempID = tempID;
    }

    @Override
    public boolean isCheated() {
        return cheated;
    }

    @Override
    public void setCheated(boolean cheated) {
        this.cheated = cheated;
    }

    @Override
    public boolean isCheatedThisRound() {
        return cheatedThisRound;
    }

    @Override
    public void setCheatedThisRound(boolean cheatedThisRound) {
        this.cheatedThisRound = cheatedThisRound;
    }

}
