package shared.networking.dto;


import java.util.List;

public class StartGameMessage extends BaseMessage {
    private List<String> playerList;

    public StartGameMessage(){}

    public StartGameMessage(List<String> playerList){
        this.playerList = playerList;
    }

    public List<String> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<String> playerList) {
        this.playerList = playerList;
    }

}
