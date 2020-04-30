package at.aau.busfahrer.service.impl;

import android.content.Context;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;



import com.esotericsoftware.minlog.Log;

import at.aau.busfahrer.service.GameService;
import shared.networking.NetworkClient;
import shared.networking.dto.CreateGameMessage;
import shared.networking.dto.RegisterMessage;
import shared.networking.dto.StartGameMessage;
import shared.networking.kryonet.NetworkClientKryo;

public class GameServiceImpl implements GameService {

    private NetworkClient client;
    private String host;
    //send this to Server !

    //SINGLETON PATTERN
    private static GameServiceImpl Instance;

    public static GameService getInstance(){
        if(GameServiceImpl.Instance==null){
            GameServiceImpl.Instance=new GameServiceImpl();
        }
        return GameServiceImpl.Instance;
    }

    private GameServiceImpl() {
        this.client = new NetworkClientKryo();
        this.host = shared.networking.kryonet.NetworkConstants.host;
    }

    public void connect() {
        //Whats this method designated for?
    }

    @Override//can be deleted later
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
                    Log.error(e.toString());
                }
            }
        });
        thread.start();
    }

    @Override
    public void playGame(final String name, final String MACAddress){

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RegisterMessage rm = new RegisterMessage(name, MACAddress);
                try {
                    client.connect(host);
                    client.sendMessage(rm);
                } catch (Exception e) {
                    Log.error(e.toString());
                }
            }
        });
        thread.start();
    }

    @Override
    public void startGame(){
        System.out.println("THREAD: sending SGM...");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
        StartGameMessage sgm = new StartGameMessage();
        try {
            client.connect(host);
            client.sendMessage(sgm);
            System.out.println("sending SGM...");
        } catch (Exception e) {
            Log.error(e.toString());
        }
            }
        });
        thread.start();
    }




}
