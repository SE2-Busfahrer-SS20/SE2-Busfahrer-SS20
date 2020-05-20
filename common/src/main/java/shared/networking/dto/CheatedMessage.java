package shared.networking.dto;

public class CheatedMessage extends BaseMessage {

    private int playerId;
    private boolean cheated;
    private long timeStamp;
    private int cheatType;

    public CheatedMessage(){}

    public CheatedMessage(int playerId, boolean cheated, long timeStamp, int cheatType) {
        this.playerId = playerId;
        this.cheated = cheated;
        this.timeStamp = timeStamp;
        this.cheatType = cheatType;
    }

    public int getTempID() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public boolean hasCheated() {
        return cheated;
    }

    public void setCheated(boolean cheated) {
        this.cheated = cheated;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getCheatType() {
        return cheatType;
    }

    public void setCheatType(int cheatType) {
        this.cheatType = cheatType;
    }
}
