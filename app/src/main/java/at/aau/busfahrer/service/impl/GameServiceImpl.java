package at.aau.busfahrer.service.impl;

import at.aau.busfahrer.presentation.GuessActivity;
import at.aau.busfahrer.service.GameService;
import shared.model.Card;
import shared.model.Game;
import shared.model.impl.CardImpl;
import shared.networking.NetworkClient;
import shared.networking.dto.CreateGameMessage;
import shared.networking.dto.TextMessage;
import shared.networking.kryonet.NetworkClientKryo;

public class GameServiceImpl implements GameService {

    private NetworkClient client;
    private String host;
    //send this to Server !
    public GameServiceImpl(String host) {
        this.client = new NetworkClientKryo();
        this.host = host;
    }

    public void connect() {
        //Whats this method designated for?
    }

    public void createGame(int playercount, String gameName) {
        //Must be declared final to get accessable in inner class
        final int pc=playercount;
        final String gN = gameName;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                CreateGameMessage cgm = new CreateGameMessage(pc,gN);
                try {
                    client.connect(host);
                    //client.sendMessage(new TextMessage("test"));
                    client.sendMessage(cgm);
                } catch (Exception e) {
                    System.out.println(e);
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }



}
