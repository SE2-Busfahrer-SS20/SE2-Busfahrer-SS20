package at.aau.server.service;

import com.esotericsoftware.kryonet.Connection;

public interface PLapService {
    void startLab(Connection connection);

    void finishLab();

    /**
     * Start PLab Service and provided Listeners.
     */
    void start();
}

