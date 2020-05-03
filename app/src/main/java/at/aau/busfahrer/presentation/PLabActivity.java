package at.aau.busfahrer.presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import at.aau.busfahrer.R;
import at.aau.busfahrer.presentation.utils.CardUtility;
import shared.model.Card;
import shared.model.GameState;
import shared.model.impl.playersStorage;

public class PLabActivity extends AppCompatActivity {

    // contains the cards on the hand of the Player.
    private Card[] cards;
    private Card[] pcards;
    // contains the Ids of the TextViews where the Player cards should be displayed.
    private int[] myCardIds = {R.id.tV_card1, R.id.tV_card2, R.id.tV_card3, R.id.tV_card4};
    // contains the Ids of the TextViews where the "Pyramiden Lab" cards should be displayed.
    private int[] pCardIds = {};
    // to check on button click if the cards are reverted.
    private boolean reverted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_lab);
        playersStorage.setState(GameState.LAB2);
        cards = playersStorage.getCards();
        // pcards = playerStorage.
        // turns the Player Cards automatically.
        turnCards(myCardIds, cards);
    }

    public void onClickCardPCard(View v) {
        if (pcards != null) {
            // Parse Text value of the clicked TextView.
            String cardText = ((TextView) findViewById(v.getId())).getText().toString();
            if (checkCardMatch(cardText, cards)) {
                // do something if the card matches.
            }
        }
    }

    /**
     * Turns all Player cards at the beginning of the lab.
     *
     * @param ids   Ids of the TextViews which are containing the cards.
     * @param cards The Card array with Players cards.
     */
    private void turnCards(int ids[], Card cards[]) {
        TextView tV;
        for (int i = 0; i < cards.length; i++) {
            tV = findViewById(ids[i]);
            CardUtility.turnCard(tV, cards[i]);
        }
    }
    /**
     *  Checks Card Rank of the clicked Card is equals to the one of the Cards on the Hand.
     * @param cardString String value of the pyramid card.
     * @param cards Cards which should be checked.
     * @return true || false
     */
    private boolean checkCardMatch(String cardString, Card[] cards) {
        Card pCard = CardUtility.getCardFromString(cardString, pcards);
        // Iterate over Player Cards and check if the Card matches.
        for (Card card : cards)
            if (card.getRank() == pCard.getRank())
                return true;
        return false;
    }

}
