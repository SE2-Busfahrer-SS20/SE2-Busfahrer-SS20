package at.aau.server.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import at.aau.server.database.Table.Score;
import at.aau.server.database.Table.User;

public class Database {

    private static final String url = "jdbc:sqlite:./database.db";

    private static Database db;
    private Connection connection;
    protected User user;
    protected Score score;

    protected static Database getInstance(){
        if(db==null){
            db = new Database();
        }
        db.connect();
        db.createTableInstances();
        return db;
    }

    private void connect() {
        if(connection!=null){
            System.out.println("Database already connected!");
            return;
        }
        try {
            connection= DriverManager.getConnection(url);
            if (connection!= null){
                DatabaseMetaData meta = connection.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("Database connected!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private void createTableInstances(){
        user=new User(connection);
        score=new Score(connection);
    }

    private void createUserTable() {
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS users (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	mac text NOT NULL,\n"
                + " name text NOT NULL\n"
                + ");";
        runStatement(sql);
    }
    private void createScoreTable() {
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS scores (\n"
                + "	userid integer,\n"
                + "	score integer,\n"
                + ");";
        runStatement(sql);
    }
    private void runStatement(String sqlStatement){
        try (
            Statement stmt = connection.createStatement()) {
            // create a new table
            stmt.execute(sqlStatement);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
