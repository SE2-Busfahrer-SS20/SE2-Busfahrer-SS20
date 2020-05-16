package at.aau.busfahrer.presentation;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import at.aau.busfahrer.service.GameService;
import at.aau.busfahrer.service.impl.CheatServiceImpl;
import at.aau.busfahrer.service.impl.GameServiceImpl;

import shared.model.Card;
import shared.model.GameState;
import shared.model.GuessRoundListener;
import shared.model.Player;
import shared.model.impl.GameImpl;
import shared.model.impl.PlayersStorageImpl;
import shared.networking.dto.PlayedMessage;


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
    private Player playerCheated;
    private Player myself;
    private int scoreCheater;
    private  int myScore;
    private PlayedMessage pl;
    private int indexOfMe;
    private TextView tV_erwischt;

    private CheatServiceImpl cheatService;



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
        tV_erwischt = findViewById(R.id.txtView_erwischt);

        // Cheat Service
        cheatService = CheatServiceImpl.getInstance();
        cheatService.setContext(getApplicationContext(), getClass().getName());
        cheatService.startListen();
        handleCheat();

        cards= playersStorage.getCards();
        if(!playersStorage.isMaster()){
            onPauseMode();
        }
        //Register Callback
        playersStorage.registerGuessRoundListener(this);
        //Only when I am cheating, the Text View is could be visbible
        tV_erwischt.setVisibility(View.INVISIBLE);


    }




    public void onClick_btCought(View view) {
        //Check wich player's turn it is
        playerList = gameImpl.getPlayerList();
        //get the index of the curren player on the playerList
        currentPlayer = gameImpl.getCurrentPlayer();
        //get the Index of myself from the player list
        indexOfMe = pl.getTempID();

        myself = playerList.get(indexOfMe);
        playerCheated = playerList.get(currentPlayer);
        //if the currentplayer has cheated, he get one point and i lose one point
        if (playerCheated.isCheatedThisRound() == true){
            //TextView "Erwischt!"
            tV_erwischt.setVisibility(View.VISIBLE);
            //after 5s the TextView is invisible
            tV_erwischt.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tV_erwischt.setVisibility(View.INVISIBLE);
                }
            }, 5000);
            //the player who cheated increases his score
            scoreCheater = playerCheated.getScore();
            scoreCheater++;
            playerCheated.setScore(scoreCheater);

            //myScore will be decremented one time
            myScore = myself.getScore();
            myScore--;
            myself.setScore(myScore);

        }else{
            //the player who has NOT cheated decreases his score
            scoreCheater = playerCheated.getScore();
            scoreCheater--;
            playerCheated.setScore(scoreCheater);

            //myScore will be increased one time
            myScore = myself.getScore();
            myScore++;
            myself.setScore(myScore);
        }



    }
    // handles cheating, Confirmation dialog, if player press yes --> cheatedMessage sent to server
    public void handleCheat(){
        cheatService.setSensorListener(() -> {
            cheatService.pauseListen();
            if(playersStorage.getTempID() == playersStorage.getCurrentTurn()){
                new AlertDialog.Builder(GuessActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                        // Yes
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            // sending network call
                            gameService.sendMsgCheated(playersStorage.getTempID(),true, System.currentTimeMillis(), cheatService.getSensorType());
                            CardUtility.turnCard(tV_card1, cards[0]);
                            cheatService.stopListen();
                        })
                        // No
                        .setNegativeButton(android.R.string.no, (dialog, which) -> cheatService.resumeListen())
                        .setTitle("Are you sure you want to cheat?")
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .create().show();
            }else{
                Toast.makeText(this, "Wait until your Turn starts", Toast.LENGTH_SHORT).show();
                cheatService.resumeListen();
            }
        });
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
        bt_cought.setVisibility(View.VISIBLE);

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
        bt_cought.setVisibility(View.INVISIBLE);
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
