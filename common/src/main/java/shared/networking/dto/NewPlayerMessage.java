package shared.networking.dto;

public class NewPlayerMessage extends BaseMessage {
    private String playerName;

    public NewPlayerMessage(){ }

    public NewPlayerMessage(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
