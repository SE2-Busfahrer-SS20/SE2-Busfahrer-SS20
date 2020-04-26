package shared.networking.dto;

public class TextMessage extends BaseMessage {

    private String text;

    public TextMessage() { }
    public TextMessage(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return String.format("TextMessage: %s", text);
    }
}
