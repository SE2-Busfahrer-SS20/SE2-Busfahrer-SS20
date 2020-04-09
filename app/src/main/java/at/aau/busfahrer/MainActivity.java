package at.aau.busfahrer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import at.aau.busfahrer.presentation.GuessActivity;

public class MainActivity extends AppCompatActivity {


    Button bt_start;    //This button is only for test puropose to open GuessActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bt_start = findViewById(R.id.bt_start);

        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open GuessActivity
                Intent intent = new Intent(MainActivity.this, at.aau.busfahrer.presentation.GuessActivity.class);
                startActivity(intent);
            }
        });
    }
}
