package at.aau.server;

import com.esotericsoftware.kryonet.Connection;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.List;

import shared.exceptions.PlayerLimitExceededException;
import shared.model.Card;
import shared.model.GameState;
import shared.model.Player;
import shared.model.impl.PlayerImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class GameServerTest {

    private final static int MAX_PLAYERS = 8;
    private final static int MIN_PLAYERS = 8;

    private GameServer gameServer;
    @Mock
    Connection mockConnection;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    /**
     * Init instance of object.
     */
    @Before
    public void init() {
        gameServer = new GameServer();

    }



    /**
     * Destroy object to support garbage collector.
     */
    @After
    public void destroy() {
        gameServer = null;
    }

}
