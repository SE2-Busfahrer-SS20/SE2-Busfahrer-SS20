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

public class EditPlayerActivity extends AppCompatActivity {

    EditText editText;
    EditText editHostName;
    long lastClick;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideAppTitleBar();
        setContentView(R.layout.activity_edit_player);
        SharedPreferences sharedPreferences = getSharedPreferences("shared_preferences",MODE_PRIVATE);
        editText = findViewById(R.id.EditPlayerText);
        editText.setText(sharedPreferences.getString("Player",null));
        editHostName = findViewById(R.id.EditHostText);
        editHostName.setText(sharedPreferences.getString("HostName","127.0.0.1"));
    }

    // removes android status bar on top, for fullscreen
    private void hideAppTitleBar(){
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    // button click listener
    public void onClickChange(View v){
        if((System.currentTimeMillis() - lastClick) > 2000 ) {
            lastClick = System.currentTimeMillis();
            String text = editText.getText().toString();
            if ((text.trim().length() < 3) || (text.trim().length() > 24))  {
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.error_toast,
                        (ViewGroup) findViewById(R.id.error_toast));
                TextView toastText = layout.findViewById(R.id.textToast);
                toastText.setText("Your name must be at least 3 and max 24 characters long ");
                toast = new Toast(this);
                toast.setGravity(Gravity.TOP, 0, 300);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();
            } else {
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.success_toast,
                        (ViewGroup) findViewById(R.id.success_toast));
                TextView toastText = layout.findViewById(R.id.textToastSc);
                toastText.setText("Username successfully changed");
                toast = new Toast(this);
                toast.setGravity(Gravity.TOP, 0, 300);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();
                saveData(editText.getText().toString().trim(), editHostName.getText().toString().trim());
            }
        }
    }

    public void onClickChangeCancel(View v) {
        Intent i = new Intent(EditPlayerActivity.this, MainMenuActivity.class);
        startActivity(i);
    }

    private void saveData(String name, String hostname){
        // saving player name to shared prefs
        SharedPreferences sharedPreferences = getSharedPreferences("shared_preferences",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Player",name); // key, value
        editor.putString("HostName",hostname);
        editor.apply();
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
