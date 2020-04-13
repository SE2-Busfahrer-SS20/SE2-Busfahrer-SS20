package shared.networking.dto;

public class CreateGameMessage extends BaseMessage{

    private String gameName;
    private int playerCount;

    public CreateGameMessage(int playerCount, String gameName) {
        super(Action.CREATE_GAME);
        this.gameName = gameName;
        this.playerCount = playerCount;
    }

    public String getGameName() {
        return gameName;
    }

    public int getPlayerCount() {
        return playerCount;
    }
}
