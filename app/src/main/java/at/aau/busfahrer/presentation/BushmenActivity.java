package at.aau.busfahrer.presentation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.content.ContextCompat;

import at.aau.busfahrer.presentation.utils.CardUtility;
import at.aau.busfahrer.service.CheatService;
import at.aau.busfahrer.service.CoughtService;
import at.aau.busfahrer.service.impl.CheatServiceImpl;
import at.aau.busfahrer.R;
import at.aau.busfahrer.service.BushmenService;
import at.aau.busfahrer.service.impl.BushmenServiceImpl;
import at.aau.busfahrer.service.impl.CoughtServiceImpl;
import shared.model.Card;
import shared.model.CoughtServiceListenerBushmen;
import shared.model.PlayersStorage;
import shared.model.impl.PlayersStorageImpl;
import shared.networking.NetworkClient;
import shared.networking.kryonet.NetworkClientKryo;

@SuppressWarnings("unused")
public class BushmenActivity extends AppCompatActivity implements CoughtServiceListenerBushmen {

    private final int[] bushmenCards = {R.id.tV_card1, R.id.tV_card2, R.id.tV_card3, R.id.tV_card4, R.id.tV_card5, R.id.tV_card6, R.id.tV_card7};
    
    private NetworkClient networkClient = NetworkClientKryo.getInstance();

    TextView TxtPunkte;

    private PlayersStorage playersStorage = PlayersStorageImpl.getInstance();
    private BushmenService bushmenService;

    private CheatService cheatService;

    //CoughtFunction
    private CoughtService coughtService;


    Handler uiHandler;
    private Button btn_score;


    public BushmenActivity() {

        bushmenService = new BushmenServiceImpl(networkClient);

        bushmenService.setUpCardTurnCallback((cardId, card) -> {
            Log.i("Bushmen","BushmenCards recieved"+cardId + " " + card);

            runOnUiThread(() -> turnCardRecieved(cardId, card));
        });


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideAppTitleBar();
        setContentView(R.layout.activity_bushmen);
        Button btCought;
        uiHandler=new Handler();
        btn_score = findViewById(R.id.bt_score3);
        // Cheat service init
        cheatService = CheatServiceImpl.getInstance();
        cheatService.setContext(getApplicationContext(), getClass().getName());
        cheatService.startListen();

        TxtPunkte = findViewById(R.id.punkte);



        TxtPunkte = findViewById(R.id.punkte);

        // set looser variable. Value will be set in PLapFinished Activity.
        bushmenService.setLooser(getIntent().getBooleanExtra("LOST_GAME", false));
        // Neue Initialisieren
        resetGame();
        updateAnzeige();
        updateScoreButton(playersStorage.getScoreList().get(playersStorage.getTempID()));

        // Für den Zuschauer wird angezeigt, dass er Zuschauer ist
        TextView textView = findViewById(R.id.headerBushmen);

        btCought = findViewById(R.id.button4);
        coughtService = CoughtServiceImpl.getInstance();
        findViewById(R.id.tvCought).setVisibility(View.INVISIBLE);

        ((NetworkClientKryo)networkClient).coughtCallbackBushmen(this);

        if(bushmenService.isLooser()){
            textView.setText("Oh dear! You have to drive with the bus");
            handleCheat();
            btCought.setVisibility(View.INVISIBLE);
        }else {
            textView.setText("Your can only watch!");
            cheatService.stopListen();
            btCought.setVisibility(View.VISIBLE);
        }
    }
    private void updateScoreButton(int score){
        uiHandler.post(() -> btn_score.setText("Score: "+score));
    }

    public void onClickScore(View v){

        ScoreFragment scoreFragment = new ScoreFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("menu")
                .replace(R.id.score_fragment,scoreFragment,"SCORE_FRAGMENT")
                .commit();
    }

    private void updateAnzeige() {
        TxtPunkte.setText("" + bushmenService.getPunkteAnzahlBusfahrer());
    }


    public void onClickCard1(View v) {
        turnCardRequest(0);
    }

    public void onClickCard2(View view) {
        turnCardRequest(1);
    }

    public void onClickCard3(View view) {

        turnCardRequest(2);
    }

    public void onClickCard4(View view) {
        turnCardRequest(3);
    }

