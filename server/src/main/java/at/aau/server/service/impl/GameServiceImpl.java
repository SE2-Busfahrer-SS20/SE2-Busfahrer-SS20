package at.aau.server.service.impl;

import java.util.ArrayList;
import java.util.List;

import at.aau.server.service.GameService;
import shared.model.Game;
import shared.model.GameState;
import shared.model.Player;
import shared.model.impl.GameImpl;

public class GameServiceImpl implements GameService {
    private List<Player> playerList;
    private int playerCount;
    private Game game;

    public GameServiceImpl(int playerCount) {
        this.playerList = new ArrayList<>(playerCount);
        this.playerCount = playerCount;
        this.game = new GameImpl();
    }


    @Override
    public List<Player> getPlayerList() {
        return playerList;
    }

    @Override
    public boolean addPlayer(Player player) {
        if(playerList.size() < playerCount) {
            playerList.add(player);
            return true;
        }
        return false;
    }

    @Override
    public GameState getGameState() {
        return game.getState();
    }

    @Override
    public int getPlayerCount() {
        return playerCount;
    }

    @Override
    public boolean gameReady() {
        return (playerList.size() == playerCount);
    }

    @Override
    public void nextLab() {
        // TODO: implement next Lab.
    }

    @Override
    public void startGame() {
        this.game.setState(GameState.STARTED);
    }

    @Override
    public void endGame() {
        throw new UnsupportedOperationException("Method not implemented yet.");
    }

}
