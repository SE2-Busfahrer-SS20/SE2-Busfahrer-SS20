package at.aau.server.service;

import at.aau.server.GameServer;
import at.aau.server.service.impl.GameServiceImpl;
import at.aau.server.service.impl.PLapServiceImpl;
import com.esotericsoftware.kryonet.Connection;
import org.junit.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import shared.model.Player;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;

public class PLabServiceTest {


        private PLapService pLapService;
        private GameService gameService;

        @Mock
        GameServer mockGameServer;
        @Mock
        Connection mockConnection;


        @Rule
        public MockitoRule mockitoRule = MockitoJUnit.rule();
        /**
         * Init instance of object.
         */
        @Before
        public void init() throws Exception {
                gameService = GameServiceImpl.getInstance();
                gameService.createGame("master", "AA:AA:AA:AA", mockConnection);
                pLapService = new PLapServiceImpl(mockGameServer);
                gameService.getGame().addPlayer("player1", "AA:BB:CC:DD", mockConnection);
                gameService.getGame().addPlayer("player2", "AA:BB:CC:DD", mockConnection);
                gameService.getGame().addPlayer("player3", "AA:BB:CC:DD", mockConnection);
                gameService.getGame().addPlayer("player4", "AA:BB:CC:DD", mockConnection);
        }

        @Test
        public void checkGetPlayerNames() {
                List<String> playerList = ((PLapServiceImpl)pLapService).getPlayerNames();
                assertEquals(playerList.size(), gameService.getPlayerList().size());
                assertEquals(playerList, getStringPlayerList());
        }
        @Test
        public void checkPlayerFinished() {
                Assert.assertFalse(((PLapServiceImpl)pLapService).lapFinished());
                for(int i = 0; i <= 4; i++)
                        gameService.getGame().playerFinishedPLap();
                Assert.assertTrue(((PLapServiceImpl)pLapService).lapFinished());
        }

        @Test
        public void testStartLab() {
                pLapService.startLab(mockConnection);
                Mockito.verify(mockConnection, atLeastOnce()).sendTCP(any());
        }

        private List<String> getStringPlayerList() {
                List<String> list = new ArrayList<>();
                for(Player p : gameService.getGame().getPlayerList()) {
                        list.add(p.getName());
                }
                return list;
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
