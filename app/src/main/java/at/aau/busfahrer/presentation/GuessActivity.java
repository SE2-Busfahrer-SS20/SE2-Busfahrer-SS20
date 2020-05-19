package at.aau.busfahrer.presentation;

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
import shared.model.impl.PlayersStorageImpl;


public class GuessActivity extends AppCompatActivity implements GuessRoundListener {
    private Card[] cards;
    private PlayersStorageImpl playersStorage = PlayersStorageImpl.getInstance();
    private GamePlayService gamePlayService = GamePlayServiceImpl.getInstance();

    private Button bt_cought;
    private Button bt_FirstOption;
    private Button bt_SecondOption;
    private Button bt_Spade;
    private Button bt_Heart;
    private Button bt_Diamond;
    private Button bt_Club;

    private TextView tV_guessQuestion;
    private TextView tV_feedback;
    private TextView tV_card1;
    private TextView tV_card2;
    private TextView tV_card3;
    private TextView tV_card4;
    private TextView tV_erwischt;

    private boolean scored;
    private CheatService cheatService;
    private CoughtServiceImpl coughtService;

    ///

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideAppTitleBar();
        setContentView(R.layout.activity_guess);

        //Visibility
        bt_FirstOption = findViewById(R.id.bt_FirstOption);
        bt_SecondOption = findViewById(R.id.bt_SecondOption);
        bt_Spade=findViewById(R.id.bt_Spade);
        bt_Heart=findViewById(R.id.bt_Heart);
        bt_Diamond=findViewById(R.id.bt_Diamond);
        bt_Club=findViewById(R.id.bt_Club);

