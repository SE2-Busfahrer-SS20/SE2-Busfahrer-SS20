package at.aau.common.shared.networking.dto;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import shared.networking.dto.BaseMessage;
import shared.networking.dto.CoughtMessage;

public class CoughtDTOServiceTest {
    private CoughtMessage coughtMessage;

    @Before
    public void setup() {
        this.coughtMessage = new CoughtMessage();
    }

    @Test
    public void testInheritance() {
        Assert.assertTrue(coughtMessage instanceof BaseMessage);
    }
    @Test
    public void testCoughtMessageGetterSetterCheatedTrue(){
        coughtMessage.setCheated(true);
        Assert.assertEquals(true,coughtMessage.isCheated());
    }
    @Test
    public void testCoughtMessageGetterSetterCheatedFalse(){
        coughtMessage.setCheated(false);
        Assert.assertEquals(false,coughtMessage.isCheated());
    }
    @Test
    public void testCoughtMessageetterSetterIndexCheater(){
        coughtMessage.setIndexCheater(0);
        Assert.assertEquals(0,coughtMessage.getIndexCheater());
    }
    @Test
    public void testCoughtMessageGetterSetterIndexCought(){
        coughtMessage.setIndexCought(1);
        Assert.assertEquals(1,coughtMessage.getIndexCought());
    }
    @Test
    public void testCoughtMessageGetterSetterScoreCheater(){
        coughtMessage.setScoreCheater(4);
        Assert.assertEquals(4,coughtMessage.getScoreCheater());
    }
    @Test
    public void testCoughtMessageGetterSetterScoreCought(){
        coughtMessage.setScoreCought(3);
        Assert.assertEquals(3,coughtMessage.getScoreCought());
    }
}
