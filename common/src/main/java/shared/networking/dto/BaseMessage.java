package shared.networking.dto;

public abstract class BaseMessage {

    private Action action;

    public BaseMessage(Action action) {
        this.action = action;
    }

    public Action getAction() {
        return action;
    }
}
