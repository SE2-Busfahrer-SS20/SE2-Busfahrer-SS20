package at.aau.server.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import at.aau.server.database.Table.Score;
import at.aau.server.database.Table.User;
import com.esotericsoftware.minlog.Log;

public class Database {

    private static final String DB_URL = "jdbc:sqlite:./database.db";

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
            Log.info("Database already connected!");
            return;
        }
        try {
            connection= DriverManager.getConnection(DB_URL);
            DatabaseMetaData meta = connection.getMetaData();
            Log.info("The driver name is " + meta.getDriverName());
            Log.info("Database connected!");
        } catch (SQLException e) {
            Log.error(e.getMessage());
        }
    }
    private void createUserTable() {
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS users (\n"
                + "id integer PRIMARY KEY,\n"
                + "mac text NOT NULL,\n"
                + "name text NOT NULL\n"
                + ");";
        try {
            runPreparedStatement(sql, null);
        }
        catch(Exception e){
            Log.error(e.getMessage());
        }
    }
    private void createScoreTable() {
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS scores (\n"
                + "userid integer NOT NULL,\n"
                + "score integer NOT NULL\n"
                + ");";
        try {
            runPreparedStatement(sql, null);
        }
        catch(Exception e){
            Log.error(e.getMessage());
        }
    }
    private int runPreparedStatement(String statement, String[] params, boolean returnKEY) throws SQLException {
        int key=-1;
        PreparedStatement preparedStatement=null;
        try{
            preparedStatement= connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
            for(int count=0;params!=null &&count<params.length;count++){
                preparedStatement.setString(count+1, params[count]);
            }
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    key=generatedKeys.getInt(1);
                }
                else if(returnKEY){
                    throw new SQLException("Creating failed, no ID obtained.");
                }

            }
        }finally {
            preparedStatement.close();
        }
        return key;
    }
    private void runPreparedStatement(String statement, String[] params) throws SQLException{
        runPreparedStatement(statement, params, false);
    }
    private ResultSet runPreparedStatementReturnList(String statement, String[] params) throws SQLException {
        PreparedStatement preparedStatement= connection.prepareStatement(statement);

        for(int count=0;params != null && count<params.length;count++){
            preparedStatement.setString(count+1, params[count]);
        }
        return preparedStatement.executeQuery();
    }
    public User addUser(String mac, String name) throws SQLException {
        try {
            return new User(runPreparedStatement("INSERT INTO users (mac, name)\n" +
                    "VALUES(?, ?);", new String[]{mac, name}, true), mac, name);
        }catch (Exception e){
            Log.error(e.getMessage());
            throw new SQLException("Could not add user!");
        }
    }
    public void deleteUser(int id) throws SQLException {
        try {
            runPreparedStatement(
                    "DELETE FROM users\n" +
                            "WHERE id=?;",
                    new String[]{String.valueOf(id)});
        }catch (Exception e){
            Log.error(e.getMessage());
            throw new SQLException("Could not delete user!");
        }
    }
    public List<User> getAllUsers() throws SQLException {
        String sql="SELECT * FROM users;";
        List<User> allUsers= new ArrayList<>();

        ResultSet res = runPreparedStatementReturnList(sql, null);
        while (res.next()) {
            allUsers.add(new User(res.getInt("id"),res.getString("mac"), res.getString("name")));
        }
        return allUsers;
    }
    public User getBestUser() throws SQLException {

        ResultSet res= runPreparedStatementReturnList("SELECT * FROM scores ORDER BY score DESC LIMIT 1;", null);

        if (res.next()){
            return getUserByID(res.getInt("userid"));
        }
        throw new SQLException("Could not query best User!");
    }
    public User getUserByMAC(String mac) throws SQLException {

        ResultSet res = runPreparedStatementReturnList("SELECT * FROM users\n" +
                "WHERE mac=?;", new String[]{mac});

        if (res.next()) {
            return new User(res.getInt("id"), res.getString("mac"), res.getString("name"));
        }
        throw new SQLException("Could not query User by Mac!");
    }
    public User getUserByID(int id) throws SQLException {

        ResultSet res = runPreparedStatementReturnList("SELECT * FROM users\n" +
                "WHERE id=?;", new String[]{String.valueOf(id)});

        if (res.next()) {
            return new User(id, res.getString("mac"), res.getString("name"));
        }
        throw new SQLException("Could not query User per ID!");
    }
    public Score addScore(int userid, int score) throws SQLException {
        try {
            runPreparedStatement("INSERT INTO scores (userid, score)\n" +
                    "VALUES(?, ?);", new String[]{String.valueOf(userid), String.valueOf(score)});
            return new Score(userid,score);
        }catch (Exception e){
            Log.error(e.getMessage());
            throw new SQLException("Could not add Score!");
        }
    }
    public List<Integer> getAllScores(int id) throws SQLException {
        List<Integer> scores= new ArrayList<>();

        ResultSet res =runPreparedStatementReturnList("SELECT score FROM scores WHERE userid = ?;", new String[]{String.valueOf(id)});

        while (res.next()){
            scores.add(res.getInt("score"));
        }
        return scores;
    }
    public int getNumberOfAllScores() throws SQLException {
        String sql =    "SELECT COUNT(*) AS rowcount FROM scores";
        ResultSet res= runPreparedStatementReturnList(sql, null);

        if (res.next()){
            return res.getInt("rowcount") ;
        }
        throw new SQLException("Could not query number of all Scores!");
    }
    public int getNumberOfAllUsers() throws SQLException {
        String sql =    "SELECT COUNT(*) AS rowcount FROM users";

        ResultSet res= runPreparedStatementReturnList(sql, null);

        if (res.next()){
            return res.getInt("rowcount") ;
        }
        throw new SQLException("Could not query number of  all Users!");
    }
    public void emptyUserTable() throws SQLException {
        String sql="DELETE FROM users;";
        try {
            runPreparedStatement(sql,null);
        }catch (Exception e){
            throw new SQLException("Could not empty Usertable!");
        }
    }
    public void emptyScoreTable() throws SQLException {
        String sql="DELETE FROM scores;";
        try {
            runPreparedStatement(sql,null);
        }catch (Exception e){
            throw new SQLException("Could not empty Scoretable!");
        }
    }
}
