package shared.networking.dto;

import shared.model.PlayerDTO;

import java.util.List;

public class SaveGameDataMessage extends BaseMessage{


    private PlayerDTO player;

    public SaveGameDataMessage() {

    }
    public SaveGameDataMessage(PlayerDTO player) {
        this.player = player;
    }

    public PlayerDTO getPlayer() {
        return player;
    }

    public void setPlayer(PlayerDTO player) {
        this.player = player;
    }

}
