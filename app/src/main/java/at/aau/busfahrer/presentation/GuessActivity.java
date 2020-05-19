package at.aau.busfahrer.presentation;

import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import at.aau.busfahrer.R;
import at.aau.busfahrer.presentation.utils.CardUtility;
import at.aau.busfahrer.service.CheatService;
import at.aau.busfahrer.service.GamePlayService;
import at.aau.busfahrer.service.impl.CheatServiceImpl;

import at.aau.busfahrer.service.impl.CoughtServiceImpl;
import at.aau.busfahrer.service.impl.GamePlayServiceImpl;
import shared.model.Card;
import shared.model.GameState;
import shared.model.GuessRoundListener;
import shared.model.Player;
import shared.model.impl.GameImpl;
import shared.model.impl.PlayersStorageImpl;
import shared.model.impl.playersStorage;
import shared.networking.dto.PlayedMessage;


public class GuessActivity extends AppCompatActivity implements GuessRoundListener {

    Handler uiHandler;
    private Card[] cards;
    private PlayersStorageImpl playersStorage = PlayersStorageImpl.getInstance();

    private GamePlayService gamePlayService = GamePlayServiceImpl.getInstance();

    private TextView tV_guessQuestion;
    private Button bt_FirstOption;
    private Button bt_SecondOption;
    private TextView tV_feedback;
    private TextView tV_card1;
    private TextView tV_card2;
    private TextView tV_card3;
    private TextView tV_card4;
    private Button bt_cought;
    private Button btn_score;


    private boolean answer;
    private CheatService cheatService;


    private CoughtServiceImpl coughtService;
    private TextView tV_erwischt;
    ///

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideAppTitleBar();
        setContentView(R.layout.activity_guess);
        uiHandler=new Handler();
        btn_score = findViewById(R.id.bt_score);

        //Visibility
        tV_guessQuestion = findViewById(R.id.tV_guessQuestion);
        bt_FirstOption = findViewById(R.id.bt_FirstOption);
        bt_SecondOption = findViewById(R.id.bt_SecondOption);
        tV_feedback = findViewById(R.id.tV_feedback);
        tV_card1 = findViewById(R.id.tV_card1);
        tV_card2 = findViewById(R.id.tV_card2);
        tV_card3 = findViewById(R.id.tV_card3);
        tV_card4 = findViewById(R.id.tV_card4);
        bt_cought = findViewById(R.id.bt_caught);
        tV_erwischt = findViewById(R.id.txtView_erwischt);


        playersStorage.setState(GameState.LAP1A);
        cards = playersStorage.getCards();
        if (!playersStorage.isMaster()) {
            onPauseMode();
            bt_cought.setVisibility(View.VISIBLE);
        }else{
            bt_cought.setVisibility(View.INVISIBLE);
        }

        //Cheat Service
        cheatService = CheatServiceImpl.getInstance();
        cheatService.setContext(getApplicationContext(), getClass().getName());
        cheatService.startListen();
        //handleCheat();

        //Register Callback
        playersStorage.registerGuessRoundListener(this);

