package shared.networking.dto;

public class DealPointsMessage extends BaseMessage{

    private int points;
    private String destPlayerName;

    public DealPointsMessage() {}
    public DealPointsMessage(String destPlayerName, int points) {
        this.points = points;
        this.destPlayerName = destPlayerName;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getDestPlayerName() {
        return destPlayerName;
    }

    public void setDestPlayerName(String destPlayerName) {
        this.destPlayerName = destPlayerName;
    }
}
