package at.aau.server;

import java.io.IOException;

import at.aau.server.service.GameService;
import at.aau.server.service.impl.GameServiceImpl;
import shared.model.GameState;
import shared.model.impl.PlayerImpl;
import shared.networking.NetworkServer;
import shared.networking.dto.BaseMessage;
import shared.networking.dto.TextMessage;
import shared.networking.kryonet.NetworkServerKryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;


public class GameServer extends NetworkServerKryo implements Runnable{

    // TODO: delete before deploy.
    private final static int MOCK_COUNT = 1;

    private Thread thread;
    private NetworkServer server;
    private GameService gameService;

    public GameServer() {
        server = new NetworkServerKryo();

    }

    @Override
    public void run() {

    }

    // TODO: extract Listener to Listener Factory.
    @Override
    public void start() throws IOException {
        super.start();
        super.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof BaseMessage) {
                    if (((TextMessage) object).text.equals("createGame")) {
                        // TODO: implement correct palyer count.
                        gameService = new GameServiceImpl(MOCK_COUNT);
                        System.out.println("Game created");
                    } else if (((TextMessage) object).text.equals("registerGame")) {
                        gameService.addPlayer(new PlayerImpl());
                        System.out.println("Player registered.");
                    }
                } else {
                    // TODO: implement.
                }

            }
        });

        thread = new Thread(this);
        thread.start();
    }
    private void play(GameState state) {
        switch (state) {
            case INIT:
                if (this.gameService.gameReady())
                    startGame();
                break;
            case STARTED:
                // TODO: implement.
                break;
            case LAB1:
                // TODO: implement.
                break;
            case LAB2:
                // TODO: implement.
                break;
            case LAB3:
                // TODO: implement.
                break;
            case ENDED:
                gameService = null;
                break;
        }
    }

    private void startGame() {
        System.out.println("Game started.");
        // TODO: implement start game.
    }


}
