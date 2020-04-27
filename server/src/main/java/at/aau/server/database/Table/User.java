package at.aau.server.database.Table;

import java.sql.Connection;

public class User {
    private Connection connection;
    private int id;
    private String name;
    private Score score;

    public User(Connection connection){
        this.connection=connection;
    }
    public void getData(){
        System.out.println("USERDATA");
    }
}
