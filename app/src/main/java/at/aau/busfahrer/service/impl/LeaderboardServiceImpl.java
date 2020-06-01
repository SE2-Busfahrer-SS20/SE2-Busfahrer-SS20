package at.aau.busfahrer.service.impl;

import at.aau.busfahrer.service.LeaderboardService;
import com.esotericsoftware.minlog.Log;
import shared.model.PlayerDTO;
import shared.networking.Callback;
import shared.networking.NetworkClient;
import shared.networking.dto.CheatedMessage;
import shared.networking.dto.LeaderboardMessage;
import shared.networking.dto.RegisterMessage;
import shared.networking.kryonet.NetworkClientKryo;

import java.util.List;

public class LeaderboardServiceImpl implements LeaderboardService{
    private static LeaderboardService instance;
    private final NetworkClient client;
    private List<String> ScoreList;
    private String host;
    private Callback<List<PlayerDTO>> playerCallback;

    private LeaderboardServiceImpl(){
        client = NetworkClientKryo.getInstance();
        this.host = "192.168.0.105"; // set default HostName value.
    }
    public static LeaderboardService getInstance() {
        if (instance == null) {
            instance = new LeaderboardServiceImpl();
        }
        return instance;
    }
    @Override
    public void connect(){
        Thread thread = new Thread(() -> {
            LeaderboardMessage lbm = new LeaderboardMessage();
            try {
                client.connect(host);
                client.sendMessage(lbm);
            } catch (Exception e) {
                Log.error(e.toString());
            }
        });
        thread.start();
    }
    @Override
    public void updateScoreList(){
        System.out.println("updatescorelist");
        this.client.registerCallback(LeaderboardMessage.class, msg ->{
            List<PlayerDTO> playerList =((LeaderboardMessage)msg).getPlayerList();
            playerCallback.callback(playerList);
        });
        /*Thread startThread = new Thread(() -> {
            try {
                System.out.println("Leaderboard was triggered");
                this.client.sendMessage(new LeaderboardMessage());
            } catch (Exception e) {
                System.out.println("Leaderboardmessage Fail!");
            }
        });
        startThread.start();*/

/*        Thread thread = new Thread(() -> {
            LeaderboardMessage lbm = new LeaderboardMessage();

            try {
                client.connect(host);
                client.sendMessage(lbm);
            } catch (Exception e) {
                Log.error(e.toString());
            }
        });
        thread.start();
        System.out.println("Client LeaderboardMessage sent!");*/
    }

    @Override
    public void registerPlayerListCallback(Callback<List<PlayerDTO>> callback) {
        this.playerCallback=callback;
    }
}
