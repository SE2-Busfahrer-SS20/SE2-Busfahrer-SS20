package at.aau.server.service;

import com.esotericsoftware.kryonet.Connection;

public interface PLapService {
    void startLab(Connection connection);

    void finishLab(String playerName, int points);

    /**
     * Start PLab Service and provided Listeners.
     */
    void start();

    /**
     * send message to all players. An boolean identifies the looser.
     */
    void updatePlayers();
}