    public void onClickCard5(View view) {
        turnCardRequest(4);
    }

    public void onClickCard6(View view) {
        turnCardRequest(5);
    }

    public void onClickCard7(View view) {
        turnCardRequest(6);
    }

    public void turnCardRequest(int cardId) {

        Thread startThread = new Thread(() -> {
            try {
              Log.i("Bushmen","Card turned"+cardId);
                this.bushmenService.turnCard(cardId);
            } catch (Exception e) {
                Log.e("Error in BushmenCard", e.toString(), e);
            }

        });
        startThread.start();

    }

    public void turnCardRecieved(int cardId, Card c) {

        TextView tV = findViewById(bushmenCards[cardId]);


        if (tV.getText() != "\uD83C\uDCA0")
            return;

        // Karten Aufdecken
        if (c.getSuit() == 1) {

            tV.setText(c.toString());
            tV.setTextColor(Color.parseColor("#FF0000"));//Red
        } else {
            tV.setText(c.toString());
            tV.setTextColor(Color.parseColor("#000000"));//Black
        }

        // Prüfung
        // Wenn Bube, König, Dame, Ass Dann Restart
        if (c.getRank() == 0 || c.getRank() == 10 || c.getRank() == 11 || c.getRank() == 12) {

            // Karten für Eingabe Sperren
            enableCards(findViewById(R.id.tV_card1), false);
            enableCards(findViewById(R.id.tV_card2), false);
            enableCards(findViewById(R.id.tV_card3), false);
            enableCards(findViewById(R.id.tV_card4), false);
            enableCards(findViewById(R.id.tV_card5), false);
            enableCards(findViewById(R.id.tV_card6), false);
            enableCards(findViewById(R.id.tV_card7), false);

            AlertDialog.Builder dialog = new AlertDialog.Builder(BushmenActivity.this, R.style.AlertDialogStyleDark);
            dialog.setTitle("Verloren");
            dialog.setMessage("Busfahrerrunde beginnt von vorne");
            final Dialog dialog1 = dialog.create();
            dialog1.show();

            Handler handler = new Handler();
            handler.postDelayed(() -> {

                dialog1.dismiss();

                // Neu Starten
                resetGame();
            }, 2000);


            bushmenService.addPunkteAnzahlBusfahrer(3);
            updateAnzeige();
        } else {

            bushmenService.addPunkteAnzahlBusfahrer(-4);
            updateAnzeige();

            bushmenService.incrementKartenCounter();

            if (bushmenService.hasWon()) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(BushmenActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                dialog.setTitle("Spielende");

                String gewinnerNachricht = bushmenService.isLooser() ? "Sie sind der Gewinner" : "Danke für's Zuschauen";
                dialog.setMessage(gewinnerNachricht);

                dialog.setCancelable(false);
                dialog.setPositiveButton("OK", (dialog12, which) -> {
                    if (bushmenService.isLooser()) {
                        playersStorage.addScoreToCurrentPlayer(bushmenService.getPunkteAnzahlBusfahrer());
                    }
                    Intent intent = new Intent(BushmenActivity.this, GameOverviewActivity.class);
                    startActivity(intent);
                    CheatServiceImpl.reset();
                });

                AlertDialog alert = dialog.create();
                alert.show();
            }
        }
    }

    private void setCardBacksite(TextView tV) {
        tV.setText("\uD83C\uDCA0");//set  card to back side
        tV.setTextColor(Color.parseColor("#000000"));//black
    }

    private void enableCards(TextView tv, boolean enable) {
        tv.setEnabled(enable);
    }

