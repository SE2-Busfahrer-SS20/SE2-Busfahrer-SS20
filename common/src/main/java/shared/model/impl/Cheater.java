package shared.model.impl;

public class Cheater {

    /**
     * This Class identifies a Player who cheated.
     *
     * @Param playerId
     * @Param cheated True if player cheated
     * @Param timeStamp Time when cheat event happened
     * @Param cheatType identifies the type, 5 = Shake, 1 = Light
     * @Param playerName
     */

    private int playerId;
    private boolean cheated;
    private long timeStamp;
    private int cheatType;
    private String playerName;

    public Cheater(int playerId, boolean cheated, long timeStamp, int cheatType, String playerName) {
        this.playerId = playerId;
        this.cheated = cheated;
        this.timeStamp = timeStamp;
        this.cheatType = cheatType;
        this.playerName = playerName;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public boolean isCheated() {
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

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}