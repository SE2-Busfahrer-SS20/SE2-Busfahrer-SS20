package at.aau.server;

import java.io.IOException;
import at.aau.server.service.GameService;
import at.aau.server.service.PLabService;
import at.aau.server.service.impl.GameServiceImpl;
import at.aau.server.service.impl.PLabServiceImpl;
import shared.model.GameState;
import shared.model.Player;
import shared.networking.dto.BaseMessage;
import shared.networking.dto.ConfirmRegisterMessage;
import shared.networking.dto.NewPlayerMessage;
import shared.networking.dto.RegisterMessage;
import shared.networking.dto.ServerActionResponse;
import shared.networking.dto.StartGameMessage;
import shared.networking.dto.TextMessage;
import shared.networking.kryonet.NetworkServerKryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

import static shared.networking.kryonet.NetworkConstants.CLASS_LIST;



public class GameServer extends NetworkServerKryo implements Runnable{

    private static final String REQUEST_TEST = "request test";
    private static final String RESPONSE_TEST = "response test";

    private Thread thread;
    private GameService gameService;
    private PLabService pLabService;

    private Connection connectionToMaster;

    public GameServer() {
        Log.set(Log.LEVEL_DEBUG); // set log level for Minlog.
        gameService = GameServiceImpl.getInstance();
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
                            Log.debug("Received Register Message");
                            try {
                                RegisterMessage msg = (RegisterMessage) object;
                                gameService.createGame();  //Create empty Game Object
                                Player player = gameService.addPlayer(msg.getPlayerName(),msg.getMACAdress(),connection);

                                // send result to client.
                                ConfirmRegisterMessage crm = new ConfirmRegisterMessage(player, true);
                                connection.sendTCP(crm);//sendet ConfirmRegisterMessage an Client

                                //Add Player to Playerlist in Wait UI
                                NewPlayerMessage npm = new NewPlayerMessage(player.getName());
                                connection.sendTCP(npm);

                                //Define current client as master
                                connectionToMaster = connection;

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

                    //join existing Game
                    else if(object instanceof RegisterMessage){
                        Log.debug("Received Register Message");

                        RegisterMessage msg = (RegisterMessage) object;
                        Player player = gameService.addPlayer(msg.getPlayerName(),msg.getMACAdress(),connection);

                        if(player!=null){ //if game is not full
                            Log.debug("new Player:"+player.getName());
                            ConfirmRegisterMessage crm = new ConfirmRegisterMessage(player);
                            connection.sendTCP(crm);

                            //Send message to Master to appear in PlayersList
                            NewPlayerMessage npm = new NewPlayerMessage(player.getName());
                            connectionToMaster.sendTCP(npm);
                        }
                        else{
                        connection.sendTCP(new ServerActionResponse("Game is full!", false));
                        }
                    }
                    else if(object instanceof StartGameMessage){
                        Log.debug("Game Started");
                        gameService.startGame();

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
                    //startGame();
                break;
            case STARTED:
                // TODO: implement.
                break;
            // Starts guess lab.
            case LAB1:
                // TODO: implement.
                break;
            // Starts pyramiden lab.
            case LAB2READY:
                this.gameService.nextLab(); // TODO: align next LAB Method.
                startLab2();
                break;
            // Starts busdriver lab.
            case LAB3:
                // TODO: implement.
                break;
            case ENDED:
                // TODO: implement.
                break;
        }
    }

    private void startLab2() {
        pLabService = new PLabServiceImpl(this.gameService.getGame());
        pLabService.startLab();
    }


    private void registerClasses() {
        for (Class c : CLASS_LIST)
            registerClass(c);
    }

}
