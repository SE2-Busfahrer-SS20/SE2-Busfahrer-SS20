package at.aau.busfahrer.presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import at.aau.busfahrer.R;
import at.aau.busfahrer.presentation.utils.CardUtility;
import at.aau.busfahrer.service.PLabService;
import at.aau.busfahrer.service.impl.PLabServiceImpl;
import shared.model.Card;
import shared.model.GameState;
import shared.model.impl.playersStorage;

public class PLapActivity extends AppCompatActivity {

    // contains the cards on the hand of the Player.
    private Card[] cards;

    private final int ROW1 = 1, ROW2 = 2, ROW3 = 3, ROW4 = 4;
    // contains the Ids of the TextViews where the Player cards should be displayed.
    private final int[] myCardIds = {R.id.tV_card1, R.id.tV_card2, R.id.tV_card3, R.id.tV_card4};
    // contains the Ids of the TextViews where the pyramid lab cards should be displayed.
    private final int[] pCardIds = {R.id.tV_pcard1, R.id.tV_pcard2, R.id.tV_pcard3, R.id.tV_pcard4, R.id.tV_pcard5, R.id.tV_pcard6, R.id.tV_pcard7, R.id.tV_pcard8, R.id.tV_pcard9, R.id.tV_pcard10};

    private PLabService pLabService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pLabService = PLabServiceImpl.getInstance();
        setContentView(R.layout.activity_p_lap);
        pLabService.registerCardCallback(cards -> {
            runOnUiThread(() -> {
                // this.pCards = cards;
                turnCards(pCardIds, cards);
                turnCards(pCardIds, cards);
            });
        });
        pLabService.startLab();

        playersStorage.setState(GameState.LAP2);
        cards = playersStorage.getCards();

        /** TODO: remove after testing. */
        // cards = ((PLabServiceImpl) pLabService).getCards();
        // ((PLabServiceImpl) pLabService).testCallback();
        /**
         *  Turns cards automatically.
         *  Cards must be turned to times.
         *  First one display cards, second one revert it.
         */

        for(int i = 0; i < 2; i++) {
            turnCards(myCardIds, cards);
        }
    }

    /**
     * On Click Listener for the pyramid cards.
     * In case of a click, the card will be checked for a match with card on hand.
     * In case of a match, the player get 1 match point.
     * The matching points can be distributed to the other players.
     * @param v
     */
    public void onClickCardPCard(View v) {
            TextView tV = findViewById(v.getId());
            // Parse Text value of the clicked TextView.
            Card card = pLabService.checkCardMatch(tV.getText().toString(), cards, getRow(v.getId()));
            if (card != null) {
                CardUtility.turnCard(tV, card);
               // matchedCardsCount++;
                Log.d("CARD MATCH", "Your card matched with one from pyramid.");
            } else {
                Log.d("CARD MATCH", "Sorry no match.");
            }
            Log.d("CARD MATCH COUNTER: ", pLabService.getMatchCount() + "");
    }

    public void onNextLabClick(View v) {
        Intent i = new Intent(PLapActivity.this, PLabFinished.class);
        startActivity(i);
    }

    public void onDealPointClick(View v) {

    }

    /**
     * Turns all Player cards at the beginning of the lab.
     *
     * @param ids   Ids of the TextViews which are containing the cards.
     * @param cards The Card array with Players cards.
     */
    private void turnCards(int ids[], Card cards[]) {
        for (int i = 0; i < cards.length; i++) {
            CardUtility.turnCard(findViewById(ids[i]), cards[i]);
        }
    }

    private int getRow(int id) {
        if (id == R.id.tV_pcard1)
            return ROW1;
        else if ( id == R.id.tV_pcard2 || id == R.id.tV_pcard3)
            return ROW2;
        else if ( id == R.id.tV_pcard4 || id == R.id.tV_pcard5|| id == R.id.tV_pcard6)
            return ROW3;
        else return ROW4;
    }
}
