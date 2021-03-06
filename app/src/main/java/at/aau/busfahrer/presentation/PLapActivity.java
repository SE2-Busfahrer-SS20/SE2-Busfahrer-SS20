package at.aau.busfahrer.presentation;

import android.os.Handler;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import java.util.ArrayList;
import at.aau.busfahrer.R;
import at.aau.busfahrer.presentation.utils.CardUtility;
import at.aau.busfahrer.service.CheatService;
import at.aau.busfahrer.service.CoughtService;
import at.aau.busfahrer.service.impl.CheatServiceImpl;
import at.aau.busfahrer.service.PLapClientService;
import at.aau.busfahrer.service.impl.CoughtServiceImpl;
import at.aau.busfahrer.service.impl.PLapClientServiceImpl;
import shared.model.Card;
import shared.model.CoughtServiceListenerPlap;
import shared.networking.kryonet.NetworkClientKryo;


public class PLapActivity extends AppCompatActivity implements CoughtServiceListenerPlap {

    // contains the cards on the hand of the Player.
    private Card[] cards;
    // contains the Ids of the TextViews where the Player cards should be displayed.
    private final int[] myCardIds = {R.id.tV_card1, R.id.tV_card2, R.id.tV_card3, R.id.tV_card4};
    // contains the Ids of the TextViews where the pyramid lab cards should be displayed.
    private final int[] pCardIds = {R.id.tV_pcard1, R.id.tV_pcard2, R.id.tV_pcard3, R.id.tV_pcard4, R.id.tV_pcard5, R.id.tV_pcard6, R.id.tV_pcard7, R.id.tV_pcard8, R.id.tV_pcard9, R.id.tV_pcard10};



    private CheatService cheatService;
    private TextView cheatCard;

    private PLapClientService pLapClientService;

    private TextView tV_cought;
    private CoughtService coughtService;

    Handler uiHandler;
    private Button btn_score;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pLapClientService = PLapClientServiceImpl.getInstance();
        setContentView(R.layout.activity_p_lap);
        pLapClientService.registerCardCallback(pCards -> runOnUiThread(() -> {
            turnCards(pCardIds, pCards);
            turnCards(pCardIds, pCards);
        }));
        pLapClientService.startLab();
        // load Player Cards, player cards will be stored in PlayerStorage.
        cards = pLapClientService.getPlayerCards();
        /**
         *  Turns cards automatically.
         *  Cards must be turned to times.
         *  First one display cards, second one revert it.
         */
        for(int i = 0; i < 2; i++) {
            turnCards(myCardIds, cards);
        }

        // Cheat service init
        cheatService = CheatServiceImpl.getInstance();
        cheatService.setContext(getApplicationContext(), getClass().getName());
        cheatService.startListen();
        handleCheatPLab();

        //CoughtService
        tV_cought = findViewById(R.id.tV_Cought);
        coughtService = CoughtServiceImpl.getInstance();
        tV_cought.setVisibility(View.INVISIBLE);

        uiHandler=new Handler();
        btn_score = findViewById(R.id.bt_score2);

