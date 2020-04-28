package shared.model.impl;

import com.esotericsoftware.kryonet.Connection;

import shared.model.Player;

public class PlayerImpl implements Player {
    private String name;
    private Connection connection;
    private int count;

    public PlayerImpl(String name, Connection connection) {
        this.name = name;
        this.connection = connection;
    }

    @Override
    public String getName() {
        return name;
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
