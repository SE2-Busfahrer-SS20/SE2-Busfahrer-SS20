package at.aau.common.shared.networking.dto;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import shared.networking.dto.BaseMessage;
import shared.networking.dto.DealPointsMessage;

public class DealPointsMessageDTOTest {
    private DealPointsMessage dealPointsMessage;

    @Before
    public void setup(){
        this.dealPointsMessage = new DealPointsMessage();
    }
    @Test
    public void testInheritance() {
        Assert.assertTrue(dealPointsMessage instanceof BaseMessage);
    }
    @Test
    public void constructorGetterTestPoints(){
        dealPointsMessage = new DealPointsMessage("Test",12);
        Assert.assertEquals(12,dealPointsMessage.getPoints());
    }
    @Test
    public void constructorGetterTestName(){
        dealPointsMessage = new DealPointsMessage("Test",12);
        Assert.assertEquals("Test",dealPointsMessage.getDestPlayerName());
    }
    @Test
    public void constructorSetterTestName(){
        dealPointsMessage = new DealPointsMessage("Test",12);
        dealPointsMessage.setDestPlayerName("Hansi");
        Assert.assertEquals("Hansi",dealPointsMessage.getDestPlayerName());
    }
    @Test
    public void constructorSetterTestPoints(){
        dealPointsMessage = new DealPointsMessage("Test",12);
        dealPointsMessage.setPoints(3);
        Assert.assertEquals(3,dealPointsMessage.getPoints());
    }
}
