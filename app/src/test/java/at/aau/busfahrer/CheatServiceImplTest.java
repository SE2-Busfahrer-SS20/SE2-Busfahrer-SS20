package at.aau.busfahrer;
import android.hardware.Sensor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import at.aau.busfahrer.service.impl.CheatServiceImpl;
import static org.junit.Assert.*;

 // TODO More Tests

public class CheatServiceImplTest {

    private CheatServiceImpl cheatService1;
    private CheatServiceImpl cheatService2;
    private int sensor;

    @Before
    public void initCheatServiceTest(){
        cheatService1 = CheatServiceImpl.getInstance();
        cheatService2 = CheatServiceImpl.getInstance();
        sensor = Sensor.TYPE_ACCELEROMETER;
        assertEquals("same Instance", cheatService1,cheatService2);
    }

    @Test
    public void initCheatService(){


    }


    @After
    public void tearDown(){
        cheatService1 = null;
        cheatService2 = null;

    }








}
