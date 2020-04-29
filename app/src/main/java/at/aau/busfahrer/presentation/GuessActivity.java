package at.aau.busfahrer.presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import at.aau.busfahrer.R;
import shared.model.Card;
import shared.model.impl.playersStorage;

public class GuessActivity extends AppCompatActivity {

    private Card[] cards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideAppTitleBar();
        setContentView(R.layout.activity_guess);

        //The cards are fetched from common
        //till now this method only works after creating a new Game on the server
        //Join a new game is not implemented yet
        cards= playersStorage.getCards();

    }

    //The following 4 onClick-methodes are just relevant for Sprint 1 where we want to be able to turn each card
    //in the final edition, the cards are turned by clicking on the buttons
    //This feature may be usefull regarding to cheating

    public void onClickCard1(View v) {
        TextView tV=findViewById(R.id.tV_card1);
        turnCard(tV, cards[0]);
    }

    public void onClickCard2(View view) {
        TextView tV=findViewById(R.id.tV_card2);
        turnCard(tV, cards[1]);
    }

    public void onClickCard3(View view) {
        TextView tV=findViewById(R.id.tV_card3);
        turnCard(tV, cards[2]);
    }

    public void onClickCard4(View view) {
        TextView tV=findViewById(R.id.tV_card4);
        turnCard(tV, cards[3]);
    }

    private void turnCard(TextView tV, Card c){
        //Id suit is Pick or Kreuz -> change collor to red
        if(c.getSuit()==1||c.getSuit()==2){
           tV.setTextColor(Color.parseColor("#FF0000"));//Red
        }

        if(tV.getText().equals("\uD83C\uDCA0")) {//if it shows the cards back-side
            tV.setText(c.toString());
        }else{
            tV.setText("\uD83C\uDCA0");//set to card back side
            tV.setTextColor(Color.parseColor("#000000"));//black
        }
    }

    // removes android status bar on top, for fullscreen
    private void hideAppTitleBar(){
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
