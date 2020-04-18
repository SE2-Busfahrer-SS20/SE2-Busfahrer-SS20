package at.aau.busfahrer.presentation;
import at.aau.busfahrer.*;
import at.aau.busfahrer.service.impl.GameService;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class StartServerActivity extends AppCompatActivity {

    EditText name;
    Spinner spinner;
    Integer playercount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideAppTitleBar();
        setContentView(R.layout.activity_start_server);
        initSpinnerDropDown();
        name = findViewById(R.id.server_name);
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
        new GameService().createGame2(playercount,name.toString());

    }

    // removes android status bar on top, for fullscreen
    private void hideAppTitleBar(){
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
