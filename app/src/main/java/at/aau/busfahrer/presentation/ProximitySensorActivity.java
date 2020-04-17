package at.aau.busfahrer.presentation;
import at.aau.busfahrer.*;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;


public class ProximitySensorActivity extends AppCompatActivity implements SensorEventListener {

    private boolean vorhanden;  // Für das Überprüfen ob das Gerät einen Sensor hat oder nicht
    TextView text;
    SensorManager sm;
    private Sensor annaeherungssensor;
    private float d;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proximity_sensor);
        text= (TextView) findViewById(R.id.textView);

        if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_PROXIMITY)) {
            vorhanden= true;
            sm= (SensorManager) getSystemService(ProximitySensorActivity.this.SENSOR_SERVICE);
            annaeherungssensor = sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            d= annaeherungssensor.getMaximumRange(); // Für die maximale Entfernung

        } else {
            Toast.makeText(getApplicationContext(), "Not available",Toast.LENGTH_SHORT).show();
        }
    }

    // Sensor regestrieren in onResume Methode
    @Override
    protected void onResume() {
        super.onResume();
        if(vorhanden){     // nochmal überprüfen ob Sensor vorhanden ist, sonst muss ich ihn erst garnicht regestrieren
            sm.registerListener(this,annaeherungssensor,sm.SENSOR_DELAY_NORMAL);
        }
    }

    // Wenn die App in den Hintergrund tritt, bsp um Strom zu sparen dann den Sensor entfernen
    @Override
    protected void onPause() {
        super.onPause();
        if(vorhanden){
            sm.unregisterListener(ProximitySensorActivity.this);
        }
    }

    // Diese 2 Methoden braucht man für den SensorEventListener

    //Wird ausgeführt wenn sich der Wert des Sensors geändert hat

    @Override
    public void onSensorChanged(SensorEvent event) {

        float di= event.values [0]; // Bekomme die Entfernung

        if(di<d){ // Überprüfen ob die Entfernung kleiner ist als die weitesete Entfernung
            text.setText("Hallo");
        } else{
            text.setText("Ciao");

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
