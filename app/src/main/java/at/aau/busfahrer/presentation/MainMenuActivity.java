package at.aau.busfahrer.presentation;
import at.aau.busfahrer.*;
import at.aau.busfahrer.service.GameService;
import at.aau.busfahrer.service.impl.GameServiceImpl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import static shared.networking.kryonet.NetworkConstants.host;

public class MainMenuActivity extends AppCompatActivity {
    GameService gamesvc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideAppTitleBar();
        setContentView(R.layout.activity_main_menu);
        gamesvc = new GameServiceImpl(host);
    }

      // click listener about button
    public void onClickAbout(View v){
        AboutFragment aboutFragment = new AboutFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("menu")
                .replace(R.id.about_fragment,aboutFragment,"ABOUT_FRAGMENT")
                .commit();
    }

    // click listener reportBug button
    public void onClickReport(View v){
        ReportFragment reportFragment = new ReportFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("menu")
                .replace(R.id.report_fragment,reportFragment,"REPORT_FRAGMENT")
                .commit();
    }

    // click listener help button
    public void onClickHelp(View v){
        HelpFragment helpFragment = new HelpFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("menu")
                .replace(R.id.help_fragment ,helpFragment,"REPORT_FRAGMENT")
                .commit();
    }

    // click listener startServer button
    public void onStartServer(View v){
        Intent i = new Intent(MainMenuActivity.this,StartServerActivity.class);
        startActivity(i);
    }

    // click listener PlayerEdit button
    public void onClickEditPlayer(View v){
        Intent i = new Intent(MainMenuActivity.this, EditPlayerActivity.class);
        startActivity(i);
    }
    // click listener PlayerEdit button
    public void onClickPlayGame(View v){

        //SEND REGISTERMESSAGE TO SERVER
        SharedPreferences sharedPreferences = getSharedPreferences("shared_preferences",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String name=sharedPreferences.getString("Player","name");

        gamesvc.playGame(name);

        Intent i = new Intent(MainMenuActivity.this, WaitActivity.class);
        startActivity(i);



    }


    // remove status bar on top, fullscreen mode.
    private void hideAppTitleBar(){
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
