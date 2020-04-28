package at.aau.server.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import at.aau.server.database.Table.Score;
import at.aau.server.database.Table.User;

public class Database {

    private static final String url = "jdbc:sqlite:./database.db";

    private static Database db;
    private Connection connection;


    protected static Database getInstance(){
        if(db==null){
            db = new Database();
        }
        db.connect();
        db.createUserTable();
        db.createScoreTable();
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
                + "	userid integer NOT NULL,\n"
                + "	score integer NOT NULL\n"
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
    private int runStatementWithReturnID(String sqlStatement){
        int key=-1;
        try (
                PreparedStatement statement = connection.prepareStatement(sqlStatement,
                        Statement.RETURN_GENERATED_KEYS)
        ) {
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    key=generatedKeys.getInt(1);
                }
                else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return key;
    }
    private void runStatementWithReturnList(String sqlStatement){
        try{
            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery(sqlStatement);
            while (res.next()) {
                int id = res.getInt("id");
                String s = res.getString("name");
                System.out.println(id + "\t\t" + s);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }
    protected User addUser(String mac, String name){
        String sql= "INSERT INTO users (mac, name)\n" +
                "VALUES('"+mac+"', '"+name+"');";

        return new User(runStatementWithReturnID(sql), mac, name);
    }
    protected void deleteUser(int id){
        String sql= "DELETE FROM users\n" +
                    "WHERE id="+id+";";
    }
    protected void getAllUsers(){

    }
    protected void getBestUser(){

    }
    protected User getUserByMAC(String mac){
        String sql= "SELECT id FROM users\n" +
                    "WHERE mac="+mac+";";
            return new User(1,"","");
    }
    protected User getUserByID(){
        return null;
    }
    protected Score addScore(int userid, int score){
        String sql= "INSERT INTO scores (userid, score)\n" +
                "VALUES('"+userid+"', '"+score+"');";
        runStatement(sql);
        return new Score(userid, score);
    }
    protected Score getScore(){
        return null;
    }
    protected void getAllScores(){

    }
}
