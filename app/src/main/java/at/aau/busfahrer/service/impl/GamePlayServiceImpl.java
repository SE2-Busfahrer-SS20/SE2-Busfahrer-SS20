package at.aau.busfahrer.service.impl;

import com.esotericsoftware.minlog.Log;

import at.aau.busfahrer.service.GamePlayService;
import shared.model.Card;
import shared.networking.NetworkClient;
import shared.networking.dto.CheatedMessage;
import shared.networking.dto.CreateGameMessage;
import shared.networking.dto.RegisterMessage;
import shared.networking.dto.StartGameMessage;
import shared.networking.dto.PlayedMessage;
import shared.networking.kryonet.NetworkClientKryo;

public class GamePlayServiceImpl implements GamePlayService {

    private NetworkClient client;
    private String host;
    //send this to Server !

    //SINGLETON PATTERN
    private static GamePlayServiceImpl Instance;

    public static GamePlayService getInstance(){
        if(GamePlayServiceImpl.Instance==null){
            GamePlayServiceImpl.Instance=new GamePlayServiceImpl();
        }
        return GamePlayServiceImpl.Instance;
    }

    private GamePlayServiceImpl() {
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

    //////////GUESS ROUND/////////////////////////

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
    public boolean guessHigherLower(final int tempID, Card card,Card reference, boolean guessHigher){
        if(card.getRank()==reference.getRank()) //same rank counts as correct guess
            return true;

        boolean cardIsHigher=true;

        if(card.getRank()<reference.getRank())//card is lower
            cardIsHigher=false;

        //following two if-statements are needed because Ass is the highest card but stored with Rank=0
        if(card.getRank()==0)//second card is Ass -> must be higher
            return true;

        if(reference.getRank()==0)//first card is Ass -> second card must be lower, if it is same, this code is not executed
            return false;

        final boolean scored=guessHigher==cardIsHigher; //true if player guessed correct, otherwise false
        return scored;
    }
    @Override
    public boolean guessBetweenOutside(final int tempID, Card card, boolean guessBetween){

        return false;
    }
    @Override
    public boolean guessSuit(final int tempID, Card card, int suit){

        return false;
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

    //////////////////////////////////////////////////////////////////////////


    // network call for player cheated in game
    public void sendMsgCheated(final int playerId, final boolean cheated, final long timeStamp, final int cheatType){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                CheatedMessage cM = new CheatedMessage(playerId,cheated,timeStamp,cheatType);
                client.sendMessage(cM);
            }
        });
        thread.start();
    }


}
