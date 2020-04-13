package shared.model;

public interface Game {

    GameState getState();
    void setState(GameState state);
}
