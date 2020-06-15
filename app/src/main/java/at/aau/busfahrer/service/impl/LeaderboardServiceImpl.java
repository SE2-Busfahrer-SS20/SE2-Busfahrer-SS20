package at.aau.busfahrer.service.impl;

import at.aau.busfahrer.service.LeaderboardService;
import com.esotericsoftware.minlog.Log;
import shared.model.PlayerDTO;
import shared.networking.Callback;
import shared.networking.NetworkClient;
import shared.networking.dto.LeaderboardMessage;
import shared.networking.kryonet.NetworkClientKryo;

import java.util.List;

public class LeaderboardServiceImpl implements LeaderboardService{
    private static LeaderboardService instance;
    private NetworkClient client;
    private String hostname;
    private Callback<List<PlayerDTO>> playerCallback;
    Thread thread;
    @Override
    public String getHostname() {
        return hostname;
    }
    @Override
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
    private LeaderboardServiceImpl(){
        this.hostname = "127.0.0.1"; // set default HostName value.
    }
    public static LeaderboardService getInstance() {
        if (instance == null) {
            instance = new LeaderboardServiceImpl();
        }
        return instance;
    }
    public static void destroyInstance() {
        instance = null;
    }
    @Override
    public void connect(){
        client = NetworkClientKryo.getInstance();
        thread = new Thread(() -> {
            LeaderboardMessage lbm = new LeaderboardMessage();
            try {
                client.connect(hostname);
                client.sendMessage(lbm);
            } catch (Exception e) {
                Log.error(e.toString());
            }
        });
        thread.start();
    }
    @Override
    public void updateScoreList(){
        this.client.registerCallback(LeaderboardMessage.class, msg ->{
            List<PlayerDTO> playerList =((LeaderboardMessage)msg).getPlayerList();
            playerCallback.callback(playerList);
        });
    }
    @Override
    public void disconnect(){
        client.close();
        try {
            thread.join();
            thread=null;
        }
        catch (Exception e){
        Log.error("Error in LeaderboardService!", e);
        }
    }
    @Override
    public void registerPlayerListCallback(Callback<List<PlayerDTO>> callback) {
        this.playerCallback=callback;
    }
}
