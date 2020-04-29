package at.aau.server.service.impl;

import com.esotericsoftware.kryonet.Connection;
import java.util.List;

import at.aau.server.service.GameService;
import shared.exceptions.PlayerLimitExceededException;
import shared.model.Card;
import shared.model.Deck;
import shared.model.Game;
import shared.model.GameState;
import shared.model.Player;
import shared.model.impl.GameImpl;
import shared.networking.dto.StartGameMessage;

public class GameServiceImpl implements GameService {

    private Game game;

    public GameServiceImpl() {
    }

    @Override//Player list was moved into game object
    public List<Player> getPlayerList() {
        return game.getPlayerList();
    }

    @Override   //Remove this method
    public boolean addPlayer(Player player) {
        List<Player> playerList = getPlayerList();

        if(playerList.size() < game.getPlayerCount()) {
            playerList.add(player);
            game.setPlayerList(playerList);
            return true;
        }
        return false;
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

        //send start game message to each client
        StartGameMessage sgm = new StartGameMessage();
        int count = this.game.getPlayerCount();
        for(int i=0;i<count;i++){
            System.out.println("sending SGM...");
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
    public void createGame() throws PlayerLimitExceededException {
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





}
