package at.aau.server.service;

import at.aau.server.GameServer;
import at.aau.server.service.impl.GameServiceImpl;
import at.aau.server.service.impl.PLapServiceImpl;
import com.esotericsoftware.kryonet.Connection;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import shared.exceptions.PlayerLimitExceededException;
import shared.model.GameState;
import shared.model.Player;
import shared.model.impl.PlayerImpl;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class PLabServiceTest {

        private final static int MAX_PLAYERS = 8;
        private final static int MIN_PLAYERS = 8;

        private PLapService pLapService;

        @Mock
        GameServer gameServer;

        @Rule
        public MockitoRule mockitoRule = MockitoJUnit.rule();
        /**
         * Init instance of object.
         */
        @Before
        public void init() {
            pLapService = new PLapServiceImpl(gameServer);
        }

        /*
         * The following code tests basic methods of GameService.
         */
        @Test
        public void testGameExists() {

        }

        /**
         * Destroy Singleton instance to create clean instance.
         */

        @After
        public void destroy() {
            // support garbage collector.
            pLapService = null;
        }

}
