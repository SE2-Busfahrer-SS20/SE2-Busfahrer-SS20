package at.aau.busfahrer.presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import org.w3c.dom.Text;

import java.sql.Time;
import java.util.HashSet;
import java.util.Set;

import at.aau.busfahrer.R;
import at.aau.busfahrer.presentation.utils.CardUtility;
import shared.model.Card;
import shared.model.Deck;
import shared.model.GameState;
import shared.model.impl.DeckImpl;
import shared.model.impl.playersStorage;

public class PLabActivity extends AppCompatActivity {

    // contains the cards on the hand of the Player.
    private Card[] cards;
    private Card[] pcards;
    // contains the Ids of the TextViews where the Player cards should be displayed.
    private final int[] myCardIds = {R.id.tV_card1, R.id.tV_card2, R.id.tV_card3, R.id.tV_card4};
    // contains the Ids of the TextViews where the pyramid lab cards should be displayed.
    private final int[] pCardIds = {R.id.tV_pcard1, R.id.tV_pcard2, R.id.tV_pcard3, R.id.tV_pcard4, R.id.tV_pcard5, R.id.tV_pcard6, R.id.tV_pcard7, R.id.tV_pcard8, R.id.tV_pcard9, R.id.tV_pcard10};
    // count of matched cards;
    private int matchedCardsCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        cards = new Card[4];
        pcards = new Card[10];
        Deck deck = new DeckImpl();
        for(int i = 0; i < 4; i++)
            cards[i] = deck.drawCard();
        for(int i = 0; i < 10; i++)
            pcards[i] = deck.drawCard();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_lab);
        playersStorage.setState(GameState.LAB2);
        // cards = playersStorage.getCards();
        // pcards = playersStorage.getPyramidCardsCards();
        // turns the Player Cards automatically.
        // CardUtility.turnCard((TextView) findViewById(myCardIds[0]), cards[0]);
        // First print all cards and turn cards.
        for(int i = 0; i < 2; i++) {
            turnCards(myCardIds, cards);
            turnCards(pCardIds, pcards);
        }

    }

    public void onClickCardPCard(View v) {
        if (pcards != null) {
            TextView tV = findViewById(v.getId());
            // Parse Text value of the clicked TextView.
            String cardText = (tV).getText().toString();
            if (checkCardMatch(tV, cards)) {
                matchedCardsCount++;
                Log.d("CARD MATCH", "Your card matched with one from pyramid.");
            } else {
                Log.d("CARD MATCH", "Sorry no match.");
            }
            Log.d("CARD MATCH COUNTER: ", matchedCardsCount + "");
        }
    }

    /**
     * Turns all Player cards at the beginning of the lab.
     *
     * @param ids   Ids of the TextViews which are containing the cards.
     * @param cards The Card array with Players cards.
     */
    private void turnCards(int ids[], Card cards[]) {
        for (int i = 0; i < cards.length; i++) {
            CardUtility.turnCard((TextView) findViewById(ids[i]), cards[i]);
        }
    }
    /**
     *  Checks Card Rank of the clicked Card is equals to the one of the Cards on the Hand.
     * @param tV TextView which contains the card to check.
     * @param cards Cards which should be checked.
     * @return true || false
     */
    private boolean checkCardMatch(TextView tV, Card[] cards) {
        Card pCard = CardUtility.getCardFromString(tV.getText().toString(), pcards);
        // check if the cardString is in the pcards stack, in case of null it's a reverted card.
        if (pCard == null)
            return false;
        else if (pCard.getRank() > 10 || pCard.getRank() < 2)
            return false;
        // Iterate over Player Cards and check if the Card matches.
        for (Card card : cards) {
            if (card.getRank() == pCard.getRank()) {
                CardUtility.turnCard(tV, card);
                return true;
            }
        }
        return false;
    }

}
