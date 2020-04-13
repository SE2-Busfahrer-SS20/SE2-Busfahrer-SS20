package at.aau.server;

public class Main {
    public static void main(String[] args) {
        GameServer gameserver = new GameServer();
        try {
            gameserver.start();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
