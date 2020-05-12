package at.aau.busfahrer.presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import at.aau.busfahrer.R;
import at.aau.busfahrer.presentation.utils.CardUtility;
import at.aau.busfahrer.service.GameService;
import at.aau.busfahrer.service.impl.GameServiceImpl;
import shared.model.Card;
import shared.model.GameState;
import shared.model.GuessRoundListener;
import shared.model.Player;
import shared.model.impl.GameImpl;
import shared.model.impl.PlayersStorageImpl;


public class GuessActivity extends AppCompatActivity implements GuessRoundListener {
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
    private Button bt_cought;

    private boolean answer;
    private List<Player> playerList;
    private GameImpl gameImpl;
    private int currentPlayer;
    private int indexOfMe;
    private Player playerCheated;
    private Player myself;
    private int scoreCheater;
    private  int myScore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideAppTitleBar();
        setContentView(R.layout.activity_guess);
        playersStorage.setState(GameState.LAP1A);

        tV_guessQuestion = findViewById(R.id.tV_guessQuestion);
        bt_Black = findViewById(R.id.bt_black);
        bt_Red = findViewById(R.id.bt_red);
        tV_feedback = findViewById(R.id.tV_feedback);
        tV_card1=findViewById(R.id.tV_card1);
        tV_card2=findViewById(R.id.tV_card2);
        tV_card3=findViewById(R.id.tV_card3);
        tV_card4=findViewById(R.id.tV_card4);
        bt_cought = findViewById(R.id.bt_caught);

        cards= playersStorage.getCards();
        if(!playersStorage.isMaster()){
            onPauseMode();
        }
        //Register Callback
        playersStorage.registerGuessRoundListener(this);


    }
    public void onClick_btCought(View view) {
        //Check wich player's turn it is
        playerList = gameImpl.getPlayerList();
        //currentPlayer = gameImpl.getCurrentPlayer();
        //indexOfMe = gameImpl.getMyself?
        playerCheated = playerList.get(currentPlayer);
        //Welcher spieler bin ich ? Welcher Index in der Liste?
        //myself = playerList.get(indexOfMe);

        if (playerCheated.isCheatedThisRound() == true){
            scoreCheater = playerCheated.getScore();
            scoreCheater++;
            playerCheated.setScore(scoreCheater);

            //myScore = myself.getScore();
            myScore--;
            //myself.setScore(myScore);

        }



    }



    public void onClick_btBlack(View view) {
       answer=gameService.guessColor(playersStorage.getTempID(), cards[0],true);
        CardUtility.turnCard(tV_card1, cards[0]);
        onAnswer(answer);
    }

    public void onClick_btRed(View view) {
        answer=gameService.guessColor(playersStorage.getTempID(), cards[0],false);
        CardUtility.turnCard(tV_card1, cards[0]);
        onAnswer(answer);
    }

    public void onClick_feedback(View view) {
        gameService.nextPlayer(1,playersStorage.getTempID(),answer);
        onPauseMode();
    }



    @Override   //Callback - executed when receiving
    public void onUpdateMessage(){

        System.out.println("It is the turn of  player: "+playersStorage.getCurrentTurn()+ " !!!!!!!!");


        if(playersStorage.getCurrentTurn()==0){
            //This means that every player has finished his turn and the next interface can be opened
            Intent i = new Intent(GuessActivity.this, MainMenuActivity.class);  //just till next round is finished - MainMenueActivity can be replaced by pyramid after merge
            startActivity(i);
        }
        if(playersStorage.getCurrentTurn()==playersStorage.getTempID()){    //this players turn
            onPlayMode();
        }
        else{
            onPauseMode();
        }
        //update Score in UI (feature does not exist yet)

    }


    //The following 4 onClick-methodes are just relevant for Sprint 1 where we want to be able to turn each card
    //in the final edition, the cards are turned by clicking on the buttons
    //This feature may be usefull regarding to cheating
    public void onClickCard1(View v) {
        // CardUtility.turnCard(tV_card1, cards[0]);
    }
    public void onClickCard2(View view) {
        // CardUtility.turnCard(tV_card2, cards[1]);
    }
    public void onClickCard3(View view) {
        // CardUtility.turnCard(tV_card3, cards[2]);
    }
    public void onClickCard4(View view) {
        // CardUtility.turnCard(tV_card4, cards[3]);
    }

    // removes android status bar on top, for fullscreen
    private void hideAppTitleBar() {
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
        tV_feedback.setVisibility(View.INVISIBLE);
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
            tV_feedback.setText("Correct Answer\n[-OK-]");
        }
        else{
            tV_feedback.setText("Wrong Answer\n[-OK-]");

        }
        tV_feedback.setVisibility(View.VISIBLE);
        bt_Black.setVisibility(View.INVISIBLE);
        bt_Red.setVisibility(View.INVISIBLE);
    }




}
