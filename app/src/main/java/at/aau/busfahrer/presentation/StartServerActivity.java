package at.aau.busfahrer.presentation;
import at.aau.busfahrer.*;
import at.aau.busfahrer.service.GameService;
import at.aau.busfahrer.service.impl.GameServiceImpl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

//THIS ACTIVITY IS NOT USED AT ALL IN THE CURRENT VERSION
public class StartServerActivity extends AppCompatActivity {

    Spinner spinner;
    Integer playercount;
    GameService gamesvc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideAppTitleBar();
        setContentView(R.layout.activity_start_server);
        initSpinnerDropDown();

        gamesvc=GameServiceImpl.getInstance();
    }

    // ui drop down, array values in res@strings
    public void initSpinnerDropDown(){
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spieleranzahl, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    // button start
    public void startServer(View v){
        playercount = Integer.valueOf(spinner.getSelectedItem().toString());
        Intent i = new Intent(StartServerActivity.this, SelectCheatsActivity.class);
        startActivity(i);

        // START SERVER:
        gamesvc.createGame(playercount);

    }

    // removes android status bar on top, for fullscreen
    private void hideAppTitleBar(){
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
