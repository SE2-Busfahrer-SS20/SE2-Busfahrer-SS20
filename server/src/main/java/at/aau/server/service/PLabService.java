package at.aau.server.service;

import com.esotericsoftware.kryonet.Connection;

public interface PLabService {
    void startLab(Connection connection);
    void finishLab();
}

