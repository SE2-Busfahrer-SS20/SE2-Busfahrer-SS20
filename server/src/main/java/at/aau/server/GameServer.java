package at.aau.server;

import java.io.IOException;

import at.aau.server.service.GameService;
import at.aau.server.service.impl.GameServiceImpl;
import shared.model.GameState;
import shared.model.impl.PlayerImpl;
import shared.networking.NetworkServer;
import shared.networking.dto.BaseMessage;
import shared.networking.dto.CreateGameMessage;
import shared.networking.dto.RegisterMessage;
import shared.networking.dto.ServerActionResponse;
import shared.networking.dto.TextMessage;
import shared.networking.kryonet.NetworkServerKryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

import static shared.networking.kryonet.NetworkConstants.CLASS_LIST;


public class GameServer extends NetworkServerKryo implements Runnable{


    private Thread thread;
    private NetworkServer server;
    private GameService gameService;


    public GameServer() {
        server = new NetworkServerKryo();
        gameService = new GameServiceImpl();
        registerClass(TextMessage.class);
        registerClasses();
    }

    @Override
    public void run() {
        while(true) {
            if (gameService.gameExists()) {
                play(gameService.getGameState());
            }

        }
    }

    // TODO: extract Listener to Listener Factory.
    @Override
    public void start() throws IOException {
        super.start();
        super.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                // check if the game is null, to prevent NullPointerExceptions.
                if (object == null) {
                    System.out.println("Object is null");
                } else {
                    if (!gameService.gameExists()) { // in case that no game instance exists.
                        if (object instanceof CreateGameMessage) {
                            CreateGameMessage msg = (CreateGameMessage) object;
                            gameService.createGame(msg.getPlayerCount());
                            Log.debug("Game created.");
                            // send result to client.
                            connection.sendTCP(new ServerActionResponse("Game created.", true));
                        } else if (object instanceof BaseMessage) {
                            Log.info("Action not supported.");
                            connection.sendTCP(new TextMessage("Action not supported."));
                        }
                    } else if (object instanceof BaseMessage) {
                        if (object instanceof RegisterMessage) {
                            RegisterMessage msg = (RegisterMessage) object;
                            if (!gameService.gameReady()) {
                                gameService.addPlayer(new PlayerImpl(msg.getPlayerName(), connection));
                                Log.debug("Player registered.");
                                // send result to client.
                                connection.sendTCP(new ServerActionResponse("Player registered.", true));
                            }
                        }
                    }

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

    private void registerClasses() {
        for (Class c : CLASS_LIST)
            registerClass(c);
    }

}
