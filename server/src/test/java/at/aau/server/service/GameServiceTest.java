package at.aau.server.service;

import com.esotericsoftware.kryonet.Connection;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.List;

import at.aau.server.service.GameService;
import at.aau.server.service.impl.GameServiceImpl;
import shared.model.GameState;
import shared.model.Player;
import shared.model.impl.PlayerImpl;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class GameServiceTest {

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
    public void addPlayer() {
        gameService.createGame(4);
        Player player = new PlayerImpl("MaxMustermann", mockConnection);
        gameService.addPlayer(player);
        List<Player> playerList = gameService.getPlayerList();
        assertEquals(playerList.size(), 1);
        assertEquals(playerList.get(0), player);
    }

    @Test
    public void checkGameStates() {
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


    /**
     * Destroy object to support garbage collector.
     */
    @After
    public void destroy() {
        gameService = null;
    }

}
