package at.aau.server.service.impl;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.minlog.Log;

import java.util.ArrayList;
import java.util.List;

import at.aau.server.service.GameService;
import shared.model.Card;
import shared.model.Game;
import shared.model.GameState;
import shared.model.Player;
import shared.model.impl.GameImpl;
import shared.networking.dto.ConfirmRegisterMessage;
import shared.networking.dto.NewPlayerMessage;
import shared.networking.dto.StartGameMessage;
import shared.networking.dto.UpdateMessage;

public class GameServiceImpl implements GameService {

    private Game game;
    // Instance for singleton.
    private static GameServiceImpl instance;

    private GameServiceImpl() {
    }


    /**
     * Returns Singleton instance.
     *
     * @return instance
     */
    public static synchronized GameService getInstance() {
        if (instance == null) {
            instance = new GameServiceImpl();
            return instance;
        }
        return instance;
    }

    /**
     * This method destroy the Singleton Instance.
     * It's needed for testing purposes.
     */
    public static synchronized void destroyInstance() {
        instance = null;
    }

    @Override//Player list was moved into game object
    public List<Player> getPlayerList() {
        return game.getPlayerList();
    }

    public Player addPlayer(String name, String MACAdress, Connection connection) {
        return game.addPlayer(name, MACAdress, connection);
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
                game.setState(GameState.LAP1A);
                break;
            case LAP1A:
                game.setState(GameState.LAP2);
                break;
            case LAP2:
                game.setState(GameState.LAP3);
                break;
        }

    }

    @Override
    public void startGame() {
        this.game.setState(GameState.STARTED);

        //send start game message to each client
        StartGameMessage sgm = new StartGameMessage();

        int count = this.game.getPlayerCount();
        for (int i = 0; i < count; i++) {
            Connection con = this.game.getPlayerList().get(i).getConnection();
            con.sendTCP(sgm);
        }

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
    public void createGame(String masterName, String MACAddress, Connection connection) {
        createGame();

        Player player = addPlayer(masterName, MACAddress, connection);

        ConfirmRegisterMessage crm = new ConfirmRegisterMessage(player, true);
        connection.sendTCP(crm);//sendet ConfirmRegisterMessage an Client

        //Add Player to Playerlist in Wait UI
        NewPlayerMessage npm = new NewPlayerMessage(player.getName());
        connection.sendTCP(npm);

        Log.info("Game created.");
    }

    // TODO: check if we still need this method ???
    public void createGame(){
        this.game=new GameImpl();
    }

    @Override
    public boolean gameExists() {
        return game != null;
    }

    @Override
    public Card[] getPlayersCards(int player) {
        return game.getPlayersCards(player);
    }

    public void startPLab() {
        this.game.setState(GameState.LAP2);

    }


    @Override
    public void GuessRound(GameState lap, int tempID, boolean scored) {

        if (scored)
            game.addPointsToPlayer(tempID, 1);

        //ArrayList of all players scores
        ArrayList<Integer> score = new ArrayList<>();
        for (int i = 0; i < game.getPlayerCount(); i++) {
            int playerScore = game.getPlayerList().get(i).getScore();
            score.add(playerScore);
        }
        //Reset Cheating
        resetCheated();

        int nextPlayer = -1;
        if (tempID < (game.getPlayerCount() - 1)) {
            nextPlayer = tempID + 1;
        } else { //if next player = 0 --> client starts guess round 2!
            nextPlayer = 0;
            this.game.setState(lap);
        }

        game.setCurrentPlayer(nextPlayer);

        //send DTO updateMessage to all clients
        UpdateMessage uM = new UpdateMessage(nextPlayer, score);
        int count = this.game.getPlayerCount();
        for (int i = 0; i < count; i++) {
            Connection con = this.game.getPlayerList().get(i).getConnection();
            con.sendTCP(uM);
        }
    }

    private void resetCheated() {
        for (int i = 0; i < game.getPlayerCount(); i++) {
            game.getPlayerList().get(i).setCheatedThisRound(false);
        }
    }



}
