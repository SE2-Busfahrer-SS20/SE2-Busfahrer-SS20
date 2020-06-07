package at.aau.busfahrer;

import at.aau.busfahrer.service.LeaderboardService;
import at.aau.busfahrer.service.impl.LeaderboardServiceImpl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import shared.model.PlayerDTO;
import shared.networking.NetworkClient;
import shared.networking.kryonet.NetworkClientKryo;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LeaderboardServiceImplTest {


    @Mock
    private NetworkClient client;

    private LeaderboardService leaderboardService;
    String hostname;

    @Before
    public void setUp() {
        this.leaderboardService = LeaderboardServiceImpl.getInstance();
        hostname="127.0.0.1";
        MockitoAnnotations.initMocks(this);

    }
    @After
    public void destroy(){
        leaderboardService = null;
        LeaderboardServiceImpl.destroyInstance();
    }
    @Test
    public void getLeaderboardInstanceTest(){
        destroy();
        Assert.assertNull(leaderboardService);
        this.leaderboardService = LeaderboardServiceImpl.getInstance();
        Assert.assertNotNull(leaderboardService);
    }
    @Test
    public void HostnameTest(){
        leaderboardService.setHostname(hostname);
        Assert.assertTrue(leaderboardService.getHostname().equals(hostname));
    }
    @Test
    public void connectTest(){

        try{
            leaderboardService.connect();
        }catch (Exception e){
            Mockito.verify(client, atLeastOnce()).sendMessage(any());
        }
    }
    @Test
    public void updateScoreListTest(){
        leaderboardService.connect();
        try{
            leaderboardService.updateScoreList();

        }catch (Exception e){
            Mockito.verify(client, atLeastOnce()).registerCallback(any(),any());
        }
    }
    @Test
    public void disconnectTest(){

        try{
            client=NetworkClientKryo.getInstance();
            leaderboardService.setHostname(hostname);
            leaderboardService.connect();
            leaderboardService.registerPlayerListCallback(playerList -> playerDTOCallbackFunction(playerList));
            leaderboardService.disconnect();

        }catch (Exception e){
            e.printStackTrace();
            verify(client, atLeastOnce()).close();

        }


    }
    private void playerDTOCallbackFunction(List<PlayerDTO> playerList){

    }
}