        ///SCHUMMEL - Aufdeckfunktion
        //Only when I am cheating, the Text View is could be visbible
        tV_erwischt.setVisibility(View.INVISIBLE);

    }

    public void onClick_btCought(View view) {
        //if the current player was cheating, he gets one point and the textView will be visible
        if (coughtService.isCheating()==true){
            //TextView "Erwischt!"
            if(playersStorage.getCurrentTurn()==playersStorage.getTempID()){
                tV_erwischt.setVisibility(View.VISIBLE);
            }

            //after 5s the TextView is invisible
            /*tV_erwischt.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tV_erwischt.setVisibility(View.INVISIBLE);
                }
            }, 5000);*/
        }


    }
    public void onClickScore(View v){

        ScoreFragment aboutFragment = new ScoreFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("menu")
                .replace(R.id.score_fragment,aboutFragment,"ABOUT_FRAGMENT")
                .commit();
    }


    // handles cheating, Confirmation dialog, if player press yes --> cheatedMessage sent to server
    public void handleCheat() {
        cheatService.setSensorListener(() -> {
            cheatService.pauseListen();
            if (playersStorage.getTempID() == playersStorage.getCurrentTurn()) {
                new AlertDialog.Builder(GuessActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                        // Yes
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            // sending network call
                            gamePlayService.sendMsgCheated(playersStorage.getTempID(), true, System.currentTimeMillis(), cheatService.getSensorType());
                            CardUtility.turnCard(tV_card1, cards[0]);
                            cheatService.stopListen();
                        })
                        // No
                        .setNegativeButton(android.R.string.no, (dialog, which) -> cheatService.resumeListen())
                        .setTitle("Are you sure you want to cheat?")
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .create().show();
            } else {
                Toast.makeText(this, "Wait until your Turn starts", Toast.LENGTH_SHORT).show();
                cheatService.resumeListen();
            }
        });
    }


    public void onClick_FirstOption(View view) {
        switch (playersStorage.getState()) {
            case LAP1A:
                answer = gamePlayService.guessColor(playersStorage.getTempID(), cards[0], true);
                CardUtility.turnCard(tV_card1, cards[0]);
                onAnswer(answer);
                break;
            case LAP1B:
                answer = gamePlayService.guessHigherLower(playersStorage.getTempID(), cards[1], true);
                CardUtility.turnCard(tV_card1, cards[1]);
                onAnswer(answer);
                break;
            case LAP1C:
                break;
            case LAP1D:
                break;
            default:
                //ERROR

        }
    }


    public void onClick_SecondOption(View view) {

        switch (playersStorage.getState()) {
            case LAP1A:
                answer = gamePlayService.guessColor(playersStorage.getTempID(), cards[0], false);
                CardUtility.turnCard(tV_card1, cards[0]);
                onAnswer(answer);
                break;
            case LAP1B:
                answer = gamePlayService.guessHigherLower(playersStorage.getTempID(), cards[1], false);
                CardUtility.turnCard(tV_card1, cards[1]);
                onAnswer(answer);
                break;
            case LAP1C:
                break;
            case LAP1D:
                break;
            default:
                //ERROR
        }

    }

    public void onClick_feedback(View view) {
        switch (playersStorage.getState()) {
            case LAP1A:
                gamePlayService.nextPlayer(1, playersStorage.getTempID(), answer);
                break;
            case LAP1B:
                gamePlayService.nextPlayer(2, playersStorage.getTempID(), answer);
                break;
            case LAP1C:
                gamePlayService.nextPlayer(3, playersStorage.getTempID(), answer);
                break;
            case LAP1D:
                gamePlayService.nextPlayer(4, playersStorage.getTempID(), answer);
                break;
            default:
                //ERROR
        }
        onPauseMode();
    }
    private void updateScoreButton(int score){
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                btn_score.setText("Score: "+score);
            }
        });
    }

    @Override   //Callback - executed when receiving
    public void onUpdateMessage() {
        System.out.println("It is the turn of  player: " + playersStorage.getCurrentTurn() + " !!!!!!!!");

        updateScoreButton(playersStorage.getScore().get(playersStorage.getTempID()));
        if (playersStorage.getCurrentTurn() == 0) {
            //This means that every player has finished the turn of the current round and the next round can be started
            //NEXT LAP
            boolean end = nextGameState();
            if (end) {
                //After all Guess-Rounds start Pyramid:
                Intent i = new Intent(GuessActivity.this, MainMenuActivity.class);  //Change this to Pyramid Round
                startActivity(i);
            }
        }

        if (playersStorage.getCurrentTurn() == playersStorage.getTempID()) {    //this players turn
            onPlayMode();
            //when it is my turn, the cought button is Invisible
            bt_cought.setVisibility(View.INVISIBLE);
        } else {
            onPauseMode();
            //when it is not my turn, the cought button is Visible
            //bt_cought.setVisibility(View.VISIBLE);
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

    private void onPauseMode() {
        tV_guessQuestion.setText("wait till it is your turn..");   //Extend this to "it's playernames turn"
        bt_FirstOption.setClickable(false);
        bt_SecondOption.setClickable(false);
        bt_FirstOption.setBackgroundResource(R.drawable.bg_btn_gray);
        bt_SecondOption.setBackgroundResource(R.drawable.bg_btn_gray);

        tV_card1.setTextColor(Color.GRAY);
        tV_card2.setTextColor(Color.GRAY);
        tV_card3.setTextColor(Color.GRAY);
        tV_card4.setTextColor(Color.GRAY);

        tV_feedback.setVisibility(View.INVISIBLE);

    }

    private void onPlayMode() {
        switch (playersStorage.getState()) {
            case LAP1A:
                tV_guessQuestion.setText("Guess if the first card is red or black");
                bt_FirstOption.setBackgroundResource(R.drawable.bg_btn_black);
                bt_SecondOption.setBackgroundResource(R.drawable.bg_btn_red);
                break;
            case LAP1B:
                tV_guessQuestion.setText("Guess if the second cards rank is higher or lower than first cards rank.");
                bt_FirstOption.setBackgroundResource(R.drawable.bg_btn_black);
                bt_SecondOption.setBackgroundResource(R.drawable.bg_btn_black);
                bt_FirstOption.setText("Higher");
                bt_SecondOption.setText("Lower");
                break;
            case LAP1C:

                break;
            case LAP1D:

                break;
            default:
                //ERROR
        }
        bt_FirstOption.setClickable(true);
        bt_SecondOption.setClickable(true);
        bt_FirstOption.setVisibility(View.VISIBLE);
        bt_SecondOption.setVisibility(View.VISIBLE);
        tV_card1.setTextColor(Color.parseColor("#000000"));
        tV_card2.setTextColor(Color.parseColor("#000000"));
        tV_card3.setTextColor(Color.parseColor("#000000"));
        tV_card4.setTextColor(Color.parseColor("#000000"));
    }

    private void onAnswer(boolean answer) {
        if (answer) {
            tV_feedback.setText("Correct Answer\n[-OK-]");
        } else {
            tV_feedback.setText("Wrong Answer\n[-OK-]");

        }
        tV_feedback.setVisibility(View.VISIBLE);
        bt_FirstOption.setVisibility(View.INVISIBLE);
        bt_SecondOption.setVisibility(View.INVISIBLE);
    }

    private boolean nextGameState() {
        switch (playersStorage.getState()) {
            case LAP1A:
                playersStorage.setState(GameState.LAP1B);
                break;
            case LAP1B:
                playersStorage.setState(GameState.LAP1C);
                break;
            case LAP1C:
                playersStorage.setState(GameState.LAP1D);
                break;
            case LAP1D:
                playersStorage.setState(GameState.LAP2);//Pyramid Round
                return true;
            default:
                //ERROR
        }
        return false;

    }


    // removes android status bar on top, for fullscreen
    private void hideAppTitleBar() {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


}

    