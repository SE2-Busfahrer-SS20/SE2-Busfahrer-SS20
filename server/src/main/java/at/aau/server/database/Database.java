package at.aau.server.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import at.aau.server.database.Table.Score;
import at.aau.server.database.Table.User;

public class Database {

    private static final String url = "jdbc:sqlite:./database.db";

    private static Database db;
    private Connection connection;


    public static Database getInstance(){
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
        try {
            runPreparedStatement(sql, null);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    private void createScoreTable() {
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS scores (\n"
                + "	userid integer NOT NULL,\n"
                + "	score integer NOT NULL\n"
                + ");";
        try {
            runPreparedStatement(sql, null);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    private int runPreparedStatement(String statement, String[] params, boolean returnKEY) throws SQLException {
        int key=-1;
        PreparedStatement addUser= connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
        for(int count=0;params!=null &&count<params.length;count++){
            addUser.setString(count+1, params[count]);
        }

        int affectedRows = addUser.executeUpdate();
        //if (affectedRows == 0) {
        //    throw new SQLException("Query failed, no rows affected.");
        //}
        try (ResultSet generatedKeys = addUser.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                key=generatedKeys.getInt(1);
            }
            else if(returnKEY){
                throw new SQLException("Creating failed, no ID obtained.");
            }
        }
        return key;
    }
    private void runPreparedStatement(String statement, String[] params) throws SQLException{
        runPreparedStatement(statement, params, false);
    }
    private ResultSet runPreparedStatementReturnList(String statement, String[] params){
        try{
            PreparedStatement preparedStatement= connection.prepareStatement(statement);

            for(int count=0;params != null && count<params.length;count++){
                preparedStatement.setString(count+1, params[count]);
            }
            return preparedStatement.executeQuery();

        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public User addUser(String mac, String name){
        try {
            return new User(runPreparedStatement("INSERT INTO users (mac, name)\n" +
                    "VALUES(?, ?);", new String[]{mac, name}, true), mac, name);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public void deleteUser(int id){
        try {
            runPreparedStatement(
                    "DELETE FROM users\n" +
                            "WHERE id=?;",
                    new String[]{String.valueOf(id)});
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public List<User> getAllUsers(){
        String sql="SELECT * FROM users;";
        List<User> allUsers= new ArrayList<>();
        try {
            ResultSet res = runPreparedStatementReturnList(sql, null);
            while (res.next()) {
                allUsers.add(new User(res.getInt("id"),res.getString("mac"), res.getString("name")));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return allUsers;
    }
    public User getBestUser(){
        try {
            ResultSet res= runPreparedStatementReturnList("SELECT * FROM scores ORDER BY userid DESC LIMIT 1;", null);
            while (res.next()){
                return getUserByID(res.getInt("userid"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    return null;
    }
    public User getUserByMAC(String mac){
        try {
            ResultSet res = runPreparedStatementReturnList("SELECT * FROM users\n" +
                    "WHERE mac=?;", new String[]{mac});
            while (res.next()) {
                return new User(res.getInt("id"), res.getString("mac"), res.getString("name"));
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
    public User getUserByID(int id){
        try {
            ResultSet res = runPreparedStatementReturnList("SELECT * FROM users\n" +
                    "WHERE id=?;", new String[]{String.valueOf(id)});
            while (res.next()) {
                return new User(id, res.getString("mac"), res.getString("name"));
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
    public Score addScore(int userid, int score){
        try {
            runPreparedStatement("INSERT INTO scores (userid, score)\n" +
                    "VALUES(?, ?);", new String[]{String.valueOf(userid), String.valueOf(score)});
            return new Score(userid,score);
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
    public List getAllScores(int id){
        List scores= new ArrayList();
        try {
            ResultSet res =runPreparedStatementReturnList("SELECT score FROM scores WHERE userid = ?;", new String[]{String.valueOf(id)});
            while (res.next()){
                scores.add(res.getInt("score"));
            }
            return scores;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public int getNumberOfAllScores() {
        String sql =    "SELECT COUNT(*) AS rowcount FROM scores";
        try{
            ResultSet res= runPreparedStatementReturnList(sql, null);
            while (res.next()){
                return res.getInt("rowcount") ;
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

        return -1;
    }
    public int getNumberOfAllUsers(){
        String sql =    "SELECT COUNT(*) AS rowcount FROM users";
        try{
            ResultSet res= runPreparedStatementReturnList(sql, null);
            while (res.next()){
                return res.getInt("rowcount") ;
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

        return -1;
    }
    public void emptyUserTable(){
        String sql="DELETE FROM users;";
        try {
            runPreparedStatement(sql,null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void emptyScoreTable(){
        String sql="DELETE FROM scores;";
        try {
            runPreparedStatement(sql,null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
