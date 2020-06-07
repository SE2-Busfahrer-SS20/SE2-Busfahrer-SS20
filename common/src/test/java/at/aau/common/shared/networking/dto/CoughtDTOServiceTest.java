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
    public void testCoughtMessageGetter(){

    }
}
