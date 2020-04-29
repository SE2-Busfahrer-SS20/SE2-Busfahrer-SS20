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
import shared.model.impl.CardImpl;
import shared.model.impl.DeckImpl;
import shared.model.impl.GameImpl;
import shared.model.impl.PlayerImpl;

public class GameServiceImpl implements GameService {

/*OVE EVERYTHING FROM HERE ..
    // define constants for MAX Players
    private final static int PLAYER_LIMIT_MAX = 8;
    private final static int PLAYER_LIMIT_MIN = 2;

    private int maxPlayerCount;
    private int playerCount;
    private Card[][] playercards; //Array of size: [amount of players][4]
    private Deck cardStack;
TO HERE INTO GAME OBJECT!!!
 */

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


    @Override//Maybee not needed anymore
    public int joinGame(){
       if(playerCount<=maxPlayerCount){
            playerCount++;
            return playerCount;
        }
        else{
            // max player count already reached.
            return -1;
        }

    }



}
