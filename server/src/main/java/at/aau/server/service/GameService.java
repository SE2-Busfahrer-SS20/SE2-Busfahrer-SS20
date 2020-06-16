package at.aau.server.service;

import com.esotericsoftware.kryonet.Connection;

import java.util.List;

import shared.exceptions.PlayerLimitExceededException;
import shared.model.Card;
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
     * @return boolean status,  false in case of a full List.
     */

    Player addPlayer(String name, String macadress, Connection connection);


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
    void createGame();
    void createGame(String masterName, String macAddress, Connection connection) throws PlayerLimitExceededException;
    boolean gameExists();
    Card[] getPlayersCards(int player);
    //Guess-Rounds
    void guessRound(GameState lap, int tempID, boolean scored);
    Card[] getBushmenCards();
}
