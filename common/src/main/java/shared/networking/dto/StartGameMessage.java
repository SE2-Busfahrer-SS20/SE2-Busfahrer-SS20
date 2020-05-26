package shared.networking.dto;


import shared.model.PlayerDTO;

import java.util.List;

public class StartGameMessage extends BaseMessage {
    private List<PlayerDTO> playerList;

    public StartGameMessage(List<PlayerDTO> playerList) {
        this.playerList = playerList;
    }

    public StartGameMessage(){}

    public List<PlayerDTO> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<PlayerDTO> playerList) {
        this.playerList = playerList;
    }
}
