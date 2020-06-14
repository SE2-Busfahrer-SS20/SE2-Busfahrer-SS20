package at.aau.busfahrer.service.impl;

import com.esotericsoftware.minlog.Log;

import at.aau.busfahrer.service.GamePlayService;
import shared.model.Card;
import shared.model.GameState;
import shared.model.PlayerDTO;
import shared.networking.Callback;
import shared.networking.NetworkClient;
import shared.networking.dto.*;
import shared.networking.kryonet.NetworkClientKryo;

public class GamePlayServiceImpl implements GamePlayService {

    private NetworkClient client;
    private String host;
    private Callback<Boolean> waitScreenCallback;

    //SINGLETON PATTERN
    private static GamePlayServiceImpl instance;

    public static GamePlayService getInstance() {
        if (GamePlayServiceImpl.instance == null) {
            GamePlayServiceImpl.instance = new GamePlayServiceImpl();
        }
        return GamePlayServiceImpl.instance;
    }

    private GamePlayServiceImpl() {

        this.client = NetworkClientKryo.getInstance();

        this.host = "127.0.0.1"; // set default HostName value.
    }

    @Override
    public void playGame(final String name, final String macAddress) {

        Thread thread = new Thread(() -> {
            RegisterMessage rm = new RegisterMessage(name, macAddress);
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
    public void registerWaitScreenCallback(Callback<Boolean> callback){
        this.waitScreenCallback=callback;
        client.registerCallback(ConfirmRegisterMessage.class,msg -> {
            boolean isMaster=((ConfirmRegisterMessage)msg).isMaster();
            waitScreenCallback.callback(isMaster);

        });
    }
    @Override
    public void startGame() {
        Thread thread = new Thread(() -> {
            StartGameMessage sgm = new StartGameMessage();
            client.sendMessage(sgm);
        });
        thread.start();
    }

    //////////GUESS ROUND/////////////////////////

    @Override
    //Guess-Round #1
    public boolean guessColor(Card card, boolean guessBlack) {
        boolean cardIsBlack = true;
        if (card.getSuit() == 1 || card.getSuit() == 2) {//Red
            cardIsBlack = false;
        }
        return guessBlack == cardIsBlack; //true if player guessed correct, otherwise false

    }

    @Override
    //Guess-Round #2
    public boolean guessHigherLower(Card card, Card reference, boolean guessHigher) {
        int rank=rank(card);
        int rankRef=rank(reference);

        //equal cards count as correct guess
        if(rank==rankRef)
            return true;

        if (rank > rankRef) //rank is higher than reference
            return guessHigher;
        else
            return !guessHigher;
    }

    @Override
    //Guess-Round #3
    public boolean guessBetweenOutside(Card card, Card refOne, Card refTwo, boolean guessBetween) {
        int rank=rank(card);
        int rankLow;
        int rankHigh;

        if(rank(refOne)<rank(refTwo)){
            rankLow=rank(refOne);
            rankHigh=rank(refTwo);
        }else {
            rankLow=rank(refTwo);
            rankHigh=rank(refOne);
        }

        //equal cards count as correct guess
        if (rank == rankLow || rank == rankHigh) {
            return true;
        }

        if (rank > rankLow && rank < rankHigh)    //isBetween ref cards
            return guessBetween;
        else
            return !guessBetween;
    }

    @Override
    //Guess-Round #4
    public boolean guessSuit(Card card, int suit) {
        return card.getSuit() == suit;
    }

    @Override
    public void nextPlayer(final GameState lap, final int tempID, final boolean scored) {
        Thread thread = new Thread(() -> {
            PlayedMessage pM = new PlayedMessage(lap, tempID, scored);
            client.sendMessage(pM);
        });
        thread.start();
    }

    private int rank(Card card){
        int rank=card.getRank();
        if(rank==0)
            rank=13;
        return rank;
    }
    //////////////////////////////////////////////////////////////////////////


    @Override
    public void setHostName(String hostname) {
        this.host = hostname;
    }

    //Send CoughtMessage to the Server
    public void sendMsgCought(final int indexCheater,final int indexCought,final int cheaterScore,final int coughtScore,final boolean cheated){
        Thread thread = new Thread(() -> {
            CoughtMessage coughtMessage = new CoughtMessage(indexCheater,indexCought,cheaterScore,coughtScore,cheated);
            client.sendMessage(coughtMessage);
        });
        thread.start();
    }

    @Override
    public void sendScoreDataToServer(PlayerDTO scoreData){
        Thread thread = new Thread(() -> {

            SaveGameDataMessage sgdm= new SaveGameDataMessage(scoreData);
            client.sendMessage(sgdm);
        });
        thread.start();
    }
}
