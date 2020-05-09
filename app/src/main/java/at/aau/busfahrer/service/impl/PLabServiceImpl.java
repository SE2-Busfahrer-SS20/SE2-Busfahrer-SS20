package at.aau.busfahrer.service.impl;

import android.util.Log;

import at.aau.busfahrer.presentation.utils.CardUtility;
import at.aau.busfahrer.service.PLabService;
import shared.model.Card;
import shared.model.Deck;
import shared.model.impl.DeckImpl;
import shared.networking.Callback;
import shared.networking.NetworkClient;
import shared.networking.dto.StartPLabMessage;
import shared.networking.kryonet.NetworkClientKryo;

public class PLabServiceImpl implements PLabService {


    // Counter for matched cards from pyramid and cards on the hand.
    private int matchCounter = 0;
    private Callback<Card[]> cardCallback;
    private NetworkClient client;
    private Card[] pCards = new Card[10];
    // constants for rows.
    private final int ROW1 = 1, ROW2 = 2, ROW3 = 3, ROW4 = 4;
    // TODO: remove, JUST for TESTING.
    private Card[] cards = new Card[4];

    public PLabServiceImpl() {
        this.client = NetworkClientKryo.getInstance();
        this.client.registerCallback(StartPLabMessage.class, msg -> {
            // TODO: implement get cards.
            cardCallback.callback(pCards);
        });

        // TODO: remove, JUST FOR TESTING.
        Deck deck = new DeckImpl();
        for(int i = 0; i < 4; i++)
            cards[i] = deck.drawCard();
        for(int i = 0; i < 10; i++)
            pCards[i] = deck.drawCard();
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
            Log.i("Card Matcher", pCard.getRank()+"");
            if (card.getRank() == pCard.getRank() && pCard.getRank() > 0 && pCard.getRank() < 10)  {
                if (row == ROW1)
                    matchCounter += 4;
                else if (row == ROW2)
                    matchCounter += 3;
                else if (row == ROW3)
                    matchCounter += 2;
                else
                    matchCounter += 1;
                return card;
            }
        }
        return null;
    }

    @Override
    public void registerCardCallback(Callback<Card[]> callback) {
        this.cardCallback = callback;
    }

    @Override
    public Card[] getPCards() {
        return pCards;
    }
    // TODO: remove, JUST FOR TESTING PURPOSES.
    public Card[] getCards() {
        return cards;
    }
    public void testCallback() {
        this.cardCallback.callback(this.pCards);
    }

    @Override
    public int getMatchCount() {
        return matchCounter;
    }
}
