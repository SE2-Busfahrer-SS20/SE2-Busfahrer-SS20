package at.aau.server.service.impl;

import com.esotericsoftware.kryonet.Connection;

import java.util.ArrayList;
import java.util.List;

import at.aau.server.service.GameService;
import shared.exceptions.PlayerLimitExceededException;
import shared.model.Card;
import shared.model.Game;
import shared.model.GameState;
import shared.model.Player;
import shared.model.impl.GameImpl;
import shared.networking.dto.StartGameMessage;
import shared.networking.dto.updateMessage;

public class GameServiceImpl implements GameService {

    private Game game;

    public GameServiceImpl() {
    }

    @Override//Player list was moved into game object
    public List<Player> getPlayerList() {
        return game.getPlayerList();
    }

    public Player addPlayer(String name, String MACAdress, Connection connection){
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
                game.setState(GameState.LAP1);
                break;
            case LAP1:
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
        for(int i=0;i<count;i++){
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
    public void createGame() {
        game=new GameImpl();
              //Only one game possible
    }

    @Override
    public boolean gameExists() {
        return game != null;
    }

    @Override
    public Card[] getPlayersCards(int player) {
        return game.getPlayersCards(player);
    }


    //Methodes for Guess-Rounds

    @Override
    public void GuessRound1(int tempID, boolean scored) {
        //update score
        if(scored)
            game.addPointsToPlayer(tempID,1);

        //ArrayList of all players scores
        ArrayList<Integer> score = new ArrayList<>();
        for(int i=0; i<game.getPlayerCount();i++){
            int playerScore=game.getPlayerList().get(i).getScore();
            score.add(playerScore);
        }

        //Who's next?
        int nextPlayer;
        if(tempID<game.getPlayerCount()){
            nextPlayer=tempID+1;
            this.game.setState(GameState.LAP1);
        }
        else{ //if next player = 0 --> client starts guess round 2!
            nextPlayer=0;
            this.game.setState(GameState.LAP2);

        }

        //send DTO updateMessage to all clients
        this.game.setState(GameState.LAP1);
        updateMessage uM = new updateMessage(nextPlayer, score);
        int count = this.game.getPlayerCount();
        for(int i=0;i<count;i++){
            Connection con = this.game.getPlayerList().get(i).getConnection();
            con.sendTCP(uM);
        }


    }





}
