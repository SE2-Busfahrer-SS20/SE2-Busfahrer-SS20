package shared.networking.dto;

public class RegisterMessage extends BaseMessage {

    private String playerName;
    private String MACAddress;

    public RegisterMessage() {}
    public RegisterMessage(String playerName, String MACAdress) {
        this.playerName = playerName;
        this.MACAddress = MACAdress;
    }

    public String getPlayerName() {
        return playerName;
    }
    public String getMACAddress(){return MACAddress; }

}
