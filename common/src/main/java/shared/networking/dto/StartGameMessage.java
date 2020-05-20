package shared.networking.dto;

import java.util.List;

import shared.model.Player;

public class StartGameMessage extends BaseMessage {
    private List<Player> playerList;

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerArrayList) {
        this.playerList = playerList;
    }
}
