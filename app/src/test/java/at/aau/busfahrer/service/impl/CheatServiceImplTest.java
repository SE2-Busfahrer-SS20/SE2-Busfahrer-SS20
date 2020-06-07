package at.aau.busfahrer.service.impl;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import at.aau.busfahrer.service.CheatService;


import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CheatServiceImplTest {

    private CheatService cheatServiceInstance;

    @Mock
    private SensorManager sensorManager;
    @Mock
    Context context;
    @Mock
    private Sensor mockSensor;

    @Before
    public void initTestClass() {
        MockitoAnnotations.initMocks(this);
        cheatServiceInstance = CheatServiceImpl.getInstance();
        cheatServiceInstance.setTestMode(true);
        cheatServiceInstance.setSensorManager(sensorManager);
        cheatServiceInstance.setContext(context, CheatServiceImplTest.class.getName());
        cheatServiceInstance.setSensorType(Sensor.TYPE_LIGHT);
    }

    @Test
    public void singletonTest() {
        CheatService cheatServiceInstanceNew = CheatServiceImpl.getInstance();
        assertEquals("Same Instance", cheatServiceInstance, cheatServiceInstanceNew);
    }

    @Test
    public void initTest() {
        assertEquals(context, cheatServiceInstance.getContext());
        assertEquals(sensorManager, cheatServiceInstance.getSensorManager());
        assertFalse(cheatServiceInstance.isSensorListen());
    }

    @Test
    public void testRandom() {
        int random = cheatServiceInstance.randomNumber(6);
        assertTrue(random < 6);
    }

    @Test
    public void setPlayerId() {
        int playerId = 10;
        cheatServiceInstance.setPlayerId(playerId);
        assertEquals(playerId, cheatServiceInstance.getPlayerId());
    }

    @Test
    public void testSensorTypeSet() {
        int[] sensorTypes = {Sensor.TYPE_ACCELEROMETER, Sensor.TYPE_LIGHT, CheatServiceImpl.TYPE_FAIR};
        cheatServiceInstance.setSensorType(sensorTypes[0]);
        assertEquals(sensorTypes[0], cheatServiceInstance.getSensorType());
        cheatServiceInstance.setSensorType(sensorTypes[1]);
        assertEquals(sensorTypes[1], cheatServiceInstance.getSensorType());
        cheatServiceInstance.setSensorType(sensorTypes[2]);
        assertEquals(sensorTypes[2], cheatServiceInstance.getSensorType());
    }

    @Test
    public void startListenAccelerometer() {
        int sensorType = Sensor.TYPE_ACCELEROMETER;
        cheatServiceInstance.setSensorType(sensorType);
        cheatServiceInstance.startListen();
        mockSensor = sensorManager.getDefaultSensor(sensorType);
        assertEquals(mockSensor, cheatServiceInstance.getSensor());
    }

    @Test
    public void startListenLight() {
        int sensorType = Sensor.TYPE_LIGHT;
        cheatServiceInstance.setSensorType(sensorType);
        cheatServiceInstance.startListen();
        mockSensor = sensorManager.getDefaultSensor(sensorType);
        assertEquals(mockSensor, cheatServiceInstance.getSensor());
    }

    @Test
    public void startListenFair() {
        int sensorType = CheatServiceImpl.getTypeFair();
        cheatServiceInstance.setSensorType(sensorType);
        cheatServiceInstance.startListen();
        assertNull(cheatServiceInstance.getSensor());
    }

    @Test
    public void startListenNotExists() {
        int sensorType = -1;
        cheatServiceInstance.setSensorType(sensorType);
        cheatServiceInstance.startListen();
        assertNull(cheatServiceInstance.getSensor());
    }

    @Test
    public void testServiceResumeStopPauseCycle() {
        cheatServiceInstance.resumeListen();
        assertTrue(cheatServiceInstance.isSensorListen());
        cheatServiceInstance.pauseListen();
        assertFalse(cheatServiceInstance.isSensorListen());
        cheatServiceInstance.resumeListen();
        assertTrue(cheatServiceInstance.isSensorListen());
        cheatServiceInstance.stopListen();
        assertFalse(cheatServiceInstance.isSensorListen());
    }

    /*
    Sensor Event Tests for Light and Accelerometer,
        * Case1 = Event fires, Case2 = No Event fires
        * verify if UI update Callback is called or not.
     */

    @Test
    public void testLightCase1EventFires() {
        float[] values = new float[]{2.3f};
        CheatServiceImpl.SensorListener listenerCallback = mock(CheatServiceImpl.SensorListener.class);
        cheatServiceInstance.setSensorListener(listenerCallback);
        cheatServiceInstance.getLight(values, System.currentTimeMillis());
        verify(listenerCallback, times(1)).handle();
    }

    @Test
    public void testLightCase2EventNotFires() {
        float[] values = new float[]{5.5f};
        CheatServiceImpl.SensorListener listenerCallback = mock(CheatServiceImpl.SensorListener.class);
        cheatServiceInstance.setSensorListener(listenerCallback);
        cheatServiceInstance.getLight(values, System.currentTimeMillis());
        verify(listenerCallback, times(0)).handle();
    }

    @Test
    public void testAccelCase1EventFires() {
        float[] values = new float[]{23.6f, 12.2f, 1};
        CheatServiceImpl.SensorListener listenerCallback = mock(CheatServiceImpl.SensorListener.class);
        cheatServiceInstance.setSensorListener(listenerCallback);
        cheatServiceInstance.getAccelerometer(values, System.currentTimeMillis());
        verify(listenerCallback, times(1)).handle();
    }

    @Test
    public void testAccelCase2EventNotFires() {
        float[] values = new float[]{1, 1, 1};
        CheatServiceImpl.SensorListener listenerCallback = mock(CheatServiceImpl.SensorListener.class);
        cheatServiceInstance.setSensorListener(listenerCallback);
        cheatServiceInstance.getAccelerometer(values, System.currentTimeMillis());
        verify(listenerCallback, times(0)).handle();
    }


    @After
    public void tearDown() {
        CheatServiceImpl.reset();
        cheatServiceInstance = null;
    }

}