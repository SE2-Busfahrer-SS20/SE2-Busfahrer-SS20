package at.aau.server;

import java.io.IOException;
import at.aau.server.service.GameService;
import at.aau.server.service.impl.GameServiceImpl;
import shared.model.GameState;
import shared.model.impl.PlayerImpl;
import shared.networking.dto.BaseMessage;
import shared.networking.dto.ConfirmRegisterMessage;
import shared.networking.dto.CreateGameMessage;
import shared.networking.dto.RegisterMessage;
import shared.networking.dto.ServerActionResponse;
import shared.networking.dto.TextMessage;
import shared.networking.kryonet.NetworkServerKryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

import javax.xml.soap.Text;

import static shared.networking.kryonet.NetworkConstants.CLASS_LIST;



public class GameServer extends NetworkServerKryo implements Runnable{

    private static final String REQUEST_TEST = "request test";
    private static final String RESPONSE_TEST = "response test";

    private Thread thread;
    private GameService gameService;


    public GameServer() {
        Log.set(Log.LEVEL_DEBUG); // set log level for Minlog.
        gameService = new GameServiceImpl();
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
        Log.debug("Server started successfully.");
        super.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                // check if the game is null, to prevent NullPointerExceptions.
                if (object == null) {
                    Log.debug("Object is null");
                } else if (object instanceof TextMessage && ((TextMessage) object).getText().equals(REQUEST_TEST)) {
                    messageCallback.callback((TextMessage) object);
                    connection.sendTCP(new TextMessage(RESPONSE_TEST));
                    Log.debug("Received TextMessage: " + ((TextMessage) object).getText());
                } else {

                    if (!gameService.gameExists()) {

                        if (object instanceof RegisterMessage) {
                            RegisterMessage msg = (RegisterMessage) object;
                            try {
                                gameService.createGame();  //Create empty Game Object
                                gameService.addPlayer(msg.getPlayerName(),msg.getMACAdress());

                                // send result to client.

                                //Respond with Cards of player #0
                                ConfirmRegisterMessage crm = new ConfirmRegisterMessage(0, gameService.getPlayersCards(0));


                                connection.sendTCP(crm);//sendet ConfirmRegisterMessage an Client
                                // Diese beinhaltet die Karten des Spielers mit der ID=0
                                //ID=0 ist immer jener Spieler, der das Spiel startet
                               Log.info("Game created.");
                            } catch (Exception ex) {
                                Log.error(ex.toString());
                                // TODO: implement client error response and implement error handler in client.
                            }
                        } else if (object instanceof BaseMessage) {
                            Log.info("Action not supported.");
                            connection.sendTCP(new TextMessage("Action not supported."));
                        }
                    }
                    //join Game
                    else if(object instanceof RegisterMessage){

                        //gameService.createGame(3);//just for test purpose to avoid creating new game in each test
                       Log.debug("Recived Register Message");

                        int ID = gameService.joinGame();
                        if(ID!=-1){     //if game is not full
                            Log.debug("Players ID="+ID);
                            ConfirmRegisterMessage crm = new ConfirmRegisterMessage(ID, gameService.getPlayersCards(ID));
                            connection.sendTCP(crm);
                            Log.debug("New player joined game ["+ID+"]");
                        }else{
                            connection.sendTCP(new ServerActionResponse("Game is full!", true)); // TODO: Action should be false in case of an error.
                        }
                    }
                      else if (object instanceof RegisterMessage) {
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
                // TODO: implement.
                break;
        }
    }

    private void startGame() {
        Log.debug("Game started.");
        // TODO: implement start game.
    }

    private void registerClasses() {
        for (Class c : CLASS_LIST)
            registerClass(c);
    }

}
