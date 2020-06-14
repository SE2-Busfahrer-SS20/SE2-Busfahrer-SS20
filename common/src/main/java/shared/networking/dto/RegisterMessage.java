package shared.networking.dto;

public class RegisterMessage extends BaseMessage {

    private String playerName;
    private String MacAddress;

    public RegisterMessage() {}
    public RegisterMessage(String playerName, String macAddress) {
        this.playerName = playerName;
        this.MacAddress = macAddress;
    }

    public String getPlayerName() {
        return playerName;
    }
    public String getMacAddress(){return MacAddress; }

}
