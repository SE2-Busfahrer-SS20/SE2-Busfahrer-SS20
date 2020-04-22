package at.aau.server.service.impl;

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

public class GameServiceImpl implements GameService {


    // define constants for MAX Players
    private final static int PLAYER_LIMIT_MAX = 8;
    private final static int PLAYER_LIMIT_MIN = 2;

    private int maxPlayerCount;
    private int playerCount;
    private Deck cardStack;
    private Card[][] playercards; //Array of size: [amount of players][4]

    private Game game;//Ignored for now

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
    public void createGame(int playerCount) throws PlayerLimitExceededException {
        if (playerCount > PLAYER_LIMIT_MAX || playerCount < PLAYER_LIMIT_MIN)
            throw new PlayerLimitExceededException();
        this.maxPlayerCount=playerCount;
        this.playerCount=1;

        if (this.game == null)
            this.game = new GameImpl(playerCount);


        //till now there is only one game possible
        //classes Game and Player are ignored
        //Code will be extended and use them later on
        //for now Users are identified with ID (to send cards for example)

        cardStack=new DeckImpl();
        playercards=new CardImpl[playerCount][4];

        for(int i=0; i<playercards.length;i++){
            for(int j=0; j<playercards[i].length;j++){
                playercards[i][j]=cardStack.drawCard();
            }
        }
    }

    @Override
    public boolean gameExists() {
        return game != null;
    }

    @Override
    public Card[] getPlayersCards(int player) {
        return playercards[player];
    }

    @Override
    public int joinGame(){
        if(playerCount<=maxPlayerCount){
            playerCount++;
            return playerCount;
        }
        else{
            //Maximale Spieleranzahl wurde bereits erreicht
            return -1;
        }

    }

}
