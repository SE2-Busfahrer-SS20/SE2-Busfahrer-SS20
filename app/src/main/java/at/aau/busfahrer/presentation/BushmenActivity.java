package at.aau.busfahrer.presentation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import at.aau.busfahrer.R;
import at.aau.busfahrer.presentation.utils.CardUtility;
import at.aau.busfahrer.service.CheatService;
import at.aau.busfahrer.service.GamePlayService;
import at.aau.busfahrer.service.impl.CheatServiceImpl;
import shared.model.Card;
import shared.model.impl.PlayersStorageImpl;
import shared.networking.NetworkClient;
import shared.networking.dto.BushmenCardMessage;
import shared.networking.dto.BushmenMessage;
import shared.networking.kryonet.NetworkClientKryo;


public class BushmenActivity extends AppCompatActivity {


    private Card[] cards;

    private final int[] bushmenCards = {R.id.tV_card1, R.id.tV_card2, R.id.tV_card3, R.id.tV_card4, R.id.tV_card5, R.id.tV_card6, R.id.tV_card7};

    private PlayersStorageImpl playersStorage = PlayersStorageImpl.getInstance();

    private NetworkClient networkClient = NetworkClientKryo.getInstance();

    //private List<CardImpl> cards;

    private GamePlayService gamePlayService;

    TextView TxtPunkte;

    private int PunkteAnzahlBusfahrer = 0;

    private int KartenCounter = 0;

    private boolean isLooser; // is true in case that the player is a looser.

    private CheatService cheatService;

    public BushmenActivity() {

        //Callback der aufgerufen wird wenn Client die Karten erhalten hat
        networkClient.registerCallback(BushmenMessage.class, msg -> {
            BushmenMessage bushmenMessage = (BushmenMessage) msg;
            this.cards = bushmenMessage.getCards();
            System.out.println(bushmenMessage.getCards().length);
        });

        networkClient.registerCallback(BushmenCardMessage.class, msg ->
                runOnUiThread(() -> {
                    BushmenCardMessage bushmenCardMessage = (BushmenCardMessage) msg;
                    System.out.println(bushmenCardMessage.getCardId() + "CardID recieved");
                    turnCardRecieved(bushmenCardMessage.getCardId(), bushmenCardMessage.getCard());
                }));


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bushmen);

        // Cheat service init
        cheatService = CheatServiceImpl.getInstance();
        cheatService.setContext(getApplicationContext(), getClass().getName());
        cheatService.startListen();

        TxtPunkte = findViewById(R.id.punkte);


        //Kommt man zum Busfahrer startet man mit 10 Punkten
        PunkteAnzahlBusfahrer += 10;
        UpdateAnzeige();

        isLooser = true;
        // set looser variable. Value will be set in PLapFinished Activity.
        isLooser = getIntent().getBooleanExtra("LOST_GAME", false);

        // Neue Initialisieren
        Reset_Game();

        //Log.i("BushmenActiviy",)

        if(isLooser){
            handleCheat();
        }else{
            cheatService.stopListen();
        }
    }


    private void UpdateAnzeige() {
        TxtPunkte.setText(String.valueOf(PunkteAnzahlBusfahrer));
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
                // Log.i("Bushmen Service", "BushmenCard turned" + cardId + " " + cards[cardId]);
                System.out.println("Card turned" + cardId);
                this.networkClient.sendMessage(new BushmenCardMessage(cardId, cards[cardId]));
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
            tV.setTextColor(Color.parseColor("#00C800"));//Green

            // Karten für Eingabe Sperren
            Enable_Cards((TextView) findViewById(R.id.tV_card1), false);
            Enable_Cards((TextView) findViewById(R.id.tV_card2), false);
            Enable_Cards((TextView) findViewById(R.id.tV_card3), false);
            Enable_Cards((TextView) findViewById(R.id.tV_card4), false);
            Enable_Cards((TextView) findViewById(R.id.tV_card5), false);
            Enable_Cards((TextView) findViewById(R.id.tV_card6), false);
            Enable_Cards((TextView) findViewById(R.id.tV_card7), false);


            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    // Erwischt man eine Bildkarte kommt Text verloren und neu beginnen

                    AlertDialog.Builder dialog = new AlertDialog.Builder(BushmenActivity.this);
                    dialog.setTitle("Verloren");
                    dialog.setMessage("Busfahrerrunde beginnt von vorne");
                    dialog.show();

                    // Neu Starten
                    Reset_Game();
                }
            }, 1000);

            // Update der Punkte wenn er Bildkarte erwischt -5, andere Karten +3 Punkte

            PunkteAnzahlBusfahrer -= 5;
            UpdateAnzeige();
        } else {
            PunkteAnzahlBusfahrer += 3;
            UpdateAnzeige();
            KartenCounter++;

            if (KartenCounter == 3) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(BushmenActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                dialog.setTitle("Gewonnen");
                dialog.setMessage("Sie sind der Gewinner");
                dialog.setCancelable(false);
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Zurück zum Hauptmenü nach Sieg
                        Intent intent = new Intent(BushmenActivity.this, at.aau.busfahrer.presentation.MainMenuActivity.class);
                        startActivity(intent);
                    }
                });

                AlertDialog alert = dialog.create();
                alert.show();

            }
        }


    }

    private void Set_Card_Backsite(TextView tV) {
        tV.setText("\uD83C\uDCA0");//set  card to back side
        tV.setTextColor(Color.parseColor("#000000"));//black
    }

    private void Enable_Cards(TextView tv, boolean enable) {
        tv.setEnabled(enable);
    }

    private void Reset_Game() {
        // Karten Zurücksetzen
        Set_Card_Backsite((TextView) findViewById(R.id.tV_card1));
        Set_Card_Backsite((TextView) findViewById(R.id.tV_card2));
        Set_Card_Backsite((TextView) findViewById(R.id.tV_card3));
        Set_Card_Backsite((TextView) findViewById(R.id.tV_card4));
        Set_Card_Backsite((TextView) findViewById(R.id.tV_card5));
        Set_Card_Backsite((TextView) findViewById(R.id.tV_card6));
        Set_Card_Backsite((TextView) findViewById(R.id.tV_card7));

        // Karten für Eingabe Freigeben
        Enable_Cards((TextView) findViewById(R.id.tV_card1), isLooser);
        Enable_Cards((TextView) findViewById(R.id.tV_card2), isLooser);
        Enable_Cards((TextView) findViewById(R.id.tV_card3), isLooser);
        Enable_Cards((TextView) findViewById(R.id.tV_card4), isLooser);
        Enable_Cards((TextView) findViewById(R.id.tV_card5), isLooser);
        Enable_Cards((TextView) findViewById(R.id.tV_card6), isLooser);
        Enable_Cards((TextView) findViewById(R.id.tV_card7), isLooser);


        // Karten NEU Austeilen

        // Neues Deck erstellen
        //DeckImpl t = new DeckImpl();
        //t.refill(); // Neu Mischen

        // karten neu Auflegen
        //cards= t.getCards();


        //Kartenzähler zurückgesetzt
        KartenCounter = 0;

        Thread startThread = new Thread(() -> {
            try {
                Log.i("Bushmen Service", "Bushmen start was triggered.");
                this.networkClient.sendMessage(new BushmenMessage());
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
            new AlertDialog.Builder(BushmenActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK)
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
        message.setTextColor(getResources().getColor(R.color.white));
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
                    CardUtility.turnCard(findViewById(bushmenCards[i]),cards[i]);
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

}
