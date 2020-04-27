package at.aau.busfahrer.service.impl;

import android.util.Log;

import at.aau.busfahrer.presentation.GuessActivity;
import at.aau.busfahrer.service.GameService;
import shared.model.Card;
import shared.model.Game;
import shared.model.impl.CardImpl;
import shared.networking.NetworkClient;
import shared.networking.dto.CreateGameMessage;
import shared.networking.dto.RegisterMessage;
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

    @Override
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
                    client.sendMessage(cgm);
                } catch (Exception e) {
                    Log.e(this.getClass().getName(), "Crate Game Error", e);
                }
            }
        });
        thread.start();

    }

    @Override
    public void joinGame(){

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RegisterMessage rm = new RegisterMessage(); //Later this should include player name
                try {
                    client.connect(host);
                    client.sendMessage(rm);
                } catch (Exception e) {
                    Log.e(this.getClass().getName(), "Join Game Error", e);
                }
            }
        });
        thread.start();

    }



}
