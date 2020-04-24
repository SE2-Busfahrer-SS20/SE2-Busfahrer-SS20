package at.aau.busfahrer.service.impl;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.util.Log;
import at.aau.busfahrer.service.CheatService;

public class CheatServiceImpl implements CheatService {

    /*
        Zu beginn des Spieles kann sich jeder Spieler für einen der folgenden „Sensoren“ entscheiden,
        mit deren Auslösung er unbemerkt Schummeln kann.

        *Lichtsensor
        *Beschleunigungsensor
        *Annährungssensor

    https://developer.android.com/reference/android/hardware/SensorManager
     */

    private static final String TAG = "CheatServiceImpl";
    private static CheatServiceImpl instance;

    // callback for UI calls
    public interface SensorListener{
        void handle();
    }

    // sensor stuff
    private Context context;
    private SensorListener sensorListener;
    private SensorManager sensorManager;
    private static Sensor sensor;
    private long lastUpdate;

    // constructor
    private CheatServiceImpl(){};
    public static synchronized CheatServiceImpl getInstance(){
        if(CheatServiceImpl.instance == null){
            CheatServiceImpl.instance = new CheatServiceImpl();
        }
        return CheatServiceImpl.instance;
    }

    public void setSensorListener(SensorListener sensorListener){
        this.sensorListener = sensorListener;
    }

    public void setSensor(int type){
        if (type == Sensor.TYPE_ACCELEROMETER){
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }else if(type == Sensor.TYPE_LIGHT){
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        }
    }

    private void onStart(){
        Log.i(TAG,"Service started");
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void onPause(){
        Log.i(TAG,"Service paused");
        sensorManager.unregisterListener(this);
    }

    private void onResume(){
        Log.i(TAG,"Service resume");
        sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void onStop(){
        Log.i(TAG,"Service killed");
        sensorManager.unregisterListener(this);
        context = null;
        sensorListener = null;
        sensorManager = null;
    }

    /*
    Sensor Event = long timestamp , float value[] x,y,z , Sensor Obj , int accuracy
    https://developer.android.com/guide/topics/sensors/sensors_overview#dont-block-the-onsensorchanged-method
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
      switch (sensor.getType()){
          case Sensor.TYPE_ACCELEROMETER:
              lastUpdate = System.currentTimeMillis();
              getAccelerometer(event);
          case Sensor.TYPE_LIGHT:
              lastUpdate = System.currentTimeMillis();
              getLight(event);
      }
    }



    private void getAccelerometer(SensorEvent event){

        long timeNow = System.currentTimeMillis();
        long timestamp = event.timestamp;
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        // calculate g force
        float accelationSquareRoot = (x * x + y * y + z * z)
                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);

        if (accelationSquareRoot >= 3){
            if(timeNow - lastUpdate < 200){
                return;
            }
            System.out.println("Shake detected");
        }else{
            return;
        }
    }

    private void getLight(SensorEvent event){

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    public void setContext(Context context) {
        this.context = context;
    }
    @Override
    public void startListen() {
        onStart();
    }
    @Override
    public void pauseListen() {
        onPause();
    }
    @Override
    public void resumeListen() {
        onResume();
    }
    @Override
    public void stopListen() {
        onStop();
    }

}
