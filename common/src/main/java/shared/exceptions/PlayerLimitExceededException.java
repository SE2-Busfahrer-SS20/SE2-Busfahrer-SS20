package shared.exceptions;

public class PlayerLimitExceededException extends Exception {
    public PlayerLimitExceededException(String message, Throwable cause) {
        super(message, cause);
    }
    public PlayerLimitExceededException(String message) {
        super(message);
    }
    public PlayerLimitExceededException() {
        super("PlayersCount must be between 2 and 8 Players.");
    }
}
