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
    public void testIsCheatingTrueScore0() {
        playersStorage.setCurrentTurn(0);
        playersStorage.setTempID(1);
        playersStorage.getPlayerList().get(1).setScore(0);
        coughtService.isCheating();
        Assert.assertEquals(0, (int) playersStorage.getPlayerList().get(1).getScore());
    }

    @Test
    public void testIsCheatingFalseScore() {
        playersStorage.setCurrentTurn(1);
        playersStorage.setTempID(0);
        coughtService.isCheating();
        Assert.assertEquals(4, (int) playersStorage.getPlayerList().get(0).getScore());
    }
    @Test
    public void testIsCheatingFalseScore0() {
        playersStorage.setCurrentTurn(1);
        playersStorage.setTempID(0);
        playersStorage.getPlayerList().get(1).setScore(0);
        coughtService.isCheating();
        Assert.assertEquals(0, (int) playersStorage.getPlayerList().get(1).getScore());
    }

    @Test
    public void testIsCheatingPlapTrue(){
        playersStorage.setTempID(1);
        Assert.assertEquals(true,coughtService.isCheatingPlap());
    }
    @Test
    public void testIsCheatingPlapTrueScore0(){
        playersStorage.setTempID(1);
        playersStorage.getPlayerList().get(1).setScore(0);
        coughtService.isCheatingPlap();
        Assert.assertEquals(0,(int)playersStorage.getPlayerList().get(1).getScore());
    }

    @Test
    public void testIsCheatingPlapFalse(){
        playersStorage.setTempID(0);
        Assert.assertEquals(false,coughtService.isCheatingPlap());
    }
    @Test
    public void testIsCheatingPlapFalseScore0(){
        playersStorage.setTempID(0);
        playersStorage.getPlayerList().get(1).setScore(0);
        coughtService.isCheatingPlap();
        Assert.assertEquals(0, (int) playersStorage.getPlayerList().get(1).getScore());
    }
    @Test
    public void testIsCheatingBushmenFalseTrue() {
        playersStorage.setCurrentTurn(1);
        playersStorage.getPlayerList().get(1).setBusdriver();
        playersStorage.setTempID(0);
        Assert.assertEquals(false, coughtService.isCheatingBushmen());
    }
    @Test
    public void testIsCheatingBushmenTrueTrue() {
        playersStorage.setCurrentTurn(0);
        playersStorage.getPlayerList().get(0).setBusdriver();
        playersStorage.setTempID(1);
        Assert.assertEquals(true, coughtService.isCheatingBushmen());
    }
    @Test
    public void testIsCheatingBushmenFalseFalseFalse() {
        playersStorage.setCurrentTurn(1);
        playersStorage.setTempID(0);
        Assert.assertEquals(false, coughtService.isCheatingBushmen());
    }
    @Test
    public void testIsCheatingBushmenTrueFalse() {
        playersStorage.setCurrentTurn(0);
        playersStorage.setTempID(1);
        Assert.assertEquals(false, coughtService.isCheatingBushmen());
    }
    @Test
    public void testIsCHeatingBushmenTrueScore0(){
        playersStorage.setCurrentTurn(0);
        playersStorage.getPlayerList().get(0).setBusdriver();
        playersStorage.setTempID(1);
        playersStorage.getPlayerList().get(1).setScore(0);
        coughtService.isCheatingBushmen();
        Assert.assertEquals(0,(int)playersStorage.getPlayerList().get(1).getScore());
    }
    @Test
    public void testIsCheatingushenFalseScore0(){
        playersStorage.setCurrentTurn(1);
        playersStorage.setTempID(0);
        playersStorage.getPlayerList().get(1).setScore(0);
        coughtService.isCheatingBushmen();
        Assert.assertEquals(0,(int)playersStorage.getPlayerList().get(1).getScore());

    }

}
