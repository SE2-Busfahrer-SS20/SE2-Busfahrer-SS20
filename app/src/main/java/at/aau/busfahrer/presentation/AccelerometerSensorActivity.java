package at.aau.busfahrer.presentation;
import at.aau.busfahrer.*;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;

public class AccelerometerSensorActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sm;
    private View view;
    private long lu;
    private boolean col= false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer_sensor);
        view = findViewById(R.id.textView);
        view.setBackgroundColor(Color.WHITE);

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        lu = System.currentTimeMillis();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(event);  //Methode aufrufen
        }

    }

// Logik der Schüttelfunktiion

    private void getAccelerometer(SensorEvent event) {
        float[] values = event.values;
        // Movement
        float x = values[0];  // X-Achse für seitliche bewegung
        float y = values[1];  // für vertikale Bewegung
        float z = values[2];

        float br = (x * x + y * y + z * z)
                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);

        // Dieser Sensor ist ja sehr empfindlich, daher müssen wir die Sekunden so speichern und prüfen ob sie sich seit der letzten bewegung geändert hat
        long actualTime = System.currentTimeMillis();

        if (br >= 2)
        {

            if (actualTime -lu < 200) {
                return;
            }
            lu = actualTime;
            if (col) {
                view.setBackgroundColor(Color.GREEN);  //Beim schütteln Bildschirm grün

            } else {
                view.setBackgroundColor(Color.BLUE); // Standart blau
            }
            col = !col;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        sm.registerListener(this,sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sm.unregisterListener(this);
    }
}
