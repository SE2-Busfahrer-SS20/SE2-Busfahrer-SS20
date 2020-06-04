package at.aau.busfahrer.service.impl;

import at.aau.busfahrer.service.CheatService;
import shared.networking.NetworkClient;
import shared.networking.dto.CheatedMessage;
import shared.networking.kryonet.NetworkClientKryo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

/**
 * This Service class handles cheating using Android Sensors.
 * The player can choose between three types (Light, Accelerometer, Fair).
 * The service is started on UI by triggering SensorEvents in the game.
 * In all 3 game rounds the player has the possibility to cheat once, after one valid cheating attempt the
 * CheatService is deactivated. Every successful cheat event is sent to the Server.
 */
public class CheatServiceImpl implements CheatService {

    private static final String TAG = "CheatServiceImpl";
    public static final int TYPE_FAIR = 0;
    private boolean testMode = false;

    @SuppressLint("StaticFieldLeak")
    // using ApplicationContext solves this problem, context is not saved.
    private static CheatService instance;


    /**
     Callback for UI Update Call
     */
    public interface SensorListener {
        void handle();
    }

    private int playerId;
    private int sensorType;
    private Context context;
    private SensorListener sensorListener;
    private SensorManager sensorManager;
    private Sensor sensor;
    private long lastUpdateMs;
    private boolean isSensorListen = false;

    private CheatServiceImpl() {
    }

    public static CheatService getInstance() {
        if (instance == null) {
            instance = new CheatServiceImpl();
        }
        return instance;
    }

    /**
     * Register a callback for ui calls
     * @param sensorListener UI Callback
     */
    public void setSensorListener(SensorListener sensorListener) {
        this.sensorListener = sensorListener;
    }

    /**
     * Initializes the CheatService including
     * SensorManager, Sensor, cheatType and then start listening for sensor events.
     */
    private void onStart() {
        Log.i(TAG, "Service started");
        if(!testMode){
            sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        }
        if (sensorManager != null) {
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
                    break;
                default:
                    Log.e(TAG, "Sensor not found");
                    break;
            }
            if (sensor != null) {
                sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
                isSensorListen = true;
            }
        }
    }

    /**
    Pause listening for sensor events, needed for synchronize with android lifecycle.
     */
    private void onPause() {
        Log.i(TAG, "Service paused");
        if (isSensorListen && sensorManager != null) {
            sensorManager.unregisterListener(this);
            isSensorListen = false;
        }
    }

    /**
     Resume listening for sensor events, needed for synchronize with android lifecycle.
     */
    private void onResume() {
        Log.i(TAG, "Service resume");
        if (!isSensorListen && sensorManager != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
            isSensorListen = true;
        }
    }

    /**
        Kills the CheatService, stop listening.
     */
    private void onStop() {
        Log.i(TAG, "Service killed");
        if (isSensorListen && sensorManager != null) {
            sensorManager.unregisterListener(this);
            context = null;
            sensorListener = null;
            sensorManager = null;
            isSensorListen = false;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(event.values, event.timestamp);
        } else if (sensor.getType() == Sensor.TYPE_LIGHT) {
            getLight(event.values, event.timestamp);
        }
    }

    /**
    This is triggered when a accelerometer sensor event fires. The method checks if the event
    is a cheating attempt from the player by calculating the amount of shaking force.
     @param time timeStamp when the event occurred.
     @param values sensorEvent values (x,y,z) detected movement.
     */
    @Override
    public void getAccelerometer(float[] values, long time) {
        long timestamp = TimeUnit.MILLISECONDS.convert(time, TimeUnit.NANOSECONDS);
        float x = values[0];
        float y = values[1];
        float z = values[2];
        if ((timestamp - lastUpdateMs) > 500) {
            float accelerationSqrt = (x * x + y * y + z * z)
                    / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
            if (accelerationSqrt >= 4) {
                lastUpdateMs = timestamp;
                Log.i(TAG, "Shake detected");
                sensorListener.handle();
            } else {
                lastUpdateMs = timestamp;
            }
        }
    }
    /**
     This is triggered when a light sensor event fires. The method checks if the event
     is a cheating attempt from the player by calculating light intensity.
     @param time timeStamp when the event occurred.
     @param values sensorEvent value(x), detected light level
     */
    @Override
    public void getLight(float[] values, long time) {
        long timestamp = TimeUnit.MILLISECONDS.convert(time, TimeUnit.NANOSECONDS);
        float lux = values[0];

        if ((timestamp - lastUpdateMs) > 800) {
            if (lux < 5) {
                lastUpdateMs = timestamp;
                Log.i(TAG, "Dark");
                sensorListener.handle();
            } else {
                lastUpdateMs = timestamp;
            }
        }
    }


    public void startListen() {
        onStart();
    }

    @Override
    public void pauseListen() {
        if (sensorType != TYPE_FAIR) {
            onPause();
        }
    }

    @Override
    public void resumeListen() {
        if (sensorType != TYPE_FAIR) {
            onResume();
        }
    }

    @Override
    public void stopListen() {
        if (sensorType != TYPE_FAIR) {
            onStop();
        }
    }

    /**
     * Kills cheatService singleton instance
     */
    public static void reset(){
        instance = null;
    }

    /**
     * Sending a Network call to the Server if a player cheated.
     * @param cheated  true when cheat is successful.
     * @param timeStamp time of cheat attempt.
     * @param cheatType selected cheatType from the player
     */
    public void sendMsgCheated(final boolean cheated, final long timeStamp, final int cheatType) {
        NetworkClient client = NetworkClientKryo.getInstance();
        Log.i(TAG, "Sending CheatMessage to Server");
        Thread thread = new Thread(() -> {
            CheatedMessage cM = new CheatedMessage(this.playerId, cheated, timeStamp, cheatType);
            client.sendMessage(cM);
        });
        thread.start();
    }

    /**
     * Generates a positive random number.
     * @param max highest number limit.
     * @return random number
     */
    public int randomNumber(int max) {
        SecureRandom random = new SecureRandom ();
        return random.nextInt(max);
    }

    /**
     * Generates a Card TextView by copying an existing TextViw.
     * @param tv copied input TextView
     * @param context Context where TextView is used.
     * @return New TextView based on the input TextView.
     */
    public TextView generateCard(TextView tv, Context context) {
        TextView cheatCard = new TextView(context);
        cheatCard.setText(tv.getText());
        cheatCard.setTextColor(tv.getCurrentTextColor());
        cheatCard.setTextSize(tv.getTextSize());
        cheatCard.setGravity(Gravity.CENTER);
        return cheatCard;
    }

    @Override @SuppressWarnings("unused")
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // NOT NEEDED
    }

    // Getter and Setter Methods

    public int getPlayerId() {
        return playerId;
    }
    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
    public void setSensorType(int type) {
        sensorType = type;
    }
    public int getSensorType() {
        return sensorType;
    }
    public void setContext(Context context, String name) {
        this.context = context;
    }
    public Context getContext() {
        return context;
    }
    public SensorManager getSensorManager() {
        return sensorManager;
    }
    public void setSensorManager(SensorManager sensorManager){
        this.sensorManager = sensorManager;
    }
    public static int getTypeFair() {
        return TYPE_FAIR;
    }
    public Sensor getSensor() {
        return sensor;
    }
    public boolean isSensorListen() {
        return isSensorListen;
    }
    public void setTestMode(boolean testMode){
        this.testMode = testMode;
    }

}