package at.aau.busfahrer.presentation;
import at.aau.busfahrer.*;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class SelectCheatsActivity extends AppCompatActivity {

    Button bt_start;    //This button is only for test puropose to open GuessActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideAppTitleBar();
        setContentView(R.layout.activity_select_cheats);

    }

    public void onClickStart(View v){
        Intent i = new Intent(SelectCheatsActivity.this, WaitActivity.class);
        startActivity(i);
    }

    private void hideAppTitleBar(){
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
