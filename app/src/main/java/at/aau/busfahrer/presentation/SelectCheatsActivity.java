package at.aau.busfahrer.presentation;
import at.aau.busfahrer.*;
import at.aau.busfahrer.service.CheatService;
import at.aau.busfahrer.service.impl.CheatServiceImpl;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.hardware.Sensor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class SelectCheatsActivity extends AppCompatActivity {

    Button bt_start;    //This button is only for test puropose to open GuessActivity
    Button light;
    Button shake;
    Button fair;
    int sensortype = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideAppTitleBar();
        setContentView(R.layout.activity_select_cheats);
        light = findViewById(R.id.button3);
        shake = findViewById(R.id.button6);
        fair = findViewById(R.id.button7);
        bt_start = findViewById(R.id.bt_start);


        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open GuessActivity
                if(sensortype != -1){
                Intent intent = new Intent(SelectCheatsActivity.this, at.aau.busfahrer.presentation.GuessActivity.class);
                startActivity(intent);
                CheatServiceImpl cheatService = CheatServiceImpl.getInstance();
                cheatService.setSensorType(sensortype);
                }else{
                    Toast.makeText(SelectCheatsActivity.this, "Select Cheat first", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void onRadioButtonClicked(View v){
        if(light.isPressed()) {
            light.setBackgroundResource(R.drawable.bg_btn_orange);
            sensortype = Sensor.TYPE_LIGHT;
        }else{
            light.setBackgroundResource(R.drawable.bg_btn_black);
        }
        if(shake.isPressed()) {
            shake.setBackgroundResource(R.drawable.bg_btn_orange);
            sensortype = Sensor.TYPE_ACCELEROMETER;
        }else{
            shake.setBackgroundResource(R.drawable.bg_btn_black);
        }
        if(fair.isPressed()) {
            fair.setBackgroundResource(R.drawable.bg_btn_orange);
            sensortype = 0;
        }else{
            fair.setBackgroundResource(R.drawable.bg_btn_black);
        }
    }

    private void hideAppTitleBar(){
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
