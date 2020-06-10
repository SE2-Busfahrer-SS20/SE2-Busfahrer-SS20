package shared.model;

public interface PlayerDTO {
    boolean isBusdriver();
    void setBusdriver();
    String getName();
    Integer getScore();
    boolean isCheating();
    void setCheating(boolean cheated);
    void setScore(Integer score);
    String getMAC();
    void setMAC(String mac);
}
