package at.aau.server.service;

import java.util.List;

import shared.model.Game;
import shared.model.GameState;
import shared.model.Player;

public interface GameService {
    /**
     * Returns List of registered Players.
     * @return List<Player>
     */
    List<Player> getPlayerList();

    /**
     * Add Player to list.
     * @param player
     * @return boolean status,  false in case of a full List.
     */
    boolean addPlayer(Player player);

    /**
     *
     * @return GameState
     */
    GameState getGameState();

    /**
     *
     * @return count of Players.
     */
    int getPlayerCount();
    boolean gameReady();
    void nextLab();
    void startGame();
    void endGame();
    Game getGame();
    void createGame(int playerCount);
}
