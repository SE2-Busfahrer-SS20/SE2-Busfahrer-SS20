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
    private static final int TYPE_FAIR = 0;

    private static CheatServiceImpl instance;

    // callback for UI calls
    public interface SensorListener{
        void handle();
    }

    // sensor stuff
    private Context context;
    private SensorListener sensorListener;
    private SensorManager sensorManager;
    private int sensor_type;
    private static Sensor sensor;
    public boolean isSensorListen = false;

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

    public void setSensorType(int type){
        sensor_type = type;
    }
    public int getSensorType(){
        return sensor_type;
    }

    private void onStart(){
        Log.i(TAG,"Service started");
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensor_type == Sensor.TYPE_ACCELEROMETER){
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }else if(sensor_type == Sensor.TYPE_LIGHT){
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        }else if (sensor_type == TYPE_FAIR){
            sensor = null;
            isSensorListen = false;
        }
        if (sensor != null){
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
            isSensorListen = true;
        }
    }

    private void onPause(){
        Log.i(TAG,"Service paused");
        sensorManager.unregisterListener(this);
        isSensorListen = false;
    }

    private void onResume(){
        Log.i(TAG,"Service resume");
        sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL);
        isSensorListen = true;
    }

    private void onStop(){
        Log.i(TAG,"Service killed");
        sensorManager.unregisterListener(this);
        context = null;
        sensorListener = null;
        sensorManager = null;
        isSensorListen = false;
    }

    /*
    Sensor Event = long timestamp , float value[] x,y,z , Sensor Obj , int accuracy
    https://developer.android.com/guide/topics/sensors/sensors_overview#dont-block-the-onsensorchanged-method
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (sensor.getType()){
          case (Sensor.TYPE_ACCELEROMETER):
              getAccelerometer(event);
              break;
          case (Sensor.TYPE_LIGHT):
              getLight(event);
              break;
      }
    }

    private void getAccelerometer(SensorEvent event) {

        long timestamp = event.timestamp; // in nanoseconds
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        // calculate g force
        float accelerationSquareRoot = (x * x + y * y + z * z)
                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        if (accelerationSquareRoot >= 2){
            System.out.println("Shake detected");
        }else{
            return;
        }
    }

    private void getLight(SensorEvent event){
        // sensor default value ~5 daylight
        // todo: sensor sensitivity too high
        float lux = event.values[0];
        if(lux < 5){
            System.out.println("dark");
        }else{
            return;
        }
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }
    public Context getContext() {
        return context;
    }
    @Override
    public void startListen() {
        onStart();
    }
    @Override
    public void pauseListen() {
        if(sensor_type == TYPE_FAIR){

        }
        onPause();
    }
    @Override
    public void resumeListen() {
        if(sensor_type == TYPE_FAIR){

        }
        onResume();
    }
    @Override
    public void stopListen() {
        if(sensor_type == TYPE_FAIR){

        }
        onStop();
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

}
