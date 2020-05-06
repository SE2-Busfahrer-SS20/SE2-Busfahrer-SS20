package at.aau.busfahrer.presentation;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.cardemulation.CardEmulation;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import at.aau.busfahrer.R;
import at.aau.busfahrer.service.GameService;
import shared.model.impl.CardImpl;
import shared.model.impl.DeckImpl;



public class BushmenActivity extends AppCompatActivity {

    private List<CardImpl> cards;

    private GameService gameService;

    TextView TxtPunkte;

    private int PunkteAnzahlBusfahrer=0;

    private int KartenCounter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideAppTitleBar();
        setContentView(R.layout.activity_bushmen);

        TxtPunkte = findViewById(R.id.punkte);

        //The cards are fetched from common
        //till now this method only works after creating a new Game on the server
        //Join a new game is not implemented yet
        //cards= playersCards.getCards();

        //Kommt man zum Busfahrer startet man mit 10 Punkten
        PunkteAnzahlBusfahrer+=10;
        UpdateAnzeige();

        // Neue Initialisieren
        Reset_Game();


    }

    private void UpdateAnzeige(){
        TxtPunkte.setText(String.valueOf(PunkteAnzahlBusfahrer));
    }

    //The following 4 onClick-methodes are just relevant for Sprint 1 where we want to be able to turn each card
    //in the final edition, the cards are turned by clicking on the buttons
    //This feature may be usefull regarding to cheating

    public void onClickCard1(View v) {
        TextView tV=findViewById(R.id.tV_card1);
        turnCard(tV, cards.get(0));
    }

    public void onClickCard2(View view) {
        TextView tV=findViewById(R.id.tV_card2);
        turnCard(tV, cards.get(1));
    }

    public void onClickCard3(View view) {
        TextView tV=findViewById(R.id.tV_card3);
        turnCard(tV, cards.get(2));
    }

    public void onClickCard4(View view) {
        TextView tV=findViewById(R.id.tV_card4);
        turnCard(tV, cards.get(3));
    }

    public void onClickCard5(View view) {
        TextView tV=findViewById(R.id.tV_card5);
        turnCard(tV, cards.get(4));
    }

    public void onClickCard6(View view) {
        TextView tV=findViewById(R.id.tV_card6);
        turnCard(tV, cards.get(5));
    }

    public void onClickCard7(View view) {
        TextView tV=findViewById(R.id.tV_card7);
        turnCard(tV, cards.get(6));
    }

    private void turnCard(TextView tV, CardImpl c)  {


        if(tV.getText()!="\uD83C\uDCA0")
            return;

        // Karten Aufdecken
        if(c.getSuit()==1 ){

            tV.setText(c.toString());
            tV.setTextColor(Color.parseColor("#FF0000"));//Red
        }
        else
        {
            tV.setText(c.toString());
            tV.setTextColor(Color.parseColor("#000000"));//Black
        }

        // Prüfung
        // Wenn Bube, König, Dame, Ass Dann Restart
        if(c.getRank() == 0 || c.getRank() == 10 || c.getRank() == 11 || c.getRank() == 12  ) {
            tV.setTextColor(Color.parseColor("#00C800"));//Green

            // Karten für Eingabe Sperren
            Enable_Cards((TextView) findViewById(R.id.tV_card1),false);
            Enable_Cards((TextView) findViewById(R.id.tV_card2),false);
            Enable_Cards((TextView) findViewById(R.id.tV_card3),false);
            Enable_Cards((TextView) findViewById(R.id.tV_card4),false);
            Enable_Cards((TextView) findViewById(R.id.tV_card5),false);
            Enable_Cards((TextView) findViewById(R.id.tV_card6),false);
            Enable_Cards((TextView) findViewById(R.id.tV_card7),false);


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

            PunkteAnzahlBusfahrer-=5;
            UpdateAnzeige();
        }else {
            PunkteAnzahlBusfahrer+=3;
            UpdateAnzeige();
            KartenCounter++;

            if(KartenCounter==3){

                AlertDialog.Builder dialog = new AlertDialog.Builder(BushmenActivity.this,AlertDialog.THEME_DEVICE_DEFAULT_DARK);
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

    private void Set_Card_Backsite(TextView tV)
    {
        tV.setText("\uD83C\uDCA0");//set  card to back side
        tV.setTextColor(Color.parseColor("#000000"));//black
    }

    private  void Enable_Cards(TextView tv, boolean enable)
    {
        tv.setEnabled(enable);
    }

    private void Reset_Game()
    {

        // Karten Zurücksetzen
        Set_Card_Backsite((TextView) findViewById(R.id.tV_card1));
        Set_Card_Backsite((TextView) findViewById(R.id.tV_card2));
        Set_Card_Backsite((TextView) findViewById(R.id.tV_card3));
        Set_Card_Backsite((TextView) findViewById(R.id.tV_card4));
        Set_Card_Backsite((TextView) findViewById(R.id.tV_card5));
        Set_Card_Backsite((TextView) findViewById(R.id.tV_card6));
        Set_Card_Backsite((TextView) findViewById(R.id.tV_card7));

        // Karten für Eingabe Freigeben
        Enable_Cards((TextView) findViewById(R.id.tV_card1),true);
        Enable_Cards((TextView) findViewById(R.id.tV_card2),true);
        Enable_Cards((TextView) findViewById(R.id.tV_card3),true);
        Enable_Cards((TextView) findViewById(R.id.tV_card4),true);
        Enable_Cards((TextView) findViewById(R.id.tV_card5),true);
        Enable_Cards((TextView) findViewById(R.id.tV_card6),true);
        Enable_Cards((TextView) findViewById(R.id.tV_card7),true);


        // Karten NEU Austeilen

        // Neues Deck erstellen
        DeckImpl t = new DeckImpl();
        t.refill(); // Neu Mischen

        // karten neu Auflegen
        cards= t.getCards();

        //Kartenzähler zurückgesetzt
        KartenCounter=0;

    }


    // removes android status bar on top, for fullscreen
    private void hideAppTitleBar(){
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
