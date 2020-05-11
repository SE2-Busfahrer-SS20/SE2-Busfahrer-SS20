package at.aau.server.service;

import com.esotericsoftware.kryonet.Connection;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;

import at.aau.server.service.impl.GameServiceImpl;
import shared.exceptions.PlayerLimitExceededException;
import shared.model.Card;
import shared.model.Game;
import shared.model.GameState;
import shared.model.Player;
import shared.model.impl.PlayerImpl;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class GameServiceTest {

    private final static int MAX_PLAYERS = 8;
    private final static int MIN_PLAYERS = 8;

    private GameService gameService;

    @Mock
    Connection mockConnection;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    /**
     * Init instance of object.
     */
    @Before
    public void init() {
        gameService = new GameServiceImpl();
    }

    /*
     * The following code tests basic methods of GameService.
     */
    @Test
    public void testGameExists() {
        assertFalse(gameService.gameExists());
    }

    @Test
    public void testGameExistsAfterCreation() throws PlayerLimitExceededException {
            gameService.createGame();
            assertTrue(gameService.gameExists());
    }


    @Test
    public void addPlayer() throws PlayerLimitExceededException {
        gameService.createGame();
        Player player = new PlayerImpl("elias", "macAdress", null,null);
        gameService.addPlayer("elias", "macAdress", null);
        List<Player> playerList = gameService.getGame().getPlayerList();
        System.out.println("SIZE:"+gameService.getGame().getPlayerCount());
        assertEquals(gameService.getGame().getPlayerCount(), 1);
    }

    @Test
    public void checkGameStates() throws PlayerLimitExceededException {
        gameService.createGame();
        assertEquals(gameService.getGame().getState(), GameState.INIT);
        gameService.startGame();
        assertEquals(gameService.getGame().getState(), GameState.STARTED);
        gameService.nextLab();
        /*
        assertEquals(gameService.getGame().getState(), GameState.LAB1);
        gameService.nextLab();
        assertEquals(gameService.getGame().getState(), GameState.LAB2);
        gameService.nextLab();
        assertEquals(gameService.getGame().getState(), GameState.LAB3);

         */
    }

    @Test
    public void checkPlayerCards() throws PlayerLimitExceededException{
        // 52 - (4*PlayerCount) remaining cards in cardStack.
        int players = 4;
        int cardsPerPlayer = 52 / players;
        // start game
        gameService.createGame();


        // check player cards.
        gameService.getGame().addPlayer("name1","mac1",null);
        gameService.getGame().addPlayer("name2","mac2",null);
        gameService.getGame().addPlayer("name3","mac3",null);
        gameService.getGame().addPlayer("name4","mac4",null);
        // check card deck after dealing cards (4 cards / Player).
        assertEquals((gameService.getGame().getCardStack()).size(), (52 - (players * 4)));


        assertEquals(gameService.getGame().getPlayerCount(), players);
        assertEquals(gameService.getGame().getPlayersCards(0).length, 4);
        assertEquals(gameService.getGame().getPlayersCards(1).length, 4);
        assertEquals(gameService.getGame().getPlayersCards(2).length, 4);
        assertEquals(gameService.getGame().getPlayersCards(3).length, 4);
    }

    /**
     * Destroy object to support garbage collector.
     */

    @After
    public void destroy() {
        gameService = null;
    }

}
