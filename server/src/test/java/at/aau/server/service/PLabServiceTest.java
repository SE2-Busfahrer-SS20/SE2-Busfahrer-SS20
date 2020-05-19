package at.aau.server.service;

import at.aau.server.GameServer;
import at.aau.server.service.impl.GameServiceImpl;
import at.aau.server.service.impl.PLapServiceImpl;
import com.esotericsoftware.kryonet.Connection;
import org.junit.*;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import shared.model.Player;
import shared.networking.dto.WinnerLooserMessage;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;

public class PLabServiceTest {


        private PLapService pLapService;
        private GameService gameService;

        @Mock
        GameServer mockGameServer;
        @Mock
        Connection mockConnection, looserConnection, winnerConnection;


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
                gameService.getGame().addPlayer("player2", "AA:BB:CC:DD", looserConnection);
                gameService.getGame().addPlayer("player3", "AA:BB:CC:DD", mockConnection);
                gameService.getGame().addPlayer("player4", "AA:BB:CC:DD", winnerConnection);
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

        @Test
        public void checkPlayerFinishedLab() {
                doFinishingSteps();
                List<Player> list = gameService.getGame().getPlayerList();

                Assert.assertEquals(10, list.get(1).getScore());
                Assert.assertEquals(50, list.get(2).getScore());
                Assert.assertEquals(5, list.get(3).getScore());
                Assert.assertEquals(8, list.get(4).getScore());

                Assert.assertNotEquals( 10, list.get(2).getScore());
        }
        @Test
        public void checkUpdatePlayers() {
               doFinishingSteps();
               pLapService.updatePlayers();
               List<Player> list = gameService.getGame().getPlayerList();
               Assert.assertEquals(5, list.get(1).getScore());
               Assert.assertEquals("player3", list.get(1).getName());
               Assert.assertEquals(50, list.get(4).getScore());
               Assert.assertEquals("player2", list.get(4).getName());
               Mockito.verify(winnerConnection, times(1)).sendTCP(argThat(new MessageMatcherWinner()));
               Mockito.verify(looserConnection, times(1)).sendTCP(argThat(new MessageMatcherLooser()));

        }

        private void doFinishingSteps() {
                pLapService.finishLab("player1", 10);
                pLapService.finishLab("player2", 50);
                pLapService.finishLab("player3", 5);
                pLapService.finishLab("player4", 8);
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

class MessageMatcherLooser implements ArgumentMatcher<WinnerLooserMessage> {

        @Override
        public boolean matches(WinnerLooserMessage argument) {
                return argument.getIsLooser();
        }
}

class MessageMatcherWinner implements ArgumentMatcher<WinnerLooserMessage> {

        @Override
        public boolean matches(WinnerLooserMessage argument) {
                return !argument.getIsLooser();
        }
}