    private void resetGame() {
        // Karten Zurücksetzen
        setCardBacksite(findViewById(R.id.tV_card1));
        setCardBacksite(findViewById(R.id.tV_card2));
        setCardBacksite(findViewById(R.id.tV_card3));
        setCardBacksite(findViewById(R.id.tV_card4));
        setCardBacksite(findViewById(R.id.tV_card5));
        setCardBacksite(findViewById(R.id.tV_card6));
        setCardBacksite(findViewById(R.id.tV_card7));

        // Karten für Eingabe Freigeben
        boolean isLooser = bushmenService.isLooser();

        enableCards(findViewById(R.id.tV_card1), isLooser);
        enableCards(findViewById(R.id.tV_card2), isLooser);
        enableCards(findViewById(R.id.tV_card3), isLooser);
        enableCards(findViewById(R.id.tV_card4), isLooser);
        enableCards(findViewById(R.id.tV_card5), isLooser);
        enableCards(findViewById(R.id.tV_card6), isLooser);
        enableCards(findViewById(R.id.tV_card7), isLooser);


        bushmenService.resetKartenCounter();
        bushmenService.resetPunkteAnzahlBusfahrer();

        Thread startThread = new Thread(() -> {
            try {
                Log.i("Bushmen Service", "Bushmen start was triggered.");
                this.bushmenService.startBushmanRound();
            } catch (Exception e) {
                Log.e("Error in BushmenService", e.toString(), e);
            }

        });
        startThread.start();


    }


    // removes android status bar on top, for fullscreen
    private void hideAppTitleBar() {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * This method handle's the cheat action if a player activates cheats.
     */
    public void handleCheat(){
        cheatService.setSensorListener(() -> {
            cheatService.pauseListen();
            new AlertDialog.Builder(BushmenActivity.this, R.style.AlertDialogStyleDark)
                    // Yes
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        // sending network call
                        cheatService.stopListen();
                        cheatService.sendMsgCheated(true, System.currentTimeMillis(), cheatService.getSensorType());
                        this.flipCards();
                    })
                    // No
                    .setNegativeButton(android.R.string.no, (dialog, which) -> cheatService.resumeListen())
                    .setTitle("Are you sure you want to cheat?").setCancelable(false)
                    .setIcon(android.R.drawable.ic_dialog_alert).create()
                    .show();
        });
    }

    /**
     * This method turns some cards, after 1 second the turned cards gets turned back.
     */
    public void flipCards(){
        TextView message = new TextView(this);
                message.setText("Cards will get turned for 1 second, remember the cards!");
        message.setTextColor(ContextCompat.getColor(this, R.color.white));

        message.setGravity(Gravity.CENTER);
        message.setTextSize(20);
        message.setPadding(15,55,15,55);

        AlertDialog.Builder cheat = new AlertDialog.Builder(BushmenActivity.this, R.style.AlertDialogStyleCards)
                .setView(message)
                .setCancelable(false);
        final Dialog dialog = cheat.create();
        dialog.show();

        Handler turn = new Handler();
        turn.postDelayed(() -> {
            dialog.dismiss();
            for (int i = 0; i < bushmenCards.length ; i++) {
                if(i % 2 == 1 || i == 0){
                    CardUtility.turnCard(findViewById(bushmenCards[i]),bushmenService.getCards().get(i));
                }
            }
        },2000);

        // flip cards back
        Handler reTurn = new Handler();
        reTurn.postDelayed(() -> {
            for (int i = 0; i < bushmenCards.length ; i++) {
                if(i % 2 == 1 || i == 0){
                    CardUtility.turnCardBack(findViewById(bushmenCards[i]));
                }
            }
        },3000);
    }

    /**
     * Android lifecycle methods, handling app state.
     */
    @Override
    protected void onPause() {
        super.onPause();
        cheatService.pauseListen();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cheatService.stopListen();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cheatService.resumeListen();
    }

    public void onClickBtCought(View view){

        TextView tVCought = findViewById(R.id.tvCought);
        if(coughtService.isCheatingBushmen()){
            tVCought.setText("Cheater wurde erwischt!!");
        }else{
            tVCought.setText("Cheater wurde NICHT erwischt!!");
        }
        changeVisibility(tVCought);
    }

    @Override
    public void coughtTextViewListenerBushmen() {
        runOnUiThread(() -> {
            TextView tVCought = findViewById(R.id.tvCought);
            tVCought.setText("Erwischt!!!!");
            Log.d("Cheater:", "\n\n\n---------- erwischt!! ----------\n\n\n");
            changeVisibility(tVCought);
        });
    }

    private void changeVisibility(TextView tv) {

        tv.setVisibility(View.VISIBLE);
        //after 5s the TextView is invisible
        tv.postDelayed(() -> tv.setVisibility(View.INVISIBLE), 5000);
    }

}
