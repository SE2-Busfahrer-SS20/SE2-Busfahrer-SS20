package shared.networking.dto;

public class ServerActionResponse extends TextMessage {

    private boolean result;

    public ServerActionResponse(String text, boolean result) {
        super("text");
        this.result = result;
    }

    public boolean getActionResult() {
        return result;
    }

}
