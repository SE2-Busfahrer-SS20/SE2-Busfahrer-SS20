package shared.networking.dto;

public class RegisterMessage extends BaseMessage {

    private String playerName;
    private String MACAdress;

    public RegisterMessage() {}
    public RegisterMessage(String playerName, String MACAdress) {
        this.playerName = playerName;
        this.MACAdress= MACAdress;
    }

    public String getPlayerName() {
        return playerName;
    }
    public String getMACAdress(){return MACAdress; }

}
