package shared.model.impl;

import shared.model.PlayerDTO;

public class PlayerDTOImpl implements PlayerDTO {

    private String name;
    private Integer score;
    private boolean isCheating;

    public PlayerDTOImpl(String name, Integer score, boolean isCheating) {
        this.name = name;
        this.score = score;
        this.isCheating = isCheating;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public void setCheating(boolean cheating) {
        isCheating = cheating;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Integer getScore() {
        return null;
    }

    @Override
    public boolean isCheating() {
        return false;
    }
}
