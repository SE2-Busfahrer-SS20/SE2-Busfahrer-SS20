package at.aau.busfahrer.service.impl;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.util.Log;
import at.aau.busfahrer.service.CheatService;

public class CheatServiceImpl implements CheatService {

    /*
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

    public void setSensor(Sensor sensor){
        this.sensor = sensor;
    }

    private void onStart(){
        Log.i(TAG,"Service started");
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if(sensorManager != null){
            sensorManager.registerListener(this,sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.values[0] > 12) {
            System.out.println("Sensor change detected");
            sensorListener.handle();
        }
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
