package at.aau.busfahrer.presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import at.aau.busfahrer.R;
import at.aau.busfahrer.service.GameService;
import at.aau.busfahrer.service.impl.GameServiceImpl;
import shared.model.Card;
import shared.model.GameState;
import shared.model.impl.PlayersStorageImpl;


public class GuessActivity extends AppCompatActivity {
    private boolean myturn;
    private Card[] cards;
    private PlayersStorageImpl playersStorage= PlayersStorageImpl.getInstance();
    private GameService gameService = GameServiceImpl.getInstance();

    private TextView tV_guessQuestion;
    private Button bt_Black;
    private Button bt_Red;
    private TextView tV_feedback;
    private TextView tV_card1;
    private TextView tV_card2;
    private TextView tV_card3;
    private TextView tV_card4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideAppTitleBar();
        setContentView(R.layout.activity_guess);
        playersStorage.setState(GameState.LAB1);

        tV_guessQuestion = findViewById(R.id.tV_guessQuestion);
        bt_Black = findViewById(R.id.bt_black);
        bt_Red = findViewById(R.id.bt_red);
        tV_feedback = findViewById(R.id.tV_feedback);
        tV_card1=findViewById(R.id.tV_card1);
        tV_card2=findViewById(R.id.tV_card2);
        tV_card3=findViewById(R.id.tV_card3);
        tV_card4=findViewById(R.id.tV_card4);

        cards= playersStorage.getCards();
        if(playersStorage.isMaster()){
            myturn=true;
        }
        else{
            onPauseMode();
        }

    }
    public void onClick_btBlack(View view) {
       boolean answer=gameService.guessColor(playersStorage.getTempID(), cards[0],true);
        turnCard(tV_card1, cards[0]);
        onAnswer(answer);
    }

    public void onClick_btRed(View view) {
        boolean answer=gameService.guessColor(playersStorage.getTempID(), cards[0],false);
        turnCard(tV_card1, cards[0]);
        onAnswer(answer);
    }


    //The following 4 onClick-methodes are just relevant for Sprint 1 where we want to be able to turn each card
    //in the final edition, the cards are turned by clicking on the buttons
    //This feature may be usefull regarding to cheating
    public void onClickCard1(View v) {
        turnCard(tV_card1, cards[0]);
    }
    public void onClickCard2(View view) {
        turnCard(tV_card2, cards[1]);
    }
    public void onClickCard3(View view) {
        turnCard(tV_card3, cards[2]);
    }
    public void onClickCard4(View view) {
        turnCard(tV_card4, cards[3]);
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

    private void onPauseMode(){
        tV_guessQuestion.setText("wait till it is your turn..");   //Extend this to "it's playernames turn"
        bt_Black.setClickable(false);
        bt_Red.setClickable(false);
        bt_Black.setBackgroundResource(R.drawable.bg_btn_gray);
        bt_Red.setBackgroundResource(R.drawable.bg_btn_gray);
        tV_card1.setTextColor(Color.GRAY);
        tV_card2.setTextColor(Color.GRAY);
        tV_card3.setTextColor(Color.GRAY);
        tV_card4.setTextColor(Color.GRAY);
    }

    private void onPlayMode(){
       tV_guessQuestion.setText("Guess if the first card is red or black");   //Extend this to "it's playernames turn"
        bt_Black.setClickable(true);
        bt_Red.setClickable(true);
        bt_Black.setBackgroundResource(R.drawable.bg_btn_black);
        bt_Red.setBackgroundResource(R.drawable.bg_btn_red);
        tV_card1.setTextColor(Color.parseColor("#000000"));
        tV_card2.setTextColor(Color.parseColor("#000000"));
        tV_card3.setTextColor(Color.parseColor("#000000"));
        tV_card4.setTextColor(Color.parseColor("#000000"));
    }

    private void onAnswer(boolean answer){
        if(answer){
            tV_feedback.setText("Correct Answer");
        }
        else{
            tV_feedback.setText("Wrong Answer");

        }
        tV_feedback.setVisibility(View.VISIBLE);
        bt_Black.setVisibility(View.INVISIBLE);
        bt_Red.setVisibility(View.INVISIBLE);
    }


}
