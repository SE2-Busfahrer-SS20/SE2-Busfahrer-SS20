package at.aau.common.shared.networking.dto;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import shared.networking.dto.BaseMessage;
import shared.networking.dto.TextMessage;

public class TextMessageDToTest {
    private TextMessage textMessage;
    private String text;

    @Before
    public void setup(){
        this.textMessage = new TextMessage();
        text = "Test";
    }
    @Test
    public void testInheritance() {
        Assert.assertTrue(textMessage instanceof BaseMessage);
    }
    @Test
    public void constrcutorTest(){
        textMessage = new TextMessage(text);
        Assert.assertEquals("Test",textMessage.getText());
    }
    @Test
    public void constrcutorTesttoString(){
        textMessage = new TextMessage(text);
        Assert.assertEquals("TextMessage: Test",textMessage.toString());
    }

}
