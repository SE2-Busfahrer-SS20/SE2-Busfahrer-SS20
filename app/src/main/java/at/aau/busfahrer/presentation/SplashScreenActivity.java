package at.aau.busfahrer.presentation;
import at.aau.busfahrer.*;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;



public class SplashScreenActivity extends AppCompatActivity {

    /*
       Start Screen when opening the App
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideAppTitleBar();
        setContentView(R.layout.activity_splash_screen);
        handleScreenSplash();
    }

    // checks if a player is already known
    // TODO: replace with database
    public boolean isPlayerRegistered(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared_preferences",MODE_PRIVATE);
        return sharedPreferences.getString("Player", null) != null;
    }

    // showing the Logo for 2 sec AND check if existing player
    public void handleScreenSplash(){
        new Handler().postDelayed(() -> {
            Intent i;
            if (!isPlayerRegistered()) {
                i = new Intent(SplashScreenActivity.this,
                        RegisterActivity.class);
            }else {
                i = new Intent(SplashScreenActivity.this,
                        MainMenuActivity.class);
            }
            startActivity(i);
            finish();
        }, 2000);
    }

    // hide title bar and status bar for fullscreen mode
    private void hideAppTitleBar(){
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View decorView = getWindow().getDecorView();
        ((View) decorView).setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
        // notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


}
