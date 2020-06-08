package at.aau.busfahrer.service.impl;


import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import at.aau.busfahrer.presentation.utils.CardUtility;
import at.aau.busfahrer.service.PLapClientService;
import shared.model.Card;
import shared.model.PlayersStorage;
import shared.model.impl.PlayersStorageImpl;
import shared.networking.Callback;
import shared.networking.NetworkClient;
import shared.networking.dto.DealPointsMessage;
import shared.networking.dto.StartPLapMessage;
import shared.networking.dto.WinnerLooserMessage;
import shared.networking.kryonet.NetworkClientKryo;

public class PLapClientServiceImpl implements PLapClientService {


    // Counter for matched cards from pyramid and cards on the hand.
    private int matchCounter = 0;
    private Callback<Card[]> cardCallback;
    private Callback<Boolean> finishedLabCallback;
    private NetworkClient client;
    private Card[] pCards = new Card[10];
    private List<String> playerNames = new ArrayList<>();
    private PlayersStorage playersStorage;
    private static PLapClientService instance;
    // constants for rows.
    private final static int ROW1 = 1;
    private final static int ROW2 = 2;
    private final static int ROW3 = 3;
    private final static int ROW4 = 4;

    private PLapClientServiceImpl() {
        this.client = NetworkClientKryo.getInstance();
        this.playersStorage = PlayersStorageImpl.getInstance();
    }

    /**
     *  Checks Card Rank of the clicked Card is equals to the one of the Cards on the Hand.
     * @param cardString TextView which contains the card to check.
     * @param cards Cards which should be checked.
     * @return true || false
     */
    @Override
    public Card checkCardMatch(String cardString, Card[] cards, int row) {
        Card pCard = CardUtility.getCardFromString(cardString, this.pCards);
        // check if the cardString is in the pcards stack, in case of null it's a reverted card.
        if (pCard == null)
            return null;
        // Iterate over Player Cards and check if the Card matches.
        for (Card card : cards) {
            Log.i("Card Matcher", pCard.getRank() + "");
            if (card.getRank() == pCard.getRank() && pCard.getRank() > 0 && pCard.getRank() < 10)  {
                if (row == ROW1)
                    matchCounter += 4;
                else if (row == ROW2)
                    matchCounter += 3;
                else if (row == ROW3)
                    matchCounter += 2;
                else if (row == ROW4)
                    matchCounter += 1;
                return card;
            }
        }
        return null;
    }

    /**
     * starts the new Lap called PLap.
     * Send message to the server to get 7 PLap cards afterwards.
     * The registered Callback turn the cards, after the client received it.
     */
    public void startLab() {
        this.client.registerCallback(StartPLapMessage.class, msg -> {
            Log.i("Callback started.", "");
            this.pCards = ((StartPLapMessage) msg).getPlabCards();
            this.playerNames = ((StartPLapMessage) msg).getPlayerNames();
            cardCallback.callback(pCards);
        });

        Thread startThread = new Thread(() -> {
            try {
                Log.i("PLab Service", "PLab start was triggered.");
                this.client.sendMessage(new StartPLapMessage());
            } catch (Exception e) {
                Log.e("Error in PLabService", e.toString(),e);
            }
        });
        startThread.start();
    }

    @Override
    public void registerCardCallback(Callback<Card[]> callback) {
        this.cardCallback = callback;
    }

    @Override
    public void registerFinishedLabCallback(Callback<Boolean> callback) {
        this.finishedLabCallback = callback;
    }

    @Override
    public void dealPoints(String playerName) {
        this.client.registerCallback(WinnerLooserMessage.class, msg -> {
            Log.i("Callback started.", "Winner Looser Callback started.");
            boolean isLooser = ((WinnerLooserMessage) msg).getIsLooser();
            finishedLabCallback.callback(isLooser);
        });
        DealPointsMessage msg = new DealPointsMessage(playerName, matchCounter);
        Thread thread = new Thread(() -> {
            try {
                Log.i("PLab Service Client", "PLab send deal point message.");
                this.client.sendMessage(msg);
            } catch (Exception e) {
                Log.e("Error in PLabService", e.toString(),e);
            }
        });
        thread.start();
    }

    @Override
    public List<String> getPlayerNames() {
        return playerNames;
    }

    @Override
    public int getMatchCount() {
        return matchCounter;
    }

    public static synchronized PLapClientService getInstance() {
        if (instance == null)
            instance = new PLapClientServiceImpl();
        return instance;
    }
    /**
     * Method to destroy instance.
     * Needed for testing purposes.
     */
    public static void destroyInstance() {
        instance = null;
    }
    public Card[] getPlayerCards() {return playersStorage.getCards();}
    public PlayersStorage getPlayerStorage(){
        return this.playersStorage;
    }
    /**
     * needed for unit testing.
     * @param cards
     */
    public void setPCards(Card[] cards) {
        this.pCards = cards;
    }

    public Card[] getPCards(){
        return this.pCards;
    }
}
