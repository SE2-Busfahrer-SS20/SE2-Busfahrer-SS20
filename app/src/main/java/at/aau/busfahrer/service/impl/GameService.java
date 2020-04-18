package at.aau.busfahrer.service.impl;

import java.util.BitSet;
import java.util.concurrent.atomic.AtomicBoolean;

import shared.networking.Callback;
import shared.networking.NetworkClient;
import shared.networking.dto.BaseMessage;
import shared.networking.dto.CreateGameMessage;
import shared.networking.kryonet.NetworkClientKryo;


public class GameService {

    public void createGame1(int playercount, String gameName) {

        final int count = playercount;
        final String name = gameName;

        //Muss Asynchron passieren
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                CreateGameMessage newGame = new CreateGameMessage(count, name);

                //send this to Server !
                NetworkClient client = new NetworkClientKryo();
                ((NetworkClientKryo) client).registerClass(CreateGameMessage.class);

                System.out.println("Class registered");
                try {
                    client.connect("localhost");
                    //client.sendMessage(newGame);
                } catch (Exception e) {
                    System.out.println(e);
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void createGame2(int plyercount, String gameName){

        CreateGameMessage newGame = new CreateGameMessage(plyercount,gameName);

        NetworkClient client = new NetworkClientKryo();
        ((NetworkClientKryo) client).registerClass(CreateGameMessage.class);

        try {
            client.connect("localhost");

            System.out.println("Connected to localhost");

            client.registerCallback(new Callback<BaseMessage>() {
                @Override
                public void callback(BaseMessage argument) {
                    System.out.printf("Client Thread ID: %d%n", Thread.currentThread().getId());
                }
            });
            client.sendMessage(newGame);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}