package at.aau.busfahrer.presentation;
import at.aau.busfahrer.*;
import at.aau.busfahrer.service.GameService;
import at.aau.busfahrer.service.impl.GameServiceImpl;
import shared.model.impl.playersStorage;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class WaitActivity extends AppCompatActivity {

    GameService gamesvc;
    private boolean wait=true;
    Thread updatePlayerList = new Thread(){
        public void run(){
            TextView[] players =new TextView[8];
             players[0] = (TextView) findViewById(R.id.playerName1);
             players[1] = (TextView) findViewById(R.id.playerName2);
             players[2] = (TextView) findViewById(R.id.playerName3);
             players[3] = (TextView) findViewById(R.id.playerName4);
             players[4] = (TextView) findViewById(R.id.playerName5);
             players[5] = (TextView) findViewById(R.id.playerName6);
             players[6] = (TextView) findViewById(R.id.playerName7);
             players[7] = (TextView) findViewById(R.id.playerName8);
            ArrayList<String> playerNames;
            //Busy Waiting
            while(wait){
              playerNames=playersStorage.getPlayerNames();
              int size=playerNames.size();
              for(int i=0; i<8 && i<size ;i++){
                  if(playerNames.get(i)!=null) {
                      players[i].setText(playerNames.get(i));
                  }
              }
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideAppTitleBar();
        setContentView(R.layout.activity_wait);

        gamesvc = GameServiceImpl.getInstance();

        //Change visability
        LinearLayout playerList = findViewById(R.id.playerList);
        Button bt_start = findViewById(R.id.bt_start);
        ImageView logo = findViewById(R.id.logo);
        if(playersStorage.isMaster()){
            playerList.setVisibility(View.VISIBLE);
            bt_start.setVisibility(View.VISIBLE);
            logo.setVisibility(View.INVISIBLE);
        }else{
            playerList.setVisibility(View.INVISIBLE);
            bt_start.setVisibility(View.INVISIBLE);
            logo.setVisibility(View.VISIBLE);
        }

        //check if other clients join server
        updatePlayerList.start();
    }

    // click listener start game button
    public void onClickStartGame(View v){
        Intent i = new Intent(WaitActivity.this, GuessActivity.class);

        wait=false;//To terminate busy waiting

        gamesvc.startGame();
        startActivity(i);

    }

    private void hideAppTitleBar(){
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }



    static void updatePlayers(){
        //Plan: Use this method to update GUI when NewPlayerMessage is received
        //Problem: This method can't be accessed from common (Circle Dependency)
        //Lösungsansatz: Wäre es möglich den Listener auf die App zu verschieben?

        //Notlösung: Momentan wurde das Problem mit busy-waiting gelöst - ich glaube aber, dass dieses Problem noch öfters auftreten wird
    }

}
