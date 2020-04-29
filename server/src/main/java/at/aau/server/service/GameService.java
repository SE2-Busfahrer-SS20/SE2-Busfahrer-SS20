package at.aau.server.service;

import com.esotericsoftware.kryonet.Connection;

import java.util.List;

import shared.exceptions.PlayerLimitExceededException;
import shared.model.Card;
import shared.model.Deck;
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
    Player addPlayer(String name, String MACAdress, Connection connection);


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
    void createGame() throws PlayerLimitExceededException;
    boolean gameExists();
    //Card[][] getPlayercardList();
    // Deck getCardStack();
    Card[] getPlayersCards(int player);
    public int joinGame();
}
