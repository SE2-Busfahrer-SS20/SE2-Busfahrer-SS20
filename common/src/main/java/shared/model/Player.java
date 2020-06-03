package shared.model;

import com.esotericsoftware.kryonet.Connection;

public interface Player {

    String getName();
    String getMACAdress();

    Card[] getCards();
    Card getCard(int index);

    Connection getConnection();

    int getScore();
    void setScore(int newCount);
    void addPoints(int points);

    int getTempID();
    void setTempID(int id);

    boolean isCheated();
    void setCheated(boolean cheated);

    boolean isCheatedThisRound();
    void setCheatedThisRound(boolean cheatedThisRound);
}
