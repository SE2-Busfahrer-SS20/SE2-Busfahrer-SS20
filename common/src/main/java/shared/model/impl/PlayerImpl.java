package shared.model.impl;

import com.esotericsoftware.kryonet.Connection;

import shared.model.Card;
import shared.model.Player;

public class PlayerImpl implements Player {
    private String name;
    private String MACAdress;
    private Card[] cards;
    private Connection connection;
    private int count;


    //Constructor for Server Side
    public PlayerImpl(String name,String MACAdress, Card[] cards, Connection connection ) {
        this.name = name;
        this.MACAdress=MACAdress;
        this.cards=cards;
        this.connection = connection;
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
    public int getCount() {
        return count;
    }

    @Override
    public void setCount(int newCount) {
        this.count = newCount;
    }

}
