package shared.networking.dto;

import shared.model.PlayerDTO;

public class NewPlayerMessage extends BaseMessage {
    private PlayerDTO player;

    public NewPlayerMessage(){ }

    public NewPlayerMessage(PlayerDTO player) {
        this.player= player;
    }

    public PlayerDTO getPlayer() {
        return player;
    }

    public void setPlayer(PlayerDTO player) {
        this.player = player;
    }
}
