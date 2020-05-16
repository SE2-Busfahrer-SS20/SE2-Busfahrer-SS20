package at.aau.server;

import java.io.IOException;
import at.aau.server.service.GameService;
import at.aau.server.service.PLapService;
import at.aau.server.service.impl.GameServiceImpl;
import at.aau.server.service.impl.PLapServiceImpl;
import shared.model.Player;
import shared.networking.dto.BaseMessage;
import shared.networking.dto.CheatedMessage;
import shared.networking.dto.ConfirmRegisterMessage;
import shared.networking.dto.NewPlayerMessage;
import shared.networking.dto.RegisterMessage;
import shared.networking.dto.ServerActionResponse;
import shared.networking.dto.StartGameMessage;
import shared.networking.dto.TextMessage;
import shared.networking.dto.PlayedMessage;
import shared.networking.kryonet.NetworkServerKryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

import static shared.networking.kryonet.NetworkConstants.CLASS_LIST;


public class GameServer extends NetworkServerKryo {


    private static final String REQUEST_TEST = "request test";
    private static final String RESPONSE_TEST = "response test";

    private GameService gameService;
    private PLapService pLapService;

    private Connection connectionToMaster;

    GameServer() {
        Log.set(Log.LEVEL_DEBUG); // set log level for Minlog.
        gameService = GameServiceImpl.getInstance();
        pLapService = new PLapServiceImpl(this);
        registerClasses();
    }

    // TODO: extract Listeners into Service functions.
    @Override
    public void start() throws IOException {
        super.start();
        // start PLab Service.
        pLapService.start();
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
                }
                else {

                    if (!gameService.gameExists()) {

                        if (object instanceof RegisterMessage) {
                            Log.debug("Received Register Message");
                            try {
                                RegisterMessage msg = (RegisterMessage) object;
                                gameService.createGame(msg.getPlayerName(), msg.getMACAddress(), connection);  //outsourced to GameService
                                connectionToMaster = connection;

                            } catch (Exception ex) {
                                Log.error(ex.toString());
                                // TODO: implement client error response and implement error handler in client.
                            }
                        } else if (object instanceof BaseMessage) {
                            String errmsg = "Action not supported.";
                            Log.info(errmsg);
                            connection.sendTCP(new TextMessage(errmsg));
                        }
                    }

                    //join existing Game
                    else if(object instanceof RegisterMessage){
                        Log.debug("Received Register Message");

                        RegisterMessage msg = (RegisterMessage) object;
                        Player player = gameService.addPlayer(msg.getPlayerName(),msg.getMACAddress(),connection);

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
                        Log.info("Game started");
                        gameService.startGame();
                    }
                    //Guess-Rounds
                    else if(object instanceof PlayedMessage){
                        PlayedMessage pM = (PlayedMessage) object;
                        if(pM.getLap()==1){     //Black or Red
                            gameService.GuessRound1(pM.getTempID(), pM.scored());
                        }
                    }

                    // Player has cheated message
                    else if(object instanceof CheatedMessage){
                        CheatedMessage cM = (CheatedMessage) object;
                        if(cM.hasCheated()){
                            gameService.getPlayerList().get(cM.getTempID()).setCheatedThisRound(true);
                        }
                    }

                    // Player has cheated message
                    else if(object instanceof CheatedMessage){
                        CheatedMessage cM = (CheatedMessage) object;
                        if(cM.hasCheated()){
                            gameService.getPlayerList().get(cM.getTempID()).setCheatedThisRound(true);
                        }
                    }

                    else if (object instanceof TextMessage) {
                        Log.info("Got message from client", ((TextMessage) object).getText());
                    }
                    else if (object instanceof BaseMessage) {
                        Log.info("Action not supported.");
                        connection.sendTCP(new TextMessage("Action not supported."));
                    }


                }
            }
        });
    }

    private void registerClasses() {
        for (Class c : CLASS_LIST)
            registerClass(c);
    }

}
