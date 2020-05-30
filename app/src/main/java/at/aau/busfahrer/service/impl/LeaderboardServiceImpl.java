package at.aau.busfahrer.service.impl;

import at.aau.busfahrer.service.LeaderboardService;
import shared.networking.NetworkClient;
import shared.networking.kryonet.NetworkClientKryo;

import java.util.List;

public class LeaderboardServiceImpl implements LeaderboardService{
    private static LeaderboardService instance;
    private final NetworkClient client = NetworkClientKryo.getInstance();
    private List<String> ScoreList;

    public static LeaderboardService getInstance() {
        if (instance == null) {
            instance = new LeaderboardServiceImpl();
        }
        return instance;
    }

}
