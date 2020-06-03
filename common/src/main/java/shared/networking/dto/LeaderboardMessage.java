package shared.networking.dto;

import shared.model.PlayerDTO;

import java.util.List;

public class LeaderboardMessage extends BaseMessage{
    private List<PlayerDTO> playerList;

    public LeaderboardMessage() {

    }
    public LeaderboardMessage(List<PlayerDTO> playerList) {
        this.playerList = playerList;
    }

    public List<PlayerDTO> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<PlayerDTO> playerList) {
        this.playerList = playerList;
    }



}
