package shared.networking.dto;

public class RegisterMessage extends BaseMessage {

    public RegisterMessage() {
        super(Action.REGISTER_PLAYER);
    }

}
