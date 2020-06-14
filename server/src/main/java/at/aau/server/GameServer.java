package at.aau.server;

import java.io.IOException;
import java.util.List;
import at.aau.server.database.Database;
import at.aau.server.database.table.User;
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

import static shared.networking.kryonet.NetworkConstants.getClassList;

public class GameServer extends NetworkServerKryo {

    private static final String REQUEST_TEST = "request test";
    private static final String RESPONSE_TEST = "response test";
    private static final int CONN_RETRY = 5;

    private Database db;
    private final GameService gameService;
    private final PLapService pLapService;

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
        super.addListener(createBasicListener());
        super.addListener(createCheatListener());
        super.addListener(createBushmenListener());
        super.addListener(createGameListener());
        super.addListener(createGuessListener());
        super.addListener(createDBAccessListener());
    }

    private Listener createGuessListener() {
        return new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                //Guess-Rounds
                if (object instanceof PlayedMessage) {
                    PlayedMessage pM = (PlayedMessage) object;
                    gameService.guessRound(pM.getLap(), pM.getTempID(), pM.scored());
                }
            }
        };
    }
    private Listener createGameListener() {
        return new Listener() {
            // TODO: extract logical functionality.
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof RegisterMessage && !gameService.gameExists()) {
                    Log.debug("Received Register Message");
                    try {
                        RegisterMessage msg = (RegisterMessage) object;
                        gameService.createGame(msg.getPlayerName(), msg.getMacAddress(), connection);  //outsourced to GameService
                        checkGameStates();
                        connectionToMaster = connection;
                    } catch (Exception ex) {
                        Log.error(ex.toString());
                    }
                }
                //join existing Game
                else if (object instanceof RegisterMessage) {
                    Log.debug("Received Register Message");

                    RegisterMessage msg = (RegisterMessage) object;
                    Player player = gameService.addPlayer(msg.getPlayerName(), msg.getMacAddress(), connection);

                    if (player != null) { //if game is not full
                        Log.debug("new Player:" + player.getName());
                        ConfirmRegisterMessage crm = new ConfirmRegisterMessage(player);
                        connection.sendTCP(crm);

                        //Send message to Master to appear in PlayersList
                        NewPlayerMessage npm = new NewPlayerMessage(PlayerDTOImpl.getDTOFromPlayer(player));
                        connectionToMaster.sendTCP(npm);
                    } else {
                        connection.sendTCP(new ServerActionResponse("Game is full!", false));
                    }
                } else if (object instanceof StartGameMessage) {
                    Log.info("Game started");

                    gameService.startGame();

                }
            }
        };
    }

    private Listener createBushmenListener() {
        return new Listener() {
            // TODO: extract logical functionality.
            @Override
            public void received(Connection connection, Object object) {
                //Bushmen-Round
                if (object instanceof BushmenMessage) {
                    BushmenMessage bushmenMessage = new BushmenMessage();
                    bushmenMessage.setCards(gameService.getBushmenCards());
                    for (int i = 0; i < gameService.getPlayerCount(); i++) {
                        gameService.getPlayerList().get(i).getConnection().sendTCP(bushmenMessage);
                    }
                } else if (object instanceof BushmenCardMessage) {
                    for (int i = 0; i < gameService.getPlayerCount(); i++) {
                        gameService.getPlayerList().get(i).getConnection().sendTCP(object);
                    }
                }
            }
        };
    }

    /**
     * Listener for cheating functionality.
     *
     * @return new Listener.
     */
    private Listener createCheatListener() {
        // TODO: refactor to extract functionality in own class.
        return new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                // Player has cheated message
                if (object instanceof CheatedMessage) {
                    CheatedMessage cM = (CheatedMessage) object;
                    if (cM.hasCheated()) {
                        gameService.getPlayerList().get(cM.getTempID()).setCheatedThisRound(true);
                    }
                    int playerId = cM.getTempID();
                    CheatedMessage updateClients = new CheatedMessage(playerId, true, cM.getTimeStamp(), cM.getCheatType());
                    for (int i = 0; i < gameService.getPlayerList().size(); i++) {
                        gameService.getPlayerList().get(i).getConnection().sendTCP(updateClients);
                    }
                } else if (object instanceof CoughtMessage) {
                    CoughtMessage coughtMessage = (CoughtMessage) object;
                    //Set the new Score of the Cheater
                    gameService.getPlayerList().get(coughtMessage.getIndexCheater()).setScore(coughtMessage.getScoreCheater());
                    //Set the new Score of the one how Cought
                    gameService.getPlayerList().get(coughtMessage.getIndexCought()).setScore(coughtMessage.getScoreCought());
                    //Update the list by every client
                    CoughtMessage updateClients = new CoughtMessage(coughtMessage.getIndexCheater(), coughtMessage.getIndexCought(), coughtMessage.getScoreCheater(), coughtMessage.getScoreCought(), coughtMessage.isCheated());
                    for (int i = 0; i < gameService.getPlayerList().size(); i++) {
                        gameService.getPlayerList().get(i).getConnection().sendTCP(updateClients);
                    }

                }
            }
        };
    }

    /**
     * Create Listener for basic functionality.
     *
     * @return new Listener.
     */
    private Listener createBasicListener() {
        return new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object == null) {
                    Log.debug("Object is null");
                } else if (object instanceof TextMessage && ((TextMessage) object).getText().equals(REQUEST_TEST)) {
                    if (messageCallback != null)
                        messageCallback.callback((TextMessage) object);
                    connection.sendTCP(new TextMessage(RESPONSE_TEST));
                    Log.debug("Received TextMessage: " + ((TextMessage) object).getText());
                } else if (object instanceof BaseMessage && !(object instanceof LeaderboardMessage)) {
                    Log.info("Action not supported.");
                    connection.sendTCP(new TextMessage("Action not supported."));
                }
            }
        };
    }

    private Listener createDBAccessListener() {
        return new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if(object instanceof LeaderboardMessage){
                    Log.info("LeaderboardMessage received!");
                    try {
                        List<PlayerDTO> playerDTOList=db.getLeaderboardAscending();
                        connection.sendTCP(new LeaderboardMessage(playerDTOList));
                        connection.close();
                    }
                    catch (Exception e){
                        Log.error("Failed to query db!", e);
                    }
                }
                if(object instanceof SaveGameDataMessage){
                    Log.info("SaveGameDataMessage received!");
                    try {
                        PlayerDTO playerDTO=((SaveGameDataMessage)object).getPlayer();

                        User user = db.addUser(playerDTO.getMAC(), playerDTO.getName());
                        db.addScore(user.getId(), playerDTO.getScore());

                    }
                    catch (Exception e){
                        Log.error("Failed save data in DB!!", e);
                    }
                }
            }
        };
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
        for (Class<?> c : getClassList())
            registerClass(c);
    }

}
