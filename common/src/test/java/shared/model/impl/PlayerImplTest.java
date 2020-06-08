package shared.model.impl;

import com.esotericsoftware.kryonet.Connection;
import org.junit.Test;
import shared.model.Card;

import static org.junit.Assert.*;

public class PlayerImplTest {

    private static Card[] cards = new Card[] {
            new CardImpl(),
            new CardImpl(),
    };

    private Connection connection = null;

    @Test
    public void constructorAndGettersWithDefaultValues() {
        PlayerImpl player = createPlayerImpl();

        assertEquals("name", player.getName());
        assertEquals("foo", player.getMACAdress());
        assertArrayEquals(cards, player.getCards());
        assertEquals(cards[1], player.getCard(1));
        assertEquals(connection, player.getConnection());
        assertEquals(0, player.getScore());
        assertEquals(-1, player.getTempID());
        assertFalse(player.isCheated());
        assertFalse(player.isCheatedThisRound());
    }

    @Test
    public void setters() {
        PlayerImpl player = createPlayerImpl();

        player.setCheated(true);
        player.setCheatedThisRound(true);
        player.setTempID(100);
        player.setScore(1000);

        assertTrue(player.isCheated());
        assertTrue(player.isCheatedThisRound());
        assertEquals(100, player.getTempID());
        assertEquals(1000, player.getScore());
    }

    @Test
    public void addPoints() {
        PlayerImpl player = createPlayerImpl();

        player.addPoints(10000);

        assertEquals(10000, player.getScore());
    }

    private PlayerImpl createPlayerImpl() {
        return new PlayerImpl("name", "foo", cards, connection);
    }

}