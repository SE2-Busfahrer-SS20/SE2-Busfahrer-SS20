package at.aau.server;

import java.io.IOException;
import java.util.List;

import at.aau.server.database.Database;
import at.aau.server.database.Table.User;
import at.aau.server.service.GameService;
import at.aau.server.service.PLapService;
import at.aau.server.service.impl.GameServiceImpl;
import at.aau.server.service.impl.PLapServiceImpl;
import shared.model.Player;
import shared.model.PlayerDTO;
import shared.model.impl.PlayerDTOImpl;
import shared.networking.dto.*;
import shared.networking.kryonet.NetworkServerKryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

import static shared.networking.kryonet.NetworkConstants.CLASS_LIST;


public class GameServer extends NetworkServerKryo {


    private static final String REQUEST_TEST = "request test";
    private static final String RESPONSE_TEST = "response test";
    private static final int CONN_RETRY = 5;

    private GameService gameService;
    private PLapService pLapService;
    private Database db;
    private Connection connectionToMaster;

    GameServer() {
        Log.set(Log.LEVEL_DEBUG); // set log level for Minlog.
        gameService = GameServiceImpl.getInstance();
        pLapService = new PLapServiceImpl(this);
        db= Database.getInstance();
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
                        if(object instanceof LeaderboardMessage){
                            Log.info("LeaderboardMessage received!");
                            try {
                                List<PlayerDTO> playerDTOList=db.getLeaderboardAscending();
                                connection.sendTCP(new LeaderboardMessage(playerDTOList));

                            }
                            catch (Exception e){
                                e.printStackTrace();
                                Log.error("Failed to query db!");
                            }
                        }
                        else if (object instanceof RegisterMessage) {
                            Log.debug("Received Register Message");
                            try {
                                RegisterMessage msg = (RegisterMessage) object;
                                gameService.createGame(msg.getPlayerName(), msg.getMACAddress(), connection);  //outsourced to GameService
                                checkGameStates();
                                connectionToMaster = connection;

                            } catch (Exception ex) {
                                Log.error(ex.toString());
                                // TODO: implement client error response and implement error handler in client.
                            }
                        }

                        else if (object instanceof BaseMessage) {
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
                            //NewPlayerMessage npm = new NewPlayerMessage(player.getName());
                            NewPlayerMessage npm = new NewPlayerMessage(PlayerDTOImpl.getDTOFromPlayer(player));
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
                        gameService.GuessRound(pM.getLap(), pM.getTempID(), pM.scored());
                    }

                    //Bushmen-Round
                    else if (object instanceof BushmenMessage){
                        BushmenMessage bushmenMessage = new BushmenMessage();
                        bushmenMessage.setCards(gameService.getBushmenCards());
                        for (int i = 0; i < gameService.getPlayerCount(); i++) {
                            gameService.getPlayerList().get(i).getConnection().sendTCP(bushmenMessage);
                        }
                    }

                    else if (object instanceof BushmenCardMessage){
                        for (int i = 0; i < gameService.getPlayerCount(); i++) {
                            gameService.getPlayerList().get(i).getConnection().sendTCP(object);
                        }
                        System.out.println("Send card to players"+object+gameService.getPlayerList());
                    }


                    // Player has cheated message
                    else if(object instanceof CheatedMessage){
                        CheatedMessage cM = (CheatedMessage) object;
                        if(cM.hasCheated()) {
                            gameService.getPlayerList().get(cM.getTempID()).setCheatedThisRound(true);
                        }
                        int playerId = cM.getTempID();
                        CheatedMessage updateClients = new CheatedMessage(playerId,true, cM.getTimeStamp(), cM.getCheatType());
                        for (int i = 0; i < gameService.getPlayerList().size() ; i++) {
                            gameService.getPlayerList().get(i).getConnection().sendTCP(updateClients);
                        }
                    }
                    else if(object instanceof CoughtMessage){
                        CoughtMessage coughtMessage = (CoughtMessage)object;
                        //Set the new Score of the Cheater
                        gameService.getPlayerList().get(coughtMessage.getIndexCheater()).setScore(coughtMessage.getScoreCheater());
                        //Set the new Score of the one how Cought
                        gameService.getPlayerList().get(coughtMessage.getIndexCought()).setScore(coughtMessage.getScoreCought());
                        //Update the list by every client
                        CoughtMessage updateClients = new CoughtMessage(coughtMessage.getIndexCheater(),coughtMessage.getScoreCought(), coughtMessage.getScoreCheater(), coughtMessage.getScoreCought(),coughtMessage.isCheated());
                        for (int i = 0; i < gameService.getPlayerList().size() ; i++) {
                            gameService.getPlayerList().get(i).getConnection().sendTCP(updateClients);
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

    private void checkGameStates() {
        new Thread(() -> {
            try {
                short connLostCounter = 0;
                while (this.gameService.gameExists()) {
                    Thread.sleep(1000);
                    int currentConnections = this.getConnections().length;
                    if (connLostCounter > CONN_RETRY) {
                        gameService.endGame();
                        Log.info("Game ended unexpected.");
                    } else if (currentConnections < gameService.getPlayerList().size()) {
                        connLostCounter++;
                        Log.debug("Connection lost. ConnLostCounter: " + connLostCounter);
                    } else {
                        connLostCounter = 0;
                        Log.debug("ConnLostCouter was set to 0.");
                    }
                }
            } catch (Exception e) {
                Log.error(e.toString());
            }
        }).start();
    }
    private void registerClasses() {
        for (Class c : CLASS_LIST)
            registerClass(c);
    }

}
