package shared.model.impl;

import org.junit.Assert;
import org.junit.Test;
import shared.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class PlayersStorageImplTest {

    @Test
    public void initialState() {
        PlayersStorage storage = PlayersStorageImpl.getInstance();

        assertNotNull(storage);
    }

    @Test
    public void getterAndSetter() {
        PlayersStorage storage = PlayersStorageImpl.getInstance();

        storage.setCards(new Card[]{});
        storage.setMaster(true);
        storage.setState(GameState.ENDED);

        assertEquals(0, storage.getCards().length);
        assertEquals(GameState.ENDED, storage.getState());
        assertTrue(storage.isMaster());
    }

    @Test
    public void playerName() {
        PlayerDTO player = createPlayerDto("foo", 0);

        PlayersStorage storage = PlayersStorageImpl.getInstance();

        //Da PlayerStorage Singleton ist muss man es immer zurücksetzen
        storage.resetPlayers();
        storage.addPlayer(player);

        assertEquals("foo", storage.getPlayerName(0));
    }


    //Wenn kein Spieler da ist, erwarte ich mir eine Exception
    @Test(expected = IndexOutOfBoundsException.class)
    public void playerNameNotFound() {
        PlayersStorage storage = PlayersStorageImpl.getInstance();

        storage.resetPlayers();
        storage.getPlayerName(-1);
    }

    @Test
    public void playerNamesList() {
        PlayerDTO p1 = createPlayerDto("foo", 0);
        PlayerDTO p2 = createPlayerDto("bar", 0);

        PlayersStorage storage = PlayersStorageImpl.getInstance();

        storage.resetPlayers();
        storage.addPlayer(p1);
        storage.addPlayer(p2);

        assertEquals(Arrays.asList("foo", "bar"), storage.getPlayerNamesList());
    }

    @Test
    public void playerScores() {
        PlayerDTO p1 = createPlayerDto("foo", 100);
        PlayerDTO p2 = createPlayerDto("bar", 42);

        PlayersStorage storage = PlayersStorageImpl.getInstance();

        storage.resetPlayers();
        storage.addPlayer(p1);
        storage.addPlayer(p2);

        assertEquals(Arrays.asList(100, 42), storage.getScoreList());
    }

    @Test
    public void addScoreToCurrentPlayer() {
        PlayerDTO p1 = createPlayerDto("foo", 100);
        PlayerDTO p2 = createPlayerDto("bar", 42);
        PlayerDTO p3 = createPlayerDto("baz", 0);

        PlayersStorage storage = PlayersStorageImpl.getInstance();

        storage.resetPlayers();
        storage.addPlayer(p1);
        storage.addPlayer(p2);
        storage.addPlayer(p3);

        storage.setTempID(1); // zweiter spieler

        storage.addScoreToCurrentPlayer(100);

        assertEquals(Arrays.asList(100, 142, 0 ), storage.getScoreList());
    }

    //Hilfsmethode die neuen Spieler erzeugt
    public static PlayerDTO createPlayerDto(String name, int score) {
        return new PlayerDTOImpl(name, score, false);
    }
    @Test
    public void resetInstanceTest(){
        PlayersStorageImpl.reset();
        Assert.assertNotEquals(null,PlayersStorageImpl.getInstance());
    }
    @Test
    public void getterSetterTempID(){
        PlayersStorage playersStorage = PlayersStorageImpl.getInstance();
        playersStorage.setTempID(3);
        Assert.assertEquals(3,playersStorage.getTempID());
    }
    @Test
    public void getterSetterCurrentTurn(){
        PlayersStorage playersStorage = PlayersStorageImpl.getInstance();
        playersStorage.setCurrentTurn(4);
        Assert.assertEquals(4,playersStorage.getCurrentTurn());
    }
    @Test
    public void getterSetterPlayerList(){
        List<PlayerDTO> playerList = new ArrayList<>();
        playerList.add(new PlayerDTOImpl("Lars",45,true));
        playerList.add((new PlayerDTOImpl("Hanis",23,false)));
        PlayersStorage playersStorage = PlayersStorageImpl.getInstance();
        playersStorage.setPlayerList(playerList);
        Assert.assertNotEquals(null,playersStorage.getPlayerList());

    }
    @Test
    public void getterSetterPlayerListAscending(){
        List<PlayerDTO> playerList = new ArrayList<>();
        playerList.add(new PlayerDTOImpl("Lars",45,true));
        playerList.add((new PlayerDTOImpl("Hanis",23,false)));
        PlayersStorage playersStorage = PlayersStorageImpl.getInstance();
        playersStorage.setPlayerList(playerList);
        Assert.assertNotEquals(null,playersStorage.getPlayerListAscending());

    }
    @Test
    public void getterSetterisCheating(){
        PlayersStorage playersStorage = PlayersStorageImpl.getInstance();
        List<PlayerDTO> playerList = new ArrayList<>();
        playerList.add(new PlayerDTOImpl("Lars",45,true));
        playerList.add((new PlayerDTOImpl("Hanis",23,false)));
        playersStorage.setPlayerList(playerList);
        Assert.assertEquals(false,playersStorage.isCheating(1));
        playersStorage.setCheating(1);
        Assert.assertEquals(true,playersStorage.isCheating(1));
    }
}