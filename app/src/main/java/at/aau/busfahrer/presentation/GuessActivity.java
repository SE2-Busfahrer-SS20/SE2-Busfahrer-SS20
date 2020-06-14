package at.aau.busfahrer.presentation;

import android.os.Handler;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import at.aau.busfahrer.R;
import at.aau.busfahrer.presentation.utils.CardUtility;
import at.aau.busfahrer.service.CheatService;
import at.aau.busfahrer.service.CoughtService;
import at.aau.busfahrer.service.GamePlayService;
import at.aau.busfahrer.service.impl.CheatServiceImpl;
import at.aau.busfahrer.service.impl.CoughtServiceImpl;
import at.aau.busfahrer.service.impl.GamePlayServiceImpl;
import shared.model.Card;
import shared.model.CoughtServiceListener;
import shared.model.GameState;
import shared.model.GuessRoundListener;
import shared.model.impl.PlayersStorageImpl;
import shared.networking.kryonet.NetworkClientKryo;
import com.esotericsoftware.minlog.*;

// *TODO remove card click listener in guess xml, because on click app can crash


public class GuessActivity extends AppCompatActivity implements GuessRoundListener, CoughtServiceListener {

    Handler uiHandler;
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

    private Button btn_score;

    private boolean scored;
    private CheatService cheatService;
    private CoughtService coughtService;

    final static String WHITE ="#000000";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        hideAppTitleBar();
        setContentView(R.layout.activity_guess);
        uiHandler=new Handler();
        btn_score = findViewById(R.id.bt_score);

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
        } else {
            onPlayMode();
        }

        //Cheat Service
        cheatService = CheatServiceImpl.getInstance();
        cheatService.setContext(getApplicationContext(), getClass().getName());
        cheatService.setPlayerId(playersStorage.getTempID());
        cheatService.startListen();
        handleCheat(); //this coursed error

        //Register Callback
        playersStorage.registerGuessRoundListener(this);
        NetworkClientKryo networkClientKryo = (NetworkClientKryo) NetworkClientKryo.getInstance();
        networkClientKryo.coughtCallback(this);

        ///SCHUMMEL - Aufdeckfunktion
        //Only when I am cheating, the Text View is could be visbible
        coughtService = CoughtServiceImpl.getInstance();
        tV_erwischt.setVisibility(View.INVISIBLE);
    }

    public void onClickBtCought(View view) {
        if(coughtService.isCheating()){
            Log.i( "Player is cheating","\n\n\n"+playersStorage.isCheating(playersStorage.getCurrentTurn())+"\n\n\n");
            //TextView beim CurrentPlayer anzeigen!!!!!!!!!!!
            tV_erwischt.setText("Cheater wurde erwischt!!!");
            tV_erwischt.setVisibility(View.VISIBLE);
            //after 5s the TextView is invisible
            tV_erwischt.postDelayed(() -> tV_erwischt.setVisibility(View.INVISIBLE), 5000);
        }else{
            tV_erwischt.setText("Cheater wurde NICHT erwischt!!!");
            tV_erwischt.setVisibility(View.VISIBLE);
            //after 5s the TextView is invisible
            tV_erwischt.postDelayed(() -> tV_erwischt.setVisibility(View.INVISIBLE), 5000);
        }

    }
    public void coughtTetxViewListener(){
        runOnUiThread(() -> {
            if (playersStorage.getTempID() == playersStorage.getCurrentTurn() &&
                playersStorage.getPlayerList().get(playersStorage.getCurrentTurn()).isCheating()) {

                    tV_erwischt.setText("Erwischt!!!!");
                    tV_erwischt.setVisibility(View.VISIBLE);
                    //after 5s the TextView is invisible
                    tV_erwischt.postDelayed(() -> tV_erwischt.setVisibility(View.INVISIBLE), 5000);
                }
            updateScoreButton(playersStorage.getScoreList().get(playersStorage.getTempID()));
        });
    }


    public void onClickScore(View v){

        ScoreFragment scoreFragment = new ScoreFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("menu")
                .replace(R.id.score_fragment,scoreFragment,"SCORE_FRAGMENT")
                .commit();
    }


    // handles cheating, Confirmation dialog, if player press yes --> cheatedMessage sent to server
    public void handleCheat() {
        cheatService.setSensorListener(() -> {
            cheatService.pauseListen();
            if (playersStorage.getTempID() == playersStorage.getCurrentTurn()) {
                new AlertDialog.Builder(GuessActivity.this, R.style.AlertDialogStyleDark)
                        // Yes
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            // sending network call
                            cheatService.stopListen();
                            cheatService.sendMsgCheated(true, System.currentTimeMillis(), cheatService.getSensorType());
                            turnCardOnCheat();
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


    //First Button used in Guess round 1 to 3 as: black, higher, outside
    public void onClickFirstOption(View view) {
        switch (playersStorage.getState()) {
            case LAP1A:
                scored = gamePlayService.guessColor(cards[0], true);
                CardUtility.turnCard(tV_card1, cards[0]);
                break;
            case LAP1B:
                scored = gamePlayService.guessHigherLower(cards[1], cards[0], true);
                CardUtility.turnCard(tV_card2, cards[1]);
                break;
            case LAP1C:
                scored = gamePlayService.guessBetweenOutside(cards[2], cards[0], cards[1] ,true);
                CardUtility.turnCard(tV_card3, cards[2]);
                break;
            default:

        }
        onAnswer(scored);
    }

    //Second Button used in Guess round 1 to 3 as: red, lower, between
    public void onClickSecondOption(View view) {

        switch (playersStorage.getState()) {
            case LAP1A:
                scored = gamePlayService.guessColor(cards[0], false);
                CardUtility.turnCard(tV_card1, cards[0]);
                break;
            case LAP1B:
                scored = gamePlayService.guessHigherLower(cards[1], cards[0], false);
                CardUtility.turnCard(tV_card2, cards[1]);
                break;
            case LAP1C:
                scored = gamePlayService.guessBetweenOutside(cards[2], cards[0], cards[1] ,false);
                CardUtility.turnCard(tV_card3, cards[2]);
                break;
            default:

        }
        onAnswer(scored);
    }

    //for round 4
    public void onClickSpade(View view)    { guessSuit(0); }
    public void onClickHeart(View view)    { guessSuit(1); }
    public void onClickDiamond(View view)  { guessSuit(2); }
    public void onClickClub(View view)     { guessSuit(3); }

    private void guessSuit(int suit){
        scored=gamePlayService.guessSuit(cards[3], suit);
        CardUtility.turnCard(tV_card4, cards[3]);
        onAnswer(scored);
    }

    public void onClickFeedback(View view) {
        gamePlayService.nextPlayer(playersStorage.getState(), playersStorage.getTempID(), scored);
        onPauseMode();
    }

        
    private void updateScoreButton(int score){
        uiHandler.post(() -> btn_score.setText("Score: "+score));
    }

    @Override   //Callback - executed when receiving UpdateMessage from server (after each players turn)
    public void onUpdateMessage() {
        
        updateScoreButton(playersStorage.getScoreList().get(playersStorage.getTempID()));

        if (playersStorage.getCurrentTurn() == 0) {
            //This means that every player has finished the turn of the current round and the next round can be started
            boolean end = nextGameState();
            if (end) {
                //After all Guess-Rounds start Pyramid Activity:
                cheatService.stopListen(); // end listen for cheating in guess round
                Intent i = new Intent(GuessActivity.this, PLapActivity.class);
                startActivity(i);
            }
        }

        if (playersStorage.getCurrentTurn() == playersStorage.getTempID()) {    //this players turn
            onPlayMode();
        } else {
            onPauseMode();
        }

    }

    //This methode changes visibility of UI elements when it is not this players turn
    private void onPauseMode() {
        //Execute on runOnUIThread to enable calling this funiction in other thread

        runOnUiThread(() -> {
            String currentPlayerName=playersStorage.getPlayerName(playersStorage.getCurrentTurn());
            tV_guessQuestion.setText(currentPlayerName+" is playing.");
            bt_FirstOption.setVisibility(View.INVISIBLE);
            bt_SecondOption.setVisibility(View.INVISIBLE);
            bt_Spade.setVisibility(View.INVISIBLE);
            bt_Heart.setVisibility(View.INVISIBLE);
            bt_Diamond.setVisibility(View.INVISIBLE);
            bt_Club.setVisibility(View.INVISIBLE);
            bt_cought.setVisibility(View.VISIBLE);
            tV_feedback.setVisibility(View.INVISIBLE);
        });
    }

    //This methode changes visibility of UI elements when it is not players turn
    private void onPlayMode() {
        //Execute on runOnUIThread to enable calling this funiction in other thread
        runOnUiThread(() -> {
            bt_Spade.setVisibility(View.INVISIBLE);
            bt_Heart.setVisibility(View.INVISIBLE);
            bt_Diamond.setVisibility(View.INVISIBLE);
            bt_Club.setVisibility(View.INVISIBLE);
            bt_FirstOption.setVisibility(View.VISIBLE);
            bt_SecondOption.setVisibility(View.VISIBLE);
            bt_cought.setVisibility(View.INVISIBLE);

            tV_card1.setTextColor(Color.parseColor(WHITE));
            tV_card2.setTextColor(Color.parseColor(WHITE));
            tV_card3.setTextColor(Color.parseColor(WHITE));
            tV_card4.setTextColor(Color.parseColor(WHITE));

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
                    tV_guessQuestion.setText("Guess the fouth cards suit.");
                    bt_Spade.setVisibility(View.VISIBLE);
                    bt_Heart.setVisibility(View.VISIBLE);
                    bt_Diamond.setVisibility(View.VISIBLE);
                    bt_Club.setVisibility(View.VISIBLE);
                    bt_FirstOption.setVisibility(View.INVISIBLE);
                    bt_SecondOption.setVisibility(View.INVISIBLE);
                    break;
                default:

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

    //change GameState and return true when last guessRound was finished
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
                Log.e("Error","Error in nextGameState()");
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

    // This methods shows the card in a Dialog Alert, if the player cheats.
    public void turnCardOnCheat(){
        TextView card = new TextView(this);
        switch (playersStorage.getState()) {
            case LAP1A:
                card = CardUtility.turnCardGetView(tV_card1, cards[0]);
                break;
            case LAP1B:
                card = CardUtility.turnCardGetView(tV_card2, cards[1]);
                break;
            case LAP1C:
                card = CardUtility.turnCardGetView(tV_card3, cards[2]);
                break;
            case LAP1D:
                card = CardUtility.turnCardGetView(tV_card4, cards[3]);
                break;
            default:
                break;
        }
        card.setGravity(Gravity.CENTER);
        new AlertDialog.Builder(GuessActivity.this, R.style.AlertDialogStyle)
                .setTitle("Your next card is")
                .setView(card).setNegativeButton(android.R.string.ok, (dialog, which) -> {
                }).show();
    }

    // android lifecycle methods, needed because Android SensorListener also listen if App is in the background.
    // app is in background
    @Override
    protected void onPause() {
        super.onPause();
        cheatService.pauseListen();
    }
    // app is in foreground
    @Override
    protected void onResume() {
        super.onResume();
        cheatService.resumeListen();
    }
    // app activity is destroyed
    @Override
    protected void onDestroy() {
        super.onDestroy();
        cheatService.stopListen();
    }
}

    
