package shared.model.impl;

import shared.model.Game;
import shared.model.GameState;

public class GameImpl implements Game {

    private GameState state;

    public GameImpl() {
        this.state = GameState.INIT;
    }

    @Override
    public GameState getState() {
        return state;
    }

    @Override
    public void setState(GameState state) {
        this.state = state;
    }

}
