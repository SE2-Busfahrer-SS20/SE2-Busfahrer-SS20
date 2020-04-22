package at.aau.server.service;

import com.esotericsoftware.kryonet.Connection;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.List;

import at.aau.server.service.GameService;
import at.aau.server.service.impl.GameServiceImpl;
import shared.exceptions.PlayerLimitExceededException;
import shared.model.Card;
import shared.model.GameState;
import shared.model.Player;
import shared.model.impl.PlayerImpl;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
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

    @Test
    public void testGameExists() {
        assertFalse(gameService.gameExists());
    }

    @Test
    public void testGameExistsAfterCreation() throws PlayerLimitExceededException {
            gameService.createGame(4);
            assertTrue(gameService.gameExists());
    }

    @Test
    public void testMaxPlayerCount() {
       try {
           gameService.createGame(MAX_PLAYERS);
       } catch (Exception e) {
           fail();
       }
       try {
           gameService.createGame(MAX_PLAYERS + 1);
           fail();
       } catch (PlayerLimitExceededException ex)
       { } catch (Exception e) {
           fail();
       }
    }

    @Test
    public void addPlayer() throws PlayerLimitExceededException {
        gameService.createGame(4);
        Player player = new PlayerImpl("MaxMustermann", mockConnection);
        gameService.addPlayer(player);
        List<Player> playerList = gameService.getPlayerList();
        assertEquals(playerList.size(), 1);
        assertEquals(playerList.get(0), player);
    }

    @Test
    public void checkGameStates() throws PlayerLimitExceededException {
        gameService.createGame(2);
        assertEquals(gameService.getGame().getState(), GameState.INIT);
        gameService.startGame();
        assertEquals(gameService.getGame().getState(), GameState.STARTED);
        gameService.nextLab();
        assertEquals(gameService.getGame().getState(), GameState.LAB1);
        gameService.nextLab();
        assertEquals(gameService.getGame().getState(), GameState.LAB2);
        gameService.nextLab();
        assertEquals(gameService.getGame().getState(), GameState.LAB3);
    }

    @Test
    public void checkPlayerCards() throws PlayerLimitExceededException{
        // 52 - 4 cards per player
        int players = 4;
        int cardsPerPlayer = 52 / players;
        gameService.createGame(4);
        Card[][] cardStack = gameService.getPlayercardList();

        assertEquals(cardStack.length, players);
        assertEquals(cardStack[0].length, 4);
        assertEquals(cardStack[1].length, 4);
        assertEquals(cardStack[2].length, 4);
        assertEquals(cardStack[3].length, 4);

    }

    /**
     * Destroy object to support garbage collector.
     */
    @After
    public void destroy() {
        gameService = null;
    }

}
