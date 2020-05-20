package at.aau.busfahrer.presentation;
import at.aau.busfahrer.*;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    EditText playerName;
    long lastClick;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideAppTitleBar();
        setContentView(R.layout.activity_register);
        playerName = findViewById(R.id.RegisterEditPlayerName);
    }
    public void onClickCreate(View v){
        if((System.currentTimeMillis() - lastClick) > 2000 ){
            lastClick = System.currentTimeMillis();
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.error_toast,
                    (ViewGroup) findViewById(R.id.error_toast));
            TextView toastText = layout.findViewById(R.id.textToast);
            toastText.setText("Your name must be at least 3 and max 24 characters long ");
            String text = playerName.getText().toString();
            if ((text.trim().length() < 3) || (text.trim().length() > 24))  {
                toast = new Toast(this);
                toast.setGravity(Gravity.TOP, 0, 300);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();
            } else {
                saveData(playerName.getText().toString().trim());
                Intent i = new Intent(RegisterActivity.this, MainMenuActivity.class);
                startActivity(i);
            }
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(toast != null){
            toast.cancel();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        if(toast != null){
            toast.cancel();
        }
    }
}
