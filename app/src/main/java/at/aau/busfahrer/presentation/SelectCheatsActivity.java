package at.aau.busfahrer.presentation;
import at.aau.busfahrer.*;
import at.aau.busfahrer.service.CheatService;
import at.aau.busfahrer.service.GamePlayService;
import at.aau.busfahrer.service.impl.CheatServiceImpl;
import at.aau.busfahrer.service.impl.GamePlayServiceImpl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.esotericsoftware.minlog.Log;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused")
public class SelectCheatsActivity extends AppCompatActivity {

    private Button light;
    private Button shake;
    private Button fair;
    private int sensortype = -1;
    private GamePlayService gamesvc = GamePlayServiceImpl.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideAppTitleBar();
        setContentView(R.layout.activity_select_cheats);
        light = findViewById(R.id.button3);
        shake = findViewById(R.id.button6);
        fair = findViewById(R.id.button7);
    }

    public void onClickStart(View v){
        if(sensortype != -1){
            SharedPreferences sharedPreferences = getSharedPreferences("shared_preferences",MODE_PRIVATE);
            String name=sharedPreferences.getString("Player","name");
            String hostname=sharedPreferences.getString("HostName","127.0.0.1");
            gamesvc.setHostName(hostname);
            gamesvc.playGame(name, getMacAddr());

            CheatService cheatService = CheatServiceImpl.getInstance();
            cheatService.setSensorType(sensortype);
            Intent i = new Intent(SelectCheatsActivity.this, WaitActivity.class);
            startActivity(i);
        }else{
            Toast.makeText(SelectCheatsActivity.this, "Select Cheat first", Toast.LENGTH_SHORT).show();
        }
    }


    public void onRadioButtonClicked(View view){
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
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
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


}
