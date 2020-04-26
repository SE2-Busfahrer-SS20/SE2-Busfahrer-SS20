package at.aau.server;

import at.aau.server.service.GameService;
import shared.networking.NetworkClient;
import shared.networking.dto.CreateGameMessage;
import shared.networking.kryonet.NetworkClientKryo;

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
