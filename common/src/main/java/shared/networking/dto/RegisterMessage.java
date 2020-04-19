package shared.networking.dto;

public class RegisterMessage extends BaseMessage {

    private String playerName;

    public RegisterMessage() {}
    public RegisterMessage(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

}
