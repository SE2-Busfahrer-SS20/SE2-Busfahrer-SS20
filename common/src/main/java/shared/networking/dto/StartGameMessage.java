package shared.networking.dto;


import java.util.List;

import shared.model.Player;

public class StartGameMessage extends BaseMessage {
    private List<Player> playerList;

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {

        this.playerList = playerList;
    }

    /*private List<PlayerDTO>players;
    setPlayerList(List<Player> playerList) {

        for(Player p: playerList) {
            this,players.add(newPlayerDTO(p.getName(), p.getScore())
        }
    }*/
}
