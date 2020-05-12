package at.aau.server.service.impl;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

import java.util.ArrayList;
import java.util.List;

import at.aau.server.GameServer;
import at.aau.server.service.GameService;
import at.aau.server.service.PLapService;
import shared.model.Player;
import shared.networking.dto.StartPLabMessage;


/**
 * PLab Service contains the game logic for the "Pyramiden Runde".
 * @Version: 1.0
 * @Author: Gerold Auer
 */
public class PLapServiceImpl implements PLapService {

    private GameServer server;
    private GameService gameService;

    public PLapServiceImpl(GameServer server) {
        this.server = server;
        this.gameService = GameServiceImpl.getInstance();
    }

    /**
     * Start Lab and send 10 PLab Cards to every Player.
     */
    @Override
    public void startLab(Connection connection) {
        if(this.gameService.getGame() != null) {
            Log.info("PLAb Started", "start send message to " + connection.toString() );
            connection.sendTCP(
                    new StartPLabMessage(this.gameService.getGame().getpCards(), getPlayerNames())
            );
        } else {
            Log.error("Game Object is null!, You need to start a game first.");
        }
    }

    @Override
    public void finishLab() {

    }

    @Override
    public void start() {
        addListener();
    }

    private List<String> getPlayerNames() {
        List<String> playerNames = new ArrayList<>();
        for (Player p : this.gameService.getGame().getPlayerList()) {
            playerNames.add(p.getName());
        }
        return playerNames;
    }

    private void addListener() {
        server.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof StartPLabMessage) {
                    Log.debug("PLab started for Connection: ", connection.toString());
                    startLab(connection);
                }
            }
        });
    }

}