        NetworkClientKryo networkClientKryo = (NetworkClientKryo) NetworkClientKryo.getInstance();
        networkClientKryo.coughtCallbackPlap(this);
        updateScoreButton(pLapClientService.getPlayerStorage().getPlayerList().get(pLapClientService.getPlayerStorage().getTempID()).getScore());
    }
    public void onClickBtCought(View view) {
        if(coughtService.isCheatingPlap()){
            tV_cought.setText("Cheater wurde erwischt!!");
            tV_cought.setVisibility(View.VISIBLE);
            //after 5s the TextView is invisible
            tV_cought.postDelayed(() -> tV_cought.setVisibility(View.INVISIBLE), 5000);
        }else {
            tV_cought.setText("Cheater wurde NICHT erwischt!!");
            tV_cought.setVisibility(View.VISIBLE);
            //after 5s the TextView is invisible
            tV_cought.postDelayed(() -> tV_cought.setVisibility(View.INVISIBLE), 5000);
        }
    }

    @Override
    public void coughtTextViewListenerPlap() {
        runOnUiThread(() -> {
            tV_cought.setText("Erwischt!!!!");
            tV_cought.setVisibility(View.VISIBLE);
            //after 5s the TextView is invisible
            tV_cought.postDelayed(() -> tV_cought.setVisibility(View.INVISIBLE), 5000);
        });
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
            Card card = pLapClientService.checkCardMatch(tV.getText().toString(), cards, getRow(v.getId()));
            if (card != null) {
                CardUtility.turnCard(tV, card);
                Log.d("CARD MATCH", "Your card matched with one from pyramid.");
            } else {
                Log.d("CARD MATCH", "Sorry no match.");
            }
            Log.d("CARD MATCH COUNTER: ", pLapClientService.getMatchCount() + "");
    }

    public void onNextLabClick(View v) {
        cheatService.stopListen();
        Intent i = new Intent(PLapActivity.this, PLabFinished.class);
        startActivity(i);
    }
    public void onClickScore(View v){

        ScoreFragment scoreFragment = new ScoreFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("menu")
                .replace(R.id.score_fragment,scoreFragment,"SCORE_FRAGMENT")
                .commit();
    }
    private void updateScoreButton(int score){
        runOnUiThread(()-> btn_score.setText("Score: "+score));

    }
    /**
     * Turns all Player cards at the beginning of the lab.
     *
     * @param ids   Ids of the TextViews which are containing the cards.
     * @param cards The Card array with Players cards.
     */
    private void turnCards(int[] ids, Card[] cards) {
        for (int i = 0; i < cards.length; i++) {
            CardUtility.turnCard(findViewById(ids[i]), cards[i]);
        }
    }

    /**
     * Helper Function to get row position of specific card.
     * @param id
     * @return ROW
     */
    private int getRow(int id) {
        final int ROW1 = 1;
        final int ROW2 = 2;
        final int ROW3 = 3;
        final int ROW4 = 4;

        if (id == R.id.tV_pcard1)
            return ROW1;
        else if ( id == R.id.tV_pcard2 || id == R.id.tV_pcard3)
            return ROW2;
        else if ( id == R.id.tV_pcard4 || id == R.id.tV_pcard5|| id == R.id.tV_pcard6)
            return ROW3;
        else return ROW4;
    }


       /**
        * A random card form the pyramid is chosen and is displayed in a Dialog Window.
        * The next step for the player is to select a Card from his hand and swap the selected card
        * with the cheated card. Sometimes the cheated card has no advantage for the player if
        * the card is already in his hand.
        */
    private void handleCheatPLab() {
        cheatService.setSensorListener(() -> {
            cheatService.pauseListen();
                new AlertDialog.Builder(PLapActivity.this, R.style.AlertDialogStyleDark)
                        // Yes
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            // sending network call
                            cheatService.stopListen();
                            cheatService.sendMsgCheated(true, System.currentTimeMillis(), cheatService.getSensorType());
                            this.cheatCard = getRandomCardFromPyramid();
                        })
                        // No
                        .setNegativeButton(android.R.string.no, (dialog, which) -> cheatService.resumeListen())
                        .setTitle("Are you sure you want to cheat?").setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_alert).create()
                        .show();
        });
    }

    /**
     * Selects a random card from the pyramid.
     * @return Card TextView for the Dialog.
     */
    private TextView getRandomCardFromPyramid() {
        int randomIndex = cheatService.randomNumber(pCardIds.length);
        TextView cheatedCard = cheatService.generateCard(findViewById(pCardIds[randomIndex]), this);
        Card randomCard = CardUtility.getCardFromString(cheatedCard.getText().toString(), pLapClientService.getPCards());
        // if random card is already turned and if card is not a number card between 0 - 10
        if (cheatedCard.getText().equals("\uD83C\uDCA0") || !(randomCard.getRank() > 0 && randomCard.getRank() < 10)){
                for (int pCardId : pCardIds) {
                if (cheatedCard.getText().equals("\uD83C\uDCA0") || !(randomCard.getRank() > 0 && randomCard.getRank() < 10)) {
                    cheatedCard = cheatService.generateCard(findViewById(pCardId), this);
                    randomCard = CardUtility.getCardFromString(cheatedCard.getText().toString(), pLapClientService.getPCards());
                }
            }
        }

        AlertDialog.Builder showCardDialog = new AlertDialog.Builder(PLapActivity.this, R.style.AlertDialogStyle);
        showCardDialog.setTitle("Your random cheat card is")
                .setView(cheatedCard)
                .setCancelable(false);
        showCardDialog.setPositiveButton(android.R.string.ok, (dialog, which) -> {selectCardsDialog();dialog.dismiss();})
                .show();
        return cheatedCard;
    }

    /**
     * The player selects a card from his hand, the selected card is swapped with cheated card.
     */
    private void selectCardsDialog() {
        ArrayList<String> cardList = new ArrayList<>();
        for (Card card : cards) {
            cardList.add(card.toString());
        }
        // grid view displays the card strings form the list in a grid with on click listener
        GridView gridView = new GridView(this);
        gridView.setAdapter(new ArrayAdapter<>(this, R.layout.grid_card_view, cardList));
        gridView.setNumColumns(2);

        final AlertDialog.Builder selectCardChange = new AlertDialog.Builder(PLapActivity.this, R.style.AlertDialogStyleCards)
                .setTitle("Select a Card from your hand you want to change.")
                .setView(gridView)
                .setCancelable(false);
        final Dialog dialog = selectCardChange.create();
        dialog.show();
        // item click listener, returns the selected card index
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            swapCard(position); dialog.dismiss();
        });
    }

    /**
     * Changes the card from the player hand with the cheated card.
     * pyramidCard: updates the TextView in the player hand.
     * cards: cheatedCard is inserted in the player hand.
     * @param pos selected card from Dialog.
     */
    private void swapCard(int pos) {
        TextView pyramidCard = findViewById(myCardIds[pos]);
        pyramidCard.setText(cheatCard.getText());
        pyramidCard.setTextColor(cheatCard.getCurrentTextColor());

        Card newCheatedCard = CardUtility.getCardFromString(this.cheatCard.getText().toString(), pLapClientService.getPCards());
        cards[pos] = newCheatedCard;
    }

    /**
     * Android lifecycle methods, needed because Android SensorListener also listen if app is in the background.
     */
    @Override
    protected void onPause() {
        super.onPause();
        cheatService.pauseListen();
    }
    @Override
    protected void onResume() {
        super.onResume();
        cheatService.resumeListen();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        cheatService.stopListen();
    }

}
