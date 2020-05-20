package at.aau.busfahrer.service.impl;
import at.aau.busfahrer.service.CheatService;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

import android.util.Log;
import java.util.concurrent.TimeUnit;

public class CheatServiceImpl implements CheatService {

    private static final String TAG = "CheatServiceImpl";
    private static final int TYPE_FAIR = 0;

    @SuppressLint("StaticFieldLeak") // using ApplicationContext solves this problem, context is not saved.
    private static CheatServiceImpl instance;

    public interface SensorListener{
        void handle();
    }

    private Context context;
    private SensorListener sensorListener;
    private SensorManager sensorManager;
    private int sensorType;
    private Sensor sensor;
    private String activityName;
    private long lastUpdateMs;
    public boolean isSensorListen = false;

    @SuppressWarnings("unused")
    private CheatServiceImpl(){};

    public static CheatServiceImpl getInstance(){
        if(CheatServiceImpl.instance == null){
            CheatServiceImpl.instance = new CheatServiceImpl();
        }
        return CheatServiceImpl.instance;
    }

    public void setSensorListener(SensorListener sensorListener){
        this.sensorListener = sensorListener;
    }

    private void onStart(){
        Log.i(TAG,"Service started");
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if(sensorManager != null){
            switch (sensorType) {
                case Sensor.TYPE_ACCELEROMETER:
                    sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                    break;
                case Sensor.TYPE_LIGHT:
                    sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
                    break;
                case TYPE_FAIR:
                    sensor = null;
                    sensorManager = null;
                    isSensorListen = false;
                    playFairMode();
                    break;
                default:
                    Log.e(TAG, "Sensor not found");
                    break;
            }
            if (sensor != null){
                sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
                isSensorListen = true;
            }
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

    private void playFairMode(){
        // handle fair mode ...
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            getAccelerometer(event);
        }else if(sensor.getType() == Sensor.TYPE_LIGHT){
            getLight(event);
        }
    }

    private void getAccelerometer(SensorEvent event) {
        long timestamp = TimeUnit.MILLISECONDS.convert(event.timestamp, TimeUnit.NANOSECONDS);
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        if((timestamp - lastUpdateMs) > 500){
        float accelerationSqrt = (x * x + y * y + z * z)
                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        if (accelerationSqrt >= 4){
            lastUpdateMs = timestamp;
            Log.i(TAG,"Shake detected");
            sensorListener.handle();
        }else{
            lastUpdateMs = timestamp;
            return;
        }
        }
    }

    private void getLight(SensorEvent event){
        long timestamp = TimeUnit.MILLISECONDS.convert(event.timestamp, TimeUnit.NANOSECONDS);
        float lux = event.values[0];

        if((timestamp - lastUpdateMs) > 800) {
            if (lux < 5) {
                lastUpdateMs = timestamp;
                Log.i(TAG,"Dark");
                sensorListener.handle();
            } else {
                lastUpdateMs = timestamp;
            }
        }
    }
    public void setSensorType(int type){
        sensorType = type;
    }
    public int getSensorType(){
        return sensorType;
    }
    @Override
    public void setContext(Context context, String name) {
        this.context = context;
        this.activityName = name;
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
        if(sensorType != TYPE_FAIR){
            onPause();
        }
    }
    @Override
    public void resumeListen() {
        if(sensorType != TYPE_FAIR){
            onResume();
        }
    }
    @Override
    public void stopListen() {
        if(sensorType != TYPE_FAIR){
            onStop();
        }
    }
    @Override @SuppressWarnings("unused")
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // NOT NEEDED
    }

}
