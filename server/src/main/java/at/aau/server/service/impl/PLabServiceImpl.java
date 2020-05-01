package at.aau.server.service.impl;

import at.aau.server.service.PLabService;
import shared.model.Game;
import shared.model.Player;
import shared.networking.dto.StartPLabMessage;

public class PLabServiceImpl implements PLabService {

    private Game game;

    public PLabServiceImpl(Game game) {
        this.game = game;
    }

    /**
     * Start Lab and send 10 PLab Cards to every Player.
     */
    @Override
    public void startLab() {
        for (Player p : game.getPlayerList()) {
            p.getConnection().sendTCP(
                        new StartPLabMessage(this.game.getpCards())
            );
        }
    }

}