        tV_guessQuestion = findViewById(R.id.tV_guessQuestion);
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
            //bt_cought.setVisibility(View.VISIBLE); //moved to onPauseMode()
        } else {
            onPlayMode();
            //bt_cought.setVisibility(View.INVISIBLE); //moved to onPauseMode()
        }

        //Cheat Service
        cheatService = CheatServiceImpl.getInstance();
        cheatService.setContext(getApplicationContext(), getClass().getName());
        cheatService.startListen();
        //handleCheat(); //this coursed error

        //Register Callback
        playersStorage.registerGuessRoundListener(this);

        ///SCHUMMEL - Aufdeckfunktion
        //Only when I am cheating, the Text View is could be visbible
        tV_erwischt.setVisibility(View.INVISIBLE);
    }


    public void onClick_btCought(View view) {
        //if the current player was cheating, he gets one point and the textView will be visible
        if (coughtService.isCheating() == true) {
            //TextView "Erwischt!"
            if (playersStorage.getCurrentTurn() == playersStorage.getTempID()) {
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
                scored = gamePlayService.guessColor(playersStorage.getTempID(), cards[0], true);
                CardUtility.turnCard(tV_card1, cards[0]);
                break;
            case LAP1B:
                scored = gamePlayService.guessHigherLower(playersStorage.getTempID(), cards[1], cards[0], true);
                CardUtility.turnCard(tV_card2, cards[1]);
                break;
            case LAP1C:
                scored = gamePlayService.guessBetweenOutside(playersStorage.getTempID(), cards[2], cards[0], cards[1] ,true);
                CardUtility.turnCard(tV_card3, cards[2]);
                break;
            case LAP1D:
                break;
            default:
                //ERROR
        }
        onAnswer(scored);
    }

    public void onClick_SecondOption(View view) {
        switch (playersStorage.getState()) {
            case LAP1A:
                scored = gamePlayService.guessColor(playersStorage.getTempID(), cards[0], false);
                CardUtility.turnCard(tV_card1, cards[0]);
                break;
            case LAP1B:
                scored = gamePlayService.guessHigherLower(playersStorage.getTempID(), cards[1], cards[0], false);
                CardUtility.turnCard(tV_card2, cards[1]);
                break;
            case LAP1C:
                scored = gamePlayService.guessBetweenOutside(playersStorage.getTempID(), cards[2], cards[0], cards[1] ,false);
                CardUtility.turnCard(tV_card3, cards[2]);
                break;
            case LAP1D:
                break;
            default:
                //ERROR
        }
        onAnswer(scored);

    }

    //for round 4
    public void onClick_Spade(View view) {
        scored=gamePlayService.guessSuit(playersStorage.getTempID(), cards[3], 0);
        CardUtility.turnCard(tV_card4, cards[3]);
        onAnswer(scored);
    }
    public void onClick_Heart(View view) {
        scored=gamePlayService.guessSuit(playersStorage.getTempID(), cards[3], 1);
        CardUtility.turnCard(tV_card4, cards[3]);
        onAnswer(scored);
    }
    public void onClick_Diamond(View view) {
        scored=gamePlayService.guessSuit(playersStorage.getTempID(), cards[3], 2);
        CardUtility.turnCard(tV_card4, cards[3]);
        onAnswer(scored);
    }
    public void onClick_Club(View view) {
        scored=gamePlayService.guessSuit(playersStorage.getTempID(), cards[3], 3);
        CardUtility.turnCard(tV_card4, cards[3]);
        onAnswer(scored);
    }

    public void onClick_feedback(View view) {

        switch (playersStorage.getState()) {
            //nextPlayer sends DTO playedMessage to Server
            case LAP1A:
                gamePlayService.nextPlayer(1, playersStorage.getTempID(), scored);
                break;
            case LAP1B:
                gamePlayService.nextPlayer(2, playersStorage.getTempID(), scored);
                break;
            case LAP1C:
                gamePlayService.nextPlayer(3, playersStorage.getTempID(), scored);
                break;
            case LAP1D:
                gamePlayService.nextPlayer(4, playersStorage.getTempID(), scored);
                break;
            default:
                //ERROR
        }
        onPauseMode();
    }

    @Override   //Callback - executed when receiving
    public void onUpdateMessage() {

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
            //bt_cought.setVisibility(View.INVISIBLE); //moved to onPlayMode();
        } else {
            onPauseMode();
            //when it is not my turn, the cought button is Visible
            //bt_cought.setVisibility(View.VISIBLE); //moved to onPauseMode();
        }
        //update Score in UI (feature does not exist yet)

    }

    private void onPauseMode() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tV_guessQuestion.setText("wait till it is your turn..");   //Extend this to "it's playernames turn"
                bt_FirstOption.setVisibility(View.INVISIBLE);
                bt_SecondOption.setVisibility(View.INVISIBLE);
                bt_Spade.setVisibility(View.INVISIBLE);
                bt_Heart.setVisibility(View.INVISIBLE);
                bt_Diamond.setVisibility(View.INVISIBLE);
                bt_Club.setVisibility(View.INVISIBLE);

                bt_FirstOption.setBackgroundResource(R.drawable.bg_btn_gray);
                bt_SecondOption.setBackgroundResource(R.drawable.bg_btn_gray);

                tV_card1.setTextColor(Color.GRAY);
                tV_card2.setTextColor(Color.GRAY);
                tV_card3.setTextColor(Color.GRAY);
                tV_card4.setTextColor(Color.GRAY);
                tV_feedback.setVisibility(View.INVISIBLE);

                bt_cought.setVisibility(View.VISIBLE);
            }
        });
    }

    private void onPlayMode() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bt_Spade.setVisibility(View.INVISIBLE);
                bt_Heart.setVisibility(View.INVISIBLE);
                bt_Diamond.setVisibility(View.INVISIBLE);
                bt_Club.setVisibility(View.INVISIBLE);
                bt_FirstOption.setVisibility(View.VISIBLE);
                bt_SecondOption.setVisibility(View.VISIBLE);
                tV_card1.setTextColor(Color.parseColor("#000000"));
                tV_card2.setTextColor(Color.parseColor("#000000"));
                tV_card3.setTextColor(Color.parseColor("#000000"));
                tV_card4.setTextColor(Color.parseColor("#000000"));

                bt_cought.setVisibility(View.INVISIBLE);

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
                        tV_guessQuestion.setText("Guess if the third cards rank is between or outside the first and second card.");
                        bt_FirstOption.setBackgroundResource(R.drawable.bg_btn_black);
                        bt_SecondOption.setBackgroundResource(R.drawable.bg_btn_black);
                        bt_FirstOption.setText("Between");
                        bt_SecondOption.setText("Outside");
                        break;
                    case LAP1D:
                        tV_guessQuestion.setText("Guess the fouth cards suit");
                        bt_Spade.setVisibility(View.VISIBLE);
                        bt_Heart.setVisibility(View.VISIBLE);
                        bt_Diamond.setVisibility(View.VISIBLE);
                        bt_Club.setVisibility(View.VISIBLE);
                        bt_FirstOption.setVisibility(View.INVISIBLE);
                        bt_SecondOption.setVisibility(View.INVISIBLE);
                        break;
                }
            }
        });
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
        bt_Spade.setVisibility(View.INVISIBLE);
        bt_Heart.setVisibility(View.INVISIBLE);
        bt_Diamond.setVisibility(View.INVISIBLE);
        bt_Club.setVisibility(View.INVISIBLE);
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

}

    