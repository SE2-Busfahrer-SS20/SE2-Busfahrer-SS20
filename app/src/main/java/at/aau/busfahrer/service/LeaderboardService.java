package at.aau.busfahrer.service;

import shared.model.PlayerDTO;
import shared.networking.Callback;

import java.util.List;

public interface LeaderboardService {
    void updateScoreList();
    void registerPlayerListCallback(Callback<List<PlayerDTO>> callback);
    void connect();
    String getHostname();
    void setHostname(String hostname);
    void disconnect();
}
