package at.aau.busfahrer.presentation;
import at.aau.busfahrer.*;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    EditText spielername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideAppTitleBar();
        setContentView(R.layout.activity_register);
        spielername = findViewById(R.id.RegisterEditPlayerName);

    }

    public void onClickCreate(View v){
        saveData(spielername.getText().toString());
        Intent i = new Intent(RegisterActivity.this, MainMenuActivity.class);
        startActivity(i);
    }
    private void saveData(String name){
        // saving player name to shared prefs
        SharedPreferences sharedPreferences = getSharedPreferences("shared_preferences",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Player",name); // key, value
        editor.apply();
    }
    private void hideAppTitleBar(){
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
