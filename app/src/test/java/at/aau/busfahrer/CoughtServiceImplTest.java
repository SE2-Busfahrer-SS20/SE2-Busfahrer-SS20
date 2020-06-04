package at.aau.busfahrer;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import at.aau.busfahrer.service.CoughtService;
import at.aau.busfahrer.service.impl.CoughtServiceImpl;

public class CoughtServiceImplTest {
    private CoughtService coughtService;

    @Before
    public void setup() {
        this.coughtService = CoughtServiceImpl.getInstance();
    }
    @After
    public void destroy() {
        coughtService = null;
    }

    @Test
    public void testSingletonPattern() {
        Assert.assertEquals(CoughtServiceImpl.getInstance(), coughtService);
    }
}
