package at.aau.common.shared.networking.dto;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import shared.networking.dto.ServerActionResponse;
import shared.networking.dto.TextMessage;

public class ServerActionRespinseDToTest {
    private ServerActionResponse serverActionResponse;
    private String text;
    private boolean response;

    @Before
    public void setup(){
        this.serverActionResponse = new ServerActionResponse();
        text = "Hallos";
        response = false;
    }
    @Test
    public void testInheritance() {
        Assert.assertTrue(serverActionResponse instanceof TextMessage);
    }
    @Test
    public void constructorTestgetAction(){
        serverActionResponse = new ServerActionResponse(text,response);
        Assert.assertEquals(false,serverActionResponse.getActionResult());

    }
}
