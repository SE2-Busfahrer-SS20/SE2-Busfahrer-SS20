package at.aau.busfahrer.service;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import at.aau.busfahrer.service.impl.CheatServiceImpl;

public interface CheatService extends SensorEventListener {

    void setContext(Context context, String name);
    void startListen();
    void pauseListen();
    void resumeListen();
    void stopListen();
    int getSensorType();
    void setSensorType(int type);
    void setSensorListener(CheatServiceImpl.SensorListener sensorListener);
    void onSensorChanged(SensorEvent event);
    void onAccuracyChanged(Sensor sensor, int accuracy);
}
