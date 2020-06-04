package at.aau.common.shared.networking.dto;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import shared.networking.dto.BaseMessage;
import shared.networking.dto.DealPointsMessage;
import shared.networking.dto.StartPLabMessage;
import shared.networking.dto.WinnerLooserMessage;

public class PLapDtoServiceTest {

    private WinnerLooserMessage winnerLooserMessage;
    private DealPointsMessage dealPointsMessage;
    private StartPLabMessage startPLabMessage;

    @Before
    public void setup() {
        this.winnerLooserMessage = new WinnerLooserMessage();
        this.dealPointsMessage = new DealPointsMessage();
        this.startPLabMessage = new StartPLabMessage();
    }

    /**
     * check if the used DTOs are extends the BaseMessage.
     */
    @Test
    public void testInheritance() {
        Assert.assertTrue(winnerLooserMessage instanceof BaseMessage);
        Assert.assertTrue(dealPointsMessage instanceof BaseMessage);
        Assert.assertTrue(startPLabMessage instanceof BaseMessage);
    }

    @Test
    public void testWinnerLooserGetterSetter() {
        winnerLooserMessage.setIsLooser(true);
        Assert.assertTrue(winnerLooserMessage.getIsLooser());
        winnerLooserMessage.setIsLooser(false);
        Assert.assertFalse(winnerLooserMessage.getIsLooser());
    }
}
