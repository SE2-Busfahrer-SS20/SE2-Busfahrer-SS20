package at.aau.busfahrer.service.impl;

import at.aau.busfahrer.service.PLapClientService;
import at.aau.busfahrer.service.impl.PLapClientServiceImpl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import shared.model.Card;
import shared.model.Deck;
import shared.model.impl.CardImpl;
import shared.model.impl.DeckImpl;
import shared.networking.Callback;
import shared.networking.dto.BushmenCardMessage;
import shared.networking.dto.StartPLapMessage;
import shared.networking.kryonet.NetworkClientKryo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;

public class PLapClientServiceImplTest {

    private PLapClientService pLapClientService;

    @Mock
    NetworkClientKryo networkClient;

    @Mock
    PLapClientService plapMockService;

    @Before
    public void setup() {
        this.pLapClientService = PLapClientServiceImpl.getInstance();
    }
    @After
    public void destroy() {
        pLapClientService = null;
        PLapClientServiceImpl.destroyInstance();
    }

    @Test
    public void testSingletonPattern() {
        Assert.assertEquals(PLapClientServiceImpl.getInstance(), pLapClientService);
    }
    @Test
    public void testStartLap() {
        /*
         * Must in try catch, because Client should send an Exception, when the server is not reachable.
         */
        try {
            pLapClientService.startLab();
        } catch (Exception e) {
            Mockito.verify(networkClient, atLeastOnce()).sendMessage(any());
        }
    }

    /**
     * check if the counter for gamer points in the pyramid lap works expected.
     */
    @Test
    public void testRowCounter() {
        Card[] pCards = getPCards();
        pCards[0] = new CardImpl(3); // set to a non picture card, otherwise checks will not work expected.
        // check if the match count is 0;
        Assert.assertEquals( 0, pLapClientService.getMatchCount());
        ((PLapClientServiceImpl) pLapClientService).setPCards(pCards);
        // check if the checkCardMatch returns a card in case of a match. Row 4 is the last row.
        Assert.assertNotNull(pLapClientService.checkCardMatch(pCards[0].toString(), pCards, 4));
        // counter should be 1 now.
        Assert.assertEquals( 1, pLapClientService.getMatchCount());

        // check match in row 3. should increase the counter about 2 points.
        pLapClientService.checkCardMatch(pCards[0].toString(), pCards, 3);
        Assert.assertEquals( 3, pLapClientService.getMatchCount());

        // check match in row 3. should increase the counter about 3 points.
        pLapClientService.checkCardMatch(pCards[0].toString(), pCards, 2);
        Assert.assertEquals( 6, pLapClientService.getMatchCount());

        // check the most upper row of the pyramid. Counter should be increased about 4.
        pLapClientService.checkCardMatch(pCards[0].toString(), pCards, 1);
        Assert.assertEquals( 10, pLapClientService.getMatchCount());

        // check if method returns null on no match.
        pCards[0] = new CardImpl(13);
        Assert.assertNull(pLapClientService.checkCardMatch(pCards[0].toString(), pCards, 4));

    }

    @Test
    public void checkPictureCardMissmatch() {
        Card[] pCards = getPCards();
        pCards[0] = new CardImpl(1, 10); // set to a non picture card, otherwise checks will not work expected.
        pCards[1] = new CardImpl(3); // check match with invalid string.
        ((PLapClientServiceImpl) pLapClientService).setPCards(pCards); // set cards

        Assert.assertNull(pLapClientService.checkCardMatch(pCards[0].toString(), pCards, 4));
        Assert.assertNull(pLapClientService.checkCardMatch("invalid", pCards, 4));
    }
    @Test
    public void playerCardsGetterTest() {
        Assert.assertEquals(10, pLapClientService.getPCards().length);
    }


    /**
     * Helper Method to generate pyramid lap cards.
     * @return pCards.
     */
    private Card[] getPCards() {
        Deck deck = new DeckImpl();
        Card[] pCards = new Card[10];
        for(int i = 0; i < pCards.length; i++) {
            pCards[i] = deck.drawCard();
        }

        return pCards;
    }

}
