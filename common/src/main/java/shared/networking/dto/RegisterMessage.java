package shared.networking.dto;

public class RegisterMessage extends BaseMessage {

    private String playerName;

    public RegisterMessage(String playerName) {
        super(Action.REGISTER_PLAYER);
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

}
