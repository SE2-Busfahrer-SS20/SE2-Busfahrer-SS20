package at.aau.server.service.impl;

import java.util.List;

import at.aau.server.service.GameService;
import shared.model.Game;
import shared.model.GameState;
import shared.model.Player;
import shared.model.impl.GameImpl;

public class GameServiceImpl implements GameService {


    private Game game;

    public GameServiceImpl() {
    }


    @Override
    public List<Player> getPlayerList() {
        return game.getPlayerList();
    }

    @Override
    public boolean addPlayer(Player player) {
        List<Player> playerList = getPlayerList();

        if(playerList.size() < game.getPlayerCount()) {
            playerList.add(player);
            game.setPlayerList(playerList);
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
        return game.getPlayerCount();
    }

    @Override
    public boolean gameReady() {
        return (game.getPlayerList().size() == game.getPlayerCount());
    }

    @Override
    public void nextLab() {
        switch (game.getState()) {
            case STARTED:
                game.setState(GameState.LAB1);
                break;
            case LAB1:
                game.setState(GameState.LAB2);
                break;
            case LAB2:
                game.setState(GameState.LAB3);
                break;
        }

    }

    @Override
    public void startGame() {
        this.game.setState(GameState.STARTED);
    }

    @Override
    public void endGame() {
        throw new UnsupportedOperationException("Method not implemented yet.");
    }

    @Override
    public Game getGame() {
        return this.game;
    }

    @Override
    public void createGame(int playerCount) {
        if (this.game == null)
            this.game = new GameImpl(playerCount);
    }

}
