package shared.networking.dto;

public class RegisterMessage extends BaseMessage {

    private String playerName;
    private String MacAddress;

    public RegisterMessage() {}
    public RegisterMessage(String playerName, String MACAdress) {
        this.playerName = playerName;
        this.MacAddress = MACAdress;
    }

    public String getPlayerName() {
        return playerName;
    }
    public String getMacAddress(){return MacAddress; }

}
