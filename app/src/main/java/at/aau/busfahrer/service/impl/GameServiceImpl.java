package at.aau.busfahrer.service.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

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

import static android.content.Context.MODE_PRIVATE;
import static androidx.core.content.ContextCompat.getSystemService;

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

    @Override
    public void createGame(int playercount) {
        //Must be declared final to get accessible in inner class
        final int pc=playercount;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                CreateGameMessage cgm = new CreateGameMessage(pc);
                try {
                    client.connect(host);
                    client.sendMessage(cgm);
                } catch (Exception e) {
                    System.out.println(e);
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }

    @Override
    public void joinGame(final String name){

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RegisterMessage rm = new RegisterMessage(name, getMacAdress());
                try {
                    client.connect(host);
                    client.sendMessage(rm);
                } catch (Exception e) {
                    System.out.println(e);
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }


    private String getMacAdress(){
       /*
        WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        return info.getMacAddress();
        */
        return "MACAdress";
    }


}
