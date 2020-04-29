package at.aau.busfahrer.presentation;
import at.aau.busfahrer.*;
import at.aau.busfahrer.service.GameService;
import at.aau.busfahrer.service.impl.GameServiceImpl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import static shared.networking.kryonet.NetworkConstants.host;//Das oder ganzes Interface löschen

public class JoinServerActivity extends AppCompatActivity {
    GameService gamesvc;//Das oder ganzes Interface löschen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideAppTitleBar();
        setContentView(R.layout.activity_join_server);

        gamesvc = new GameServiceImpl(host);//Das oder ganzes Interface löschen
    }

    // click listener beitreten button
    public void onClickBeitreten(View v){

        Log.d("Join Button", "pressed");

        Intent i = new Intent(JoinServerActivity.this, at.aau.busfahrer.presentation.SelectCheatsActivity.class);
        startActivity(i);

        //join Game
       // gamesvc.joinGame();//Das oder ganzes Interface löschen
    }

    private void hideAppTitleBar(){
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
