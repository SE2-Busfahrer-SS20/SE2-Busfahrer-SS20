package at.aau.busfahrer.service.impl;

import com.esotericsoftware.minlog.Log;

import at.aau.busfahrer.service.GameService;
import shared.model.Card;
import shared.networking.NetworkClient;
import shared.networking.dto.CreateGameMessage;
import shared.networking.dto.RegisterMessage;

import shared.networking.dto.StartGameMessage;

import shared.networking.dto.playedMessage;
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
        Thread thread = new Thread(new Runnable() {
                @Override
               public void run() {
            StartGameMessage sgm = new StartGameMessage();
            try {
                client.connect(host);
                client.sendMessage(sgm);
            } catch (Exception e) {
                Log.error(e.toString());
                }
            }
        });
        thread.start();
    }


    public boolean guessColor(Card card, boolean guessBlack){
        boolean cardIsBlack=true;
        if(card.getSuit()==1||card.getSuit()==2){//Red
            cardIsBlack=false;
        }
        final boolean guess=guessBlack==cardIsBlack; //true when player guessed correct, otherwise false

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                playedMessage pM = new playedMessage(guess, 1);
                try {
                    client.connect(host);
                    client.sendMessage(pM);
                } catch (Exception e) {
                    Log.error(e.toString());
                }
            }
        });
        thread.start();

        return guess;

    }


}
