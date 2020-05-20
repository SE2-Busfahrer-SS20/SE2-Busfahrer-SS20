package shared.networking.dto;


import java.util.ArrayList;

public class StartGameMessage extends BaseMessage {
    private ArrayList<String> playerList;

    public StartGameMessage() {
        this.playerList =new ArrayList<String>();
    }

    public ArrayList<String> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(ArrayList<String> playerList) {
        this.playerList = playerList;
    }
}
