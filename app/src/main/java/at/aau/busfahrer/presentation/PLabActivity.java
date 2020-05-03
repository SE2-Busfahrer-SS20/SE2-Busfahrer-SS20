package at.aau.busfahrer.presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import at.aau.busfahrer.R;
import at.aau.busfahrer.presentation.utils.CardUtility;
import shared.model.Card;
import shared.model.impl.playersStorage;

public class PLabActivity extends AppCompatActivity {

    private Card[] cards;
    private int[]  myCardIds = {R.id.tV_card1, R.id.tV_card2, R.id.tV_card3, R.id.tV_card4};
    private int[]  pCardIds = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_lab);
        cards= playersStorage.getCards();
        turnCards(myCardIds, cards);
    }

    public void onClickCard1(View v) {
        TextView tV = findViewById(v.getId());
        CardUtility.turnCard(tV, cards[0]);
    }

    private void turnCards(int ids[], Card cards[]) {
        TextView tV;
        for (int i = 0; i < cards.length; i++) {
            tV = findViewById(ids[i]);
            CardUtility.turnCard(tV, cards[i]);
        }
    }
}
