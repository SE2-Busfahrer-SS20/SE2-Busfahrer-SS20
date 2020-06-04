package at.aau.busfahrer.service.impl;


import at.aau.busfahrer.service.BushmenService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import shared.model.Card;
import shared.networking.Callback;
import shared.networking.NetworkClient;
import shared.networking.dto.BushmenCardMessage;
import shared.networking.dto.BushmenMessage;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class) // Initializes mocks (@Mock)
public class BushmenServiceImplTest {

    private BushmenService service;

    @Mock
    private NetworkClient networkClient;

    @Before
    public void before() {
        service = new BushmenServiceImpl(networkClient);
    }

    @Test
    public void kartenCounter_is0() {
        Assert.assertEquals(service.getKartenCounter(), 0);
    }

    @Test
    public void kartenCounter_incrementBy1() {
        service.incrementKartenCounter();
        Assert.assertEquals(service.getKartenCounter(), 1);
    }

    @Test
    public void kartenCounter_incrementBy4AndWon() {
        service.incrementKartenCounter();
        service.incrementKartenCounter();
        service.incrementKartenCounter();
        service.incrementKartenCounter();

        Assert.assertEquals(service.getKartenCounter(), 4);
        Assert.assertTrue(service.hasWon());
    }

    @Test
    public void kartenCounter_reset() {
        service.incrementKartenCounter();

        service.resetKartenCounter();

        Assert.assertEquals(service.getKartenCounter(), 0);
    }

    @Test
    public void punkteAnzahlBusfahrer_is10() {
        Assert.assertEquals(service.getPunkteAnzahlBusfahrer(), 10);
    }

    @Test
    public void punkteAnzahlBusfahrer_add() {
        service.addPunkteAnzahlBusfahrer(100);
        service.addPunkteAnzahlBusfahrer(-50);

        Assert.assertEquals(service.getPunkteAnzahlBusfahrer(), 60);
    }

    @Test
    public void punkteAnzahlBusfahrer_reset() {
        service.addPunkteAnzahlBusfahrer(100);

        service.resetPunkteAnzahlBusfahrer();

        Assert.assertEquals(service.getPunkteAnzahlBusfahrer(), 10);
    }

    @Test
    public void isLooser() {
        service.setLooser(true);

        Assert.assertTrue(service.isLooser());
    }

    @Test
    public void startBushmanRound() {
        service.startBushmanRound();

        // Verifiziere, dass sendMessage vom networkClient genau 1x mit einer Bushmen message aufgerufen wird
        verify(networkClient, times(1)).sendMessage(any(BushmenMessage.class));
    }

    @Test
    public void turnCard() {
        Card firstCard = mock(Card.class);
        Card secondCard = mock(Card.class);
        List<Card> cards = Arrays.asList(firstCard, secondCard);
        service.setCards(cards);

        service.turnCard(1);

        ArgumentCaptor<BushmenCardMessage> captor = ArgumentCaptor.forClass(BushmenCardMessage.class);
        verify(networkClient, times(1)).sendMessage(captor.capture());

        BushmenCardMessage message = captor.getValue();
        Assert.assertEquals(message.getCard(), secondCard);
        Assert.assertEquals(message.getCardId(), 1);
    }

    @Test
    public void cardCallbacks() {
        int cardId = 1;
        Card card = mock(Card.class);

        doAnswer((answer) -> {
            // Is executed after registerCallback with a BushmenCardMessage is called on the network client
            Callback<BushmenCardMessage> cardMessage = (Callback<BushmenCardMessage>) answer.getArgument(1, Callback.class);
            cardMessage.callback(new BushmenCardMessage(cardId, card));

            return null;
        }).when(networkClient).registerCallback(eq(BushmenCardMessage.class), any());

        service.setUpCardTurnCallback((actualCardId, actualCard) -> {
            Assert.assertEquals(actualCardId.intValue(), cardId);
            Assert.assertEquals(actualCard, card);
        });
    }
}