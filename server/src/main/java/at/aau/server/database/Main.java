package at.aau.server.database;

import at.aau.server.database.Table.User;

public class Main {
    public static void main(String[] args) {
        Database database= Database.getInstance();
        User user = database.addUser("mac", "Username");
        //System.out.println("USER ADDED: ID= "+user.getId()+" MAC= "+user.getMac()+" NAME= "+user.getName() );
        //database.deleteUser(15);
        System.out.println("scores of user"+database.getAllScores(19).toString());
    }
}