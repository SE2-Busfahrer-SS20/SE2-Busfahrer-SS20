package at.aau.busfahrer.presentation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import at.aau.busfahrer.R;
import at.aau.busfahrer.service.BushmenService;
import at.aau.busfahrer.service.impl.BushmenServiceImpl;
import shared.model.Card;
import shared.networking.NetworkClient;
import shared.networking.kryonet.NetworkClientKryo;

@SuppressWarnings("unused")
public class BushmenActivity extends AppCompatActivity {

    private final int[] bushmenCards = {R.id.tV_card1, R.id.tV_card2, R.id.tV_card3, R.id.tV_card4, R.id.tV_card5, R.id.tV_card6, R.id.tV_card7};
    
    private NetworkClient networkClient = NetworkClientKryo.getInstance();

    TextView TxtPunkte;


    private BushmenService bushmenService;

    public BushmenActivity() {

        bushmenService = new BushmenServiceImpl(networkClient);

        bushmenService.setUpCardTurnCallback((cardId, card) -> {
            Log.i("Bushmen","BushmenCards recieved"+cardId + " " + card);

            runOnUiThread(() -> {
                turnCardRecieved(cardId, card);
            });
        });


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideAppTitleBar();
        setContentView(R.layout.activity_bushmen);


        TxtPunkte = findViewById(R.id.punkte);

        // set looser variable. Value will be set in PLapFinished Activity.
        bushmenService.setLooser(getIntent().getBooleanExtra("LOST_GAME", false));

        // Neue Initialisieren
        resetGame();
        updateAnzeige();
        
        // Für den Zuschauer wird angezeigt, dass er Zuschauer ist
        TextView textView = findViewById(R.id.headerBushmen);

        if(bushmenService.isLooser()){
            textView.setText("Oh dear! You have to drive with the bus");
        }else {
            textView.setText("Your can only watch!");
        }
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
//                this.networkClient.sendMessage(new BushmenCardMessage(cardId, cards[cardId]));
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

            AlertDialog.Builder dialog = new AlertDialog.Builder(BushmenActivity.this);
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


            // Update der Punkte wenn er Bildkarte erwischt +3, andere Karten -4 Punkte

//            PunkteAnzahlBusfahrer += 3;
            bushmenService.addPunkteAnzahlBusfahrer(3);
            updateAnzeige();
        } else {
//            PunkteAnzahlBusfahrer -= 4;
            bushmenService.addPunkteAnzahlBusfahrer(-4);
            updateAnzeige();
//            KartenCounter++;
            bushmenService.incrementKartenCounter();

            if (bushmenService.hasWon()) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(BushmenActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                dialog.setTitle("Gewonnen");

                String gewinnerNachricht = bushmenService.isLooser() ? "Sie sind der Gewinner" : "Danke für's Zuschauen";
                dialog.setMessage(gewinnerNachricht);

                dialog.setCancelable(false);
                dialog.setPositiveButton("OK", (dialog12, which) -> {
                    //Zurück zum Hauptmenü nach Sieg
                    Intent intent = new Intent(BushmenActivity.this, MainMenuActivity.class);
                    startActivity(intent);
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


        // Karten NEU Austeilen


        //Kartenzähler zurückgesetzt
//        KartenCounter = 0;
        bushmenService.resetKartenCounter();
        bushmenService.resetPunkteAnzahlBusfahrer();

        Thread startThread = new Thread(() -> {
            try {
                Log.i("Bushmen Service", "Bushmen start was triggered.");
//                this.networkClient.sendMessage(new BushmenMessage());
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

}
