package at.aau.server.service.impl;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import at.aau.server.GameServer;
import at.aau.server.service.PLabService;
import shared.model.Game;
import shared.model.Player;
import shared.networking.dto.StartPLabMessage;


/**
 * PLab Service contains the game logic for the "Pyramiden Runde".
 * @Version: 1.0
 * @Author: Gerold Auer
 */
public class PLabServiceImpl implements PLabService {

    private Game game;
    private GameServer server;

    public PLabServiceImpl(Game game, GameServer server) {
        this.game = game;
        this.server = server;
    }

    /**
     * Start Lab and send 10 PLab Cards to every Player.
     */
    @Override
    public void startLab() {
        for (Player p : game.getPlayerList()) {
            p.getConnection().sendTCP(
                        new StartPLabMessage(this.game.getpCards(),this.game.getPlayerList() )
            );
        }
    }

    @Override
    public void finishLab() {

    }

    private void addListener() {
        server.addListener(new Listener() {
            public void received(Connection connection, Object object) {
            }
        });
    }

}
