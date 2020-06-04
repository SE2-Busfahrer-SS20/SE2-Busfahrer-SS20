package at.aau.common.shared.networking.dto;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import shared.model.Card;
import shared.model.impl.CardImpl;
import shared.networking.dto.BaseMessage;
import shared.networking.dto.DealPointsMessage;
import shared.networking.dto.StartPLapMessage;
import shared.networking.dto.WinnerLooserMessage;

import java.util.ArrayList;
import java.util.List;

public class PLapDtoServiceTest {

    private WinnerLooserMessage winnerLooserMessage;
    private DealPointsMessage dealPointsMessage;
    private StartPLapMessage startPLapMessage;

    @Before
    public void setup() {
        this.winnerLooserMessage = new WinnerLooserMessage();
        this.dealPointsMessage = new DealPointsMessage();
        this.startPLapMessage = new StartPLapMessage();
    }

    /**
     * check if the used DTOs are extends the BaseMessage.
     */
    @Test
    public void testInheritance() {
        Assert.assertTrue(winnerLooserMessage instanceof BaseMessage);
        Assert.assertTrue(dealPointsMessage instanceof BaseMessage);
        Assert.assertTrue(startPLapMessage instanceof BaseMessage);
    }

    @Test
    public void testWinnerLooserGetterSetter() {
        winnerLooserMessage.setIsLooser(true);
        Assert.assertTrue(winnerLooserMessage.getIsLooser());
        winnerLooserMessage.setIsLooser(false);
        Assert.assertFalse(winnerLooserMessage.getIsLooser());
    }
    @Test
    public void testDealPointsMessage() {
        dealPointsMessage.setDestPlayerName("example");
        Assert.assertEquals("example", dealPointsMessage.getDestPlayerName());

        dealPointsMessage.setPoints(10);
        Assert.assertEquals(10, dealPointsMessage.getPoints());

        // Test Argument Constructor
        dealPointsMessage = new DealPointsMessage("noname", 110);
        Assert.assertEquals(110, dealPointsMessage.getPoints());
        Assert.assertEquals("noname", dealPointsMessage.getDestPlayerName());
    }

    @Test
    public void testStartPLapMessage() {
        Card[] testArr = new Card[10];
        Card[] testArr2 = new Card[10];
        startPLapMessage.setPlabCards(testArr);
        Assert.assertArrayEquals(testArr, startPLapMessage.getPlabCards());

        List<String> mockList = new ArrayList<>();
        mockList.add("noname");
        mockList.add("example1");

        startPLapMessage.setPlayerNames(mockList);
        Assert.assertEquals(mockList, startPLapMessage.getPlayerNames());

        // Test Argument Constructor
        mockList.add("example2");
        startPLapMessage = new StartPLapMessage(testArr2, mockList);
        Assert.assertEquals(mockList, startPLapMessage.getPlayerNames());
        Assert.assertArrayEquals(testArr2, startPLapMessage.getPlabCards());

        startPLapMessage = new StartPLapMessage(testArr);
        Assert.assertArrayEquals(testArr, startPLapMessage.getPlabCards());
    }
}
