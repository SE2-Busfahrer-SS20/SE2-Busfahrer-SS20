package at.aau.server.service.impl;

import java.util.List;

import at.aau.server.service.GamerService;
import shared.model.GameState;
import shared.model.Player;

public class GamerServiceImpl implements GamerService {
    @Override
    public List<Player> getPlayerList() {
        return null;
    }

    @Override
    public boolean addPlayer(Player player) {
        return false;
    }

    @Override
    public GameState getGameState() {
        return null;
    }

    @Override
    public int getPlayerCount() {
        return 0;
    }

    @Override
    public boolean gameReady() {
        return false;
    }

    @Override
    public void nextLab() {

    }

    @Override
    public void startGame() {

    }

    @Override
    public void endGame() {

    }
}
