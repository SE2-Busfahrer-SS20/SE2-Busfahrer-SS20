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
import java.util.concurrent.TimeUnit;

public class CheatServiceImpl implements CheatService {

    private final NetworkClient client = NetworkClientKryo.getInstance();
    private static final String TAG = "CheatServiceImpl";
    private static final int TYPE_FAIR = 0;

    @SuppressLint("StaticFieldLeak")
    // using ApplicationContext solves this problem, context is not saved.
    private static CheatService instance;

    public interface SensorListener {
        void handle();
    }

    private int playerId;
    private int sensorType;

    private Context context;
    private SensorListener sensorListener;
    private SensorManager sensorManager;
    private Sensor sensor;
    private String activityName;
    private long lastUpdateMs;
    private boolean isSensorListen = false;

    @SuppressWarnings("unused")
    private CheatServiceImpl() {
    }

    public static CheatService getInstance() {
        if (instance == null) {
            instance = new CheatServiceImpl();
        }
        return instance;
    }

    public void setSensorListener(SensorListener sensorListener) {
        this.sensorListener = sensorListener;
    }

    private void onStart() {
        Log.i(TAG, "Service started");
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
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

    private void onPause() {
        Log.i(TAG, "Service paused");
        if (isSensorListen && sensorManager != null) {
            sensorManager.unregisterListener(this);
            isSensorListen = false;
        }
    }

    private void onResume() {
        Log.i(TAG, "Service resume");
        if (!isSensorListen && sensorManager != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
            isSensorListen = true;
        }

    }

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
            getAccelerometer(event);
        } else if (sensor.getType() == Sensor.TYPE_LIGHT) {
            getLight(event);
        }
    }

    private void getAccelerometer(SensorEvent event) {
        long timestamp = TimeUnit.MILLISECONDS.convert(event.timestamp, TimeUnit.NANOSECONDS);
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

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

    private void getLight(SensorEvent event) {
        long timestamp = TimeUnit.MILLISECONDS.convert(event.timestamp, TimeUnit.NANOSECONDS);
        float lux = event.values[0];

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

    // network call for player cheated in game
    public void sendMsgCheated(final boolean cheated, final long timeStamp, final int cheatType) {
        Log.i(TAG, "Sending CheatMessage to Server");
        Thread thread = new Thread(() -> {
            CheatedMessage cM = new CheatedMessage(this.playerId, cheated, timeStamp, cheatType);
            client.sendMessage(cM);
        });
        thread.start();
    }

    public int randomNumber(int max, int min) {
        return (int) ((Math.random() * (max + min)));
    }

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

    /**
     * Getter and Setter
     */
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
        this.activityName = name;
    }
    public Context getContext() {
        return context;
    }


}
