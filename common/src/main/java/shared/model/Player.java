package shared.model;

import com.esotericsoftware.kryonet.Connection;

public interface Player {


    String getName();
    Connection getConnection();
    int getCount();
    void setCount(int newCount);

}
