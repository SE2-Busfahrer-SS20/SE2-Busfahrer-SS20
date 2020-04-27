package at.aau.server.database.Table;

import java.sql.Connection;

public class Score {
    private Connection connection;
    public Score(Connection connection) {
        this.connection=connection;
    }
    public void getData(){
        System.out.println("SCOREDATA");
    }
}
