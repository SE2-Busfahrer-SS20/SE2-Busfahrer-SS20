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

import com.esotericsoftware.minlog.Log;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class MainMenuActivity extends AppCompatActivity {
    GameService gamesvc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideAppTitleBar();
        setContentView(R.layout.activity_main_menu);
        gamesvc = GameServiceImpl.getInstance();
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

        gamesvc.playGame(name, getMacAddr());

        Intent i = new Intent(MainMenuActivity.this, SelectCheatsActivity.class);
        startActivity(i);

    }


    // remove status bar on top, fullscreen mode.
    private void hideAppTitleBar(){
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    private  String getMacAddr() {
        //This code was found on StackOverFlow:
            //https://stackoverflow.com/questions/33159224/getting-mac-address-in-android-6-0
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception e) {
            Log.error(e.toString());
        }
        return "";
    }

    public void openPLab(View v) {
        Intent i = new Intent(MainMenuActivity.this, PLabActivity.class);
        startActivity(i);
    }
}
