package shared.model;

public interface PlayerDTO {
    String getName();
    Integer getScore();
    boolean isCheating();
    void setCheating(boolean cheated);
}
