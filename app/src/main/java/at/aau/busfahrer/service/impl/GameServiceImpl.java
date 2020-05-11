package at.aau.busfahrer.service.impl;

import com.esotericsoftware.minlog.Log;

import at.aau.busfahrer.service.GameService;
import shared.model.Card;
import shared.networking.NetworkClient;
import shared.networking.dto.CreateGameMessage;
import shared.networking.dto.RegisterMessage;
import shared.networking.dto.StartGameMessage;
import shared.networking.dto.PlayedMessage;
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
        this.client = NetworkClientKryo.getInstance();
        this.host = shared.networking.kryonet.NetworkConstants.host;
    }

    @Override//can be deleted later
    public void createGame(int playercount) {
        //Must be declared final to get accessible in inner class
        final int pc=playercount;

        Thread thread = new Thread(() -> {
            CreateGameMessage cgm = new CreateGameMessage(pc);
            try {
                client.connect(host);
                client.sendMessage(cgm);
            } catch (Exception e) {
                Log.error(e.toString());
            }
        });
        thread.start();
    }

    @Override
    public void playGame(final String name, final String MACAddress){

        Thread thread = new Thread(() -> {
            RegisterMessage rm = new RegisterMessage(name, MACAddress);
            try {
                client.connect(host);
                client.sendMessage(rm);
            } catch (Exception e) {
                Log.error(e.toString());
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
            client.sendMessage(sgm);
            }
        });
        thread.start();
    }

    @Override
    public boolean guessColor(final int tempID, Card card, boolean guessBlack){
        boolean cardIsBlack=true;
        if(card.getSuit()==1||card.getSuit()==2){//Red
            cardIsBlack=false;
        }
        final boolean scored=guessBlack==cardIsBlack; //true if player guessed correct, otherwise false

        return scored;

    }

    @Override
    public void nextPlayer(final int lap, final int tempID, final boolean scored){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                PlayedMessage pM = new PlayedMessage(lap,tempID, scored);
                client.sendMessage(pM);
            }
        });
        thread.start();
    }


}
