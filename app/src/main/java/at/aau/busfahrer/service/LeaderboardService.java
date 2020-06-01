package at.aau.busfahrer.service;

import shared.model.PlayerDTO;
import shared.networking.Callback;

import java.util.List;

public interface LeaderboardService {
    public void updateScoreList();
    void registerPlayerListCallback(Callback<List<PlayerDTO>> callback);
    public void connect();
}
