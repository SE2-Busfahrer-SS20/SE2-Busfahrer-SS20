package at.aau.busfahrer.service;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public interface CheatService extends SensorEventListener {

    void setContext(Context context);
    void startListen();
    void pauseListen();
    void resumeListen();
    void stopListen();
    void setSensor(int type);

    void onSensorChanged(SensorEvent event);
    void onAccuracyChanged(Sensor sensor, int accuracy);
}
