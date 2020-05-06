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
    int getTempID();
    void setTempID(int ID);


}
