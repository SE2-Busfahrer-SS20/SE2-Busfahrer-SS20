package at.aau.busfahrer.service.impl;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import at.aau.busfahrer.service.CoughtService;
import at.aau.busfahrer.service.impl.CoughtServiceImpl;
import shared.model.PlayersStorage;
import shared.model.impl.PlayerDTOImpl;
import shared.model.impl.PlayersStorageImpl;

public class CoughtServiceImplTest {
    private CoughtService coughtService;
    private PlayersStorageImpl playersStorage;
    private PlayerDTOImpl player1;
    private PlayerDTOImpl player2;

    @Before
    public void setup() {
        this.coughtService = CoughtServiceImpl.getInstance();
        this.playersStorage = PlayersStorageImpl.getInstance();
        this.player1 = new PlayerDTOImpl("Larissa", 3, true);
        this.player2 = new PlayerDTOImpl("Cought", 5, false);
        playersStorage.addPlayer(player1);
        playersStorage.addPlayer(player2);

    }

    @After
    public void destroy() {
        coughtService = null;
        PlayersStorageImpl.reset();
    }

    @Test
    public void testSingletonPattern() {
        Assert.assertEquals(CoughtServiceImpl.getInstance(), coughtService);
    }

    @Test
    public void testIsCheatingTrue() {
        playersStorage.setCurrentTurn(0);
        playersStorage.setTempID(1);
        Assert.assertEquals(true, coughtService.isCheating());
    }

    @Test
    public void testIsCheatingFalse() {
        playersStorage.setCurrentTurn(1);
        playersStorage.setTempID(0);
        Assert.assertEquals(false, coughtService.isCheating());
    }

    @Test
    public void testIsCheatingTrueScore() {
        playersStorage.setCurrentTurn(0);
        playersStorage.setTempID(1);
        coughtService.isCheating();
        Assert.assertEquals(4, (int) playersStorage.getPlayerList().get(1).getScore());
    }

    @Test
    public void testIsCheatingFalseScore() {
        playersStorage.setCurrentTurn(1);
        playersStorage.setTempID(0);
        coughtService.isCheating();
        Assert.assertEquals(4, (int) playersStorage.getPlayerList().get(0).getScore());
    }

    @Test
    public void testIsCheatingPlapTrue(){
        playersStorage.setTempID(1);
        Assert.assertEquals(true,coughtService.isCheatingPlap());
    }

    @Test
    public void testIsCheatingPlapFalse(){
        playersStorage.setTempID(0);
        Assert.assertEquals(false,coughtService.isCheatingPlap());
    }
}
