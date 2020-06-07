package at.aau.server;


import org.junit.After;
import org.junit.Before;

import org.junit.Test;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import shared.networking.NetworkClient;
import shared.networking.dto.TextMessage;
import shared.networking.kryonet.NetworkClientKryo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class GameServerTest {


    private GameServer gameServer;

    /**
     * Destroy object to support garbage collector.
     */
    @After
    public void destroy() {
        gameServer = null;
    }

    private static final String REQUEST_TEST = "request test";
    private static final String RESPONSE_TEST = "response test";

    private AtomicBoolean request1Handled;
    private AtomicBoolean request2Handled;
    private AtomicBoolean responseHandled;

    @Before
    public void setup() {
        gameServer = new GameServer();
        request1Handled = new AtomicBoolean(false);
        request2Handled = new AtomicBoolean(false);
        responseHandled = new AtomicBoolean(false);
    }

    @Test
    public void NetworkConnection_OneClient_SendAndReceiveText() throws IOException, InterruptedException {
        System.out.printf("Main Thread ID: %d%n", Thread.currentThread().getId());

        startServer();
        startClient();

        // wait for server and client to handle messages
        Thread.sleep(1500);

        assertTrue(request1Handled.get());
        assertTrue(request2Handled.get());
        assertTrue(responseHandled.get());
    }

    private void startServer() throws IOException {
        AtomicBoolean first = new AtomicBoolean(true);

        // NetworkServer server = new NetworkServerKryo();
        // registerClassesForComponent((NetworkServerKryo)server);

        gameServer.start();
        gameServer.registerCallback(argument -> {
                    System.out.printf("Server Thread ID: %d%n", Thread.currentThread().getId());

            request1Handled.set(true);
            assertTrue(argument instanceof TextMessage);

            assertEquals(REQUEST_TEST, ((TextMessage) argument).getText());
            request2Handled.set(true);

            gameServer.broadcastMessage(new TextMessage(RESPONSE_TEST));
                }
        );
    }

    private void startClient() throws IOException {
        NetworkClient client = NetworkClientKryo.getInstance();
        // registerClassesForComponent((NetworkClientKryo)client);

        client.connect("localhost");
        client.registerCallback(TextMessage.class, argument ->
                {
                    System.out.printf("Client Thread ID: %d%n", Thread.currentThread().getId());

                    assertTrue(argument instanceof TextMessage);
                    assertEquals(RESPONSE_TEST, ((TextMessage) argument).getText());
                    responseHandled.set(true);
                }
        );

      //  client.sendMessage(new TextMessageSubClass());
        client.sendMessage(new TextMessage(REQUEST_TEST));
    }

}
