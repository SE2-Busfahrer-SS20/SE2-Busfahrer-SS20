package shared.model.impl;

import org.junit.Assert;
import org.junit.Test;
import shared.model.Player;
import shared.model.PlayerDTO;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class PlayerDTOImplTest {

//schauen ob Konstruktur das richtige Feld setzt
    @Test
    public void constructorAndDefaultValues() {
        PlayerDTO player = new PlayerDTOImpl("foo", 100, true);

        assertEquals("foo", player.getName());
        assertEquals(100, player.getScore().intValue());
        assertTrue(player.isCheating());
    }

    //schauen ob defaultwerte richtig gesetzt sind
    @Test
    public void emptyConstructor() {
        PlayerDTO player = new PlayerDTOImpl();

        assertNull(player.getName());
        assertNull(player.getScore());
        assertFalse(player.isCheating());
    }

    @Test
    public void getterAndSetter() {
        PlayerDTOImpl player = new PlayerDTOImpl(); // no interface -> test setter getName()

        player.setName("bar");
        player.setScore(42);
        player.setCheating(true);

        assertEquals("bar", player.getName());
        assertEquals(42, player.getScore().intValue());
        assertTrue(player.isCheating());
    }

    @Test
    public void dtoToPlayerList() {
        List<Player> players = Arrays.asList(
                createPlayer("1", 100, true),
                createPlayer("2", 0, false)
        );

        List<PlayerDTO> playerDTOs = PlayerDTOImpl.getDTOFromPlayerList(players);

        List<PlayerDTO> expectedPlayerDTOs = Arrays.asList(
                new PlayerDTOImpl("1", 100, true),
                new PlayerDTOImpl("2", 0, false)
        );

        assertEquals(expectedPlayerDTOs.size(), playerDTOs.size());
        assertPlayerDtoEquals(expectedPlayerDTOs.get(0), playerDTOs.get(0));
        assertPlayerDtoEquals(expectedPlayerDTOs.get(1), playerDTOs.get(1));
    }

    @Test
    public void dtoToPlayer() {
        Player player = createPlayer("1", 100, true);

        PlayerDTO dto = PlayerDTOImpl.getDTOFromPlayer(player);

        PlayerDTO expectedDto = new PlayerDTOImpl("1", 100, true);

        assertPlayerDtoEquals(expectedDto, dto);
    }

    public static Player createPlayer(String name, int score, boolean isCheating) {
        Player p = new PlayerImpl(name, null, null, null);

        p.setScore(score);
        p.setCheated(isCheating);

        return p;
    }

    private static void assertPlayerDtoEquals(PlayerDTO expected, PlayerDTO actual) {
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getScore(), actual.getScore());
        assertEquals(expected.isCheating(), actual.isCheating());
    }
    @Test
    public void constrcutorTestAndGetterisBusdriver(){
        PlayerDTO playerDTO = new PlayerDTOImpl("Lars",23,true,"Test");
        Assert.assertEquals(false,playerDTO.isBusdriver());
    }
    @Test
    public void getterBusdriverTest(){
        PlayerDTO playerDTO = new PlayerDTOImpl("Lars",23,true,"Test");
        playerDTO.setBusdriver();
        Assert.assertEquals(true,playerDTO.isBusdriver());
    }
    @Test
    public void constrcutorTestAndGetterMac(){
        PlayerDTO playerDTO = new PlayerDTOImpl("Lars",23,true,"Test");
        Assert.assertEquals("Test",playerDTO.getMAC());
    }
    @Test
    public void getterMacTest(){
        PlayerDTO playerDTO = new PlayerDTOImpl("Lars",23,true,"Test");
        playerDTO.setMAC("NeuerTest");
        Assert.assertEquals("NeuerTest",playerDTO.getMAC());
    }
}