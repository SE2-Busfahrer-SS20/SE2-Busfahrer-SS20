package at.aau.busfahrer.presentation;
import at.aau.busfahrer.*;
import at.aau.busfahrer.service.GamePlayService;
import at.aau.busfahrer.service.impl.GamePlayServiceImpl;
import shared.model.PreGameListener;
import shared.model.impl.PlayersStorageImpl;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class WaitActivity extends AppCompatActivity implements PreGameListener {
    private PlayersStorageImpl playersStorage = PlayersStorageImpl.getInstance();
    GamePlayService gamesvc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideAppTitleBar();
        setContentView(R.layout.activity_wait);

        gamesvc = GamePlayServiceImpl.getInstance();

        //Change visability
        LinearLayout playerList = findViewById(R.id.playerList);
        Button btStart = findViewById(R.id.bt_start);
        ImageView logo = findViewById(R.id.logo);
        if(playersStorage.isMaster()){
            playerList.setVisibility(View.VISIBLE);
            btStart.setVisibility(View.VISIBLE);
            logo.setVisibility(View.INVISIBLE);
            updatePlayerList();
        }else{
            playerList.setVisibility(View.INVISIBLE);
            btStart.setVisibility(View.INVISIBLE);
            logo.setVisibility(View.VISIBLE);
        }

        //registerCallback
        playersStorage.registerPreGameListener(this);
    }

    // click listener start game button
    public void onClickStartGame(View v){
        Intent i = new Intent(WaitActivity.this, GuessActivity.class);

        gamesvc.startGame();//Send StartGameMessage to other clients
        startActivity(i);

    }

    private void hideAppTitleBar(){
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    ///Callback to update Playerlist
    @Override
    public void onAdditionalPlayer() {
        updatePlayerList();
    }
    //Callback to open new Activity when Master starts the game
    @Override
    public void onGameStart(){
        Intent i = new Intent(WaitActivity.this, GuessActivity.class);
        startActivity(i);
    }

    //Update playerList in Interface
    private void updatePlayerList(){
        if(playersStorage.isMaster()) {
            TextView[] players = new TextView[8];
            players[0] = (TextView) findViewById(R.id.playerName1);
            players[1] = (TextView) findViewById(R.id.playerName2);
            players[2] = (TextView) findViewById(R.id.playerName3);
            players[3] = (TextView) findViewById(R.id.playerName4);
            players[4] = (TextView) findViewById(R.id.playerName5);
            players[5] = (TextView) findViewById(R.id.playerName6);
            players[6] = (TextView) findViewById(R.id.playerName7);
            players[7] = (TextView) findViewById(R.id.playerName8);
            ArrayList<String> playerNames;

            playerNames = playersStorage.getPlayerNamesList();
            int size = playerNames.size();
            for (int i = 0; i < 8 && i < size; i++) {
                if (playerNames.get(i) != null) {
                    players[i].setText(playerNames.get(i));
                }
            }
        }
    }

}
