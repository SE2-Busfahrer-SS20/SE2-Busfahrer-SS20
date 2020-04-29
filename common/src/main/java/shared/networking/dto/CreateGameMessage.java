package shared.networking.dto;

public class CreateGameMessage extends BaseMessage{

    private String gameName;
    private int playerCount;

    public CreateGameMessage() {}
    public CreateGameMessage(int playerCount) {
        this.playerCount = playerCount;
    }

    public int getPlayerCount() {
        return playerCount;
    }

}
