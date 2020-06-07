package shared.networking.dto;

public class WinnerLooserMessage extends BaseMessage {

    private boolean isLooser;

    public void setIsLooser(boolean lost) {
        this.isLooser = lost;
    }
    public boolean getIsLooser() {
        return isLooser;
    }
}
