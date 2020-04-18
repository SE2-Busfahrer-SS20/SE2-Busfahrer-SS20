package shared.model.impl;

import java.util.ArrayList;
import java.util.List;

import shared.model.Game;
import shared.model.GameState;
import shared.model.Player;

public class GameImpl implements Game {

    private GameState state;
    private int gameCount;
    private List<Player> playerList;



    public GameImpl(int gameCount) {
        this.gameCount = gameCount;
        this.playerList = new ArrayList<>(this.gameCount);
        this.state = GameState.INIT;
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
        return gameCount;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

}
