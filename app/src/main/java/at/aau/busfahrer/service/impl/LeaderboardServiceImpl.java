package at.aau.busfahrer.service.impl;

import at.aau.busfahrer.service.LeaderboardService;
import com.esotericsoftware.minlog.Log;
import shared.networking.NetworkClient;
import shared.networking.dto.CheatedMessage;
import shared.networking.dto.LeaderboardMessage;
import shared.networking.kryonet.NetworkClientKryo;

import java.util.List;

public class LeaderboardServiceImpl implements LeaderboardService{
    private static LeaderboardService instance;
    private final NetworkClient client;
    private List<String> ScoreList;
    private String host;

    private LeaderboardServiceImpl(){
        client = NetworkClientKryo.getInstance();;
        this.host = "10.0.0.1"; // set default HostName value.
    }
    public static LeaderboardService getInstance() {
        if (instance == null) {
            instance = new LeaderboardServiceImpl();
        }
        return instance;
    }
    public void updateScoreList(){
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
        System.out.println("Client LeaderboardMessage sent!");
    }
}
