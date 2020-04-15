package at.aau.busfahrer.presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import at.aau.busfahrer.R;
import at.aau.busfahrer.model.Card;
import at.aau.busfahrer.model.impl.CardImpl;

public class GuessActivity extends AppCompatActivity {
    private Card card1;
    private Card card2;
    private Card card3;
    private Card card4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess);

        //This screen is opened on each client after they successfully joint a game and it is their turn

        //#1: The following card objects should be random cards provided by the server
        //Just for now they are hardcoded
        card1 = new CardImpl(0,1);
        card2 = new CardImpl(1,1);
        card3 = new CardImpl(2,1);
        card4 = new CardImpl(3,1);
    }

    //The following 4 onClick-methodes are just relevant for Sprint 1 where we want to be able to turn each card
    //in the final edition, the cards are turned by clicking on the buttons
    //This feature may be usefull regarding to cheating

    public void onClickCard1(View v) {
        TextView tV=findViewById(R.id.tV_card1);
        turnCard(tV, card1);
    }

    public void onClickCard2(View view) {
        TextView tV=findViewById(R.id.tV_card2);
        turnCard(tV, card2);
    }

    public void onClickCard3(View view) {
        TextView tV=findViewById(R.id.tV_card3);
        turnCard(tV, card3);
    }

    public void onClickCard4(View view) {
        TextView tV=findViewById(R.id.tV_card4);
        turnCard(tV, card4);
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
}
