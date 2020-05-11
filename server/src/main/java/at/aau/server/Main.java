package at.aau.server;

import com.esotericsoftware.minlog.Log;

public class Main {
    public static void main(String[] args) {

        GameServer gameserver = new GameServer();
        try {
            gameserver.start();
        } catch (Exception ex) {
            Log.error(ex.toString(), ex);
        }


    }

}
