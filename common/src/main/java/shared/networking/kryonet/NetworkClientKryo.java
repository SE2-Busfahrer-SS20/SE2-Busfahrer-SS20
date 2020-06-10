package shared.networking.kryonet;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import shared.model.CoughtServiceListener;
import shared.model.CoughtServiceListenerBushmen;
import shared.model.CoughtServiceListenerPlap;
import shared.model.GameState;
import shared.model.impl.PlayersStorageImpl;
import shared.networking.Callback;
import shared.networking.NetworkClient;
import shared.networking.dto.*;

import static shared.networking.kryonet.NetworkConstants.getClassList;

public class NetworkClientKryo implements NetworkClient, KryoNetComponent {

    private final Client client;
    private final PlayersStorageImpl playersStorage = PlayersStorageImpl.getInstance();
    // Callback Map to store callbacks for every DTO Class. HashMap to access object in O(1).
    private final Map<Class<?>, Callback<BaseMessage>> callbackMap = new HashMap<>();
    private static NetworkClient instance;

    private NetworkClientKryo() {
        client = new Client();
        registerClasses();
    }

    public void registerClass(Class<?> c) {
        client.getKryo().register(c);
    }

    @Override
    public void close(){
        //client.removeListener(listenLeaderboardmessage);
        client.close();
    }

    @Override
    public void connect(String host) throws IOException {
        client.start();
        client.connect(5000, host, NetworkConstants.TCP_PORT);
        //Here the client receives messages from the server !

        client.addListener(createBasicListener());
        client.addListener(createBushmenListener());
        client.addListener(createGameListener());
        client.addListener(createCheatListener());
        client.addListener(createLeaderboardListener());
        client.addListener(createStartPLapListener());
        client.addListener(createWinnerLoserListener());

    }
    private Listener createLeaderboardListener() {
        return new Listener(){
            @Override
            public void received(Connection connection, Object object) {

                if(object instanceof LeaderboardMessage) {
                    Log.debug("LeaderboardMessage received");
                    callbackMap.get(LeaderboardMessage.class).callback((LeaderboardMessage)object);
                }
            }
        };
    }
    private Listener createWinnerLoserListener() {
        return new Listener(){
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof WinnerLooserMessage) {
                    Log.info("WinnerLooserMessage received");
                    // call the correct callback to store cards and update UI Thread.
                    callbackMap.get(WinnerLooserMessage.class).callback((BaseMessage) object);
                }

            }
        };
    }
    private Listener createStartPLapListener() {
        return new Listener(){
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof StartPLapMessage) {
                    Log.info("StartPlaBMesssage received");
                    // call the correct callback to store cards and update UI Thread.
                    callbackMap.get(StartPLapMessage.class).callback((BaseMessage) object);
                }

            }
        };
    }
    private Listener createBasicListener() {
        return new Listener(){
            @Override
            public void received(Connection connection, Object object) {
                if(object instanceof UpdateMessage){
                    UpdateMessage uM = (UpdateMessage)object;
                    playersStorage.updateOnMessage(uM.getPlayerList(),uM.getCurrentPlayer());

                }
                // just for debugging purposes.
                if (object instanceof TextMessage) {
                    Log.debug("Callback is instance of TextMessage");
                    Log.debug("Text of TextMessage: "+((TextMessage) (object)).getText());
                    if(callbackMap.get(TextMessage.class)!=null)
                        callbackMap.get(TextMessage.class).callback((BaseMessage) object);
                }
            }
        };
    }
    private Listener createCheatListener() {
        return new Listener(){
            @Override
            public void received(Connection connection, Object object) {

                if (object instanceof CheatedMessage) {
                    Log.debug("CheatedMessage received");
                    Log.debug("\n\n\n CLIENT : "+((CheatedMessage) object).hasCheated()+"\n\n\n");
                    playersStorage.setCheating(((CheatedMessage) object).getTempID());
                }
                if(object instanceof CoughtMessage){
                    Log.error("CoughtMessage received");
                    CoughtMessage coughtMessage = (CoughtMessage)object;
                    playersStorage.getPlayerList().get(coughtMessage.getIndexCheater()).setScore(coughtMessage.getScoreCheater());
                    playersStorage.getPlayerList().get(coughtMessage.getIndexCought()).setScore(coughtMessage.getScoreCought());
                    //Listener for the Cheater for the TextView for the GuessRound
                    setTextViewVisible();
                    //Listener for the Cheater for the TextView in Plap
                    if(playersStorage.getTempID() == coughtMessage.getIndexCheater() && playersStorage.getPlayerList().get(coughtMessage.getIndexCheater()).isCheating() &&playersStorage.getState() == GameState.LAP2 ){
                        setTextViewVisiblePlap();
                        Log.error("Listener Cheater received PLAP Caught.");
                    }
                    if(playersStorage.getTempID() == coughtMessage.getIndexCheater() && playersStorage.getPlayerList().get(coughtMessage.getIndexCheater()).isCheating() &&playersStorage.getState() == GameState.LAP3 ){
                        setTextViewVisibleBushmen();
                        Log.error("Listener Cheater received Bushmen Caught.");
                    }
                }
            }
        };
    }
    private Listener createBushmenListener() {
        return new Listener(){
            @Override
            public void received(Connection connection, Object object) {

                if (object instanceof BushmenMessage) {
                    // BushmenMessage bushmenMessage = (BushmenMessage) object;
                    Log.info("Bushmen received");
                    // playersStorage.setBushmenCards(bushmenMessage.getCards());
                    if(callbackMap.get(BushmenMessage.class) != null)
                        callbackMap.get(BushmenMessage.class).callback((BaseMessage) object);
                    else
                        Log.error("BushmenMessage.class callback is null!");

                }

                if (object instanceof BushmenCardMessage) {
                    Log.info("BushmenCard received" + object);
                    callbackMap.get(BushmenCardMessage.class).callback((BushmenCardMessage) object);

                }
            }
        };
    }
    private Listener createGameListener() {
        return new Listener(){
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof ConfirmRegisterMessage) {
                    Log.debug("Registration Confirmed");
                    playersStorage.setMaster(((ConfirmRegisterMessage)object).isMaster());
                    playersStorage.setCards(((ConfirmRegisterMessage)object).getCards());
                    playersStorage.setTempID(((ConfirmRegisterMessage)object).getID());

                    if(((ConfirmRegisterMessage)object).isMaster()){
                        callbackMap.get(ConfirmRegisterMessage.class).callback((BaseMessage) object);
                    }
                }

                if(object instanceof NewPlayerMessage){
                    Log.debug("New Player in the Game");
                    boolean alreadyExists=false;
                    for(int i=0;i<playersStorage.getPlayerList().size();i++){
                        if(playersStorage.getPlayerList().get(i).getName().equals(((NewPlayerMessage)object).getPlayer().getName())){
                            alreadyExists=true;
                        }
                    }
                    if(!alreadyExists){
                        playersStorage.addPlayer(((NewPlayerMessage)object).getPlayer());
                    }


                }

                if(object instanceof StartGameMessage){
                    Log.debug("Game can start now");
                    playersStorage.setPlayerFromDTO(((StartGameMessage)object).getPlayerList());

                    playersStorage.setState(GameState.READY);
                }

            }
        };
    }

    public void registerCallback(Class dtoClass, Callback<BaseMessage> callback) {
        this.callbackMap.put(dtoClass, callback);
    }

    public void sendMessage(BaseMessage message) {
        client.sendTCP(message);
    }

    private void registerClasses() {
        for (Class<?> c : getClassList())
            registerClass(c);
    }

    public static synchronized NetworkClient getInstance() {
        if (instance == null)
            instance = new NetworkClientKryo();
        return instance;
    }

    private CoughtServiceListener coughtServiceListener;
    private CoughtServiceListenerPlap coughtServiceListenerPlap;
    private CoughtServiceListenerBushmen coughtServiceListenerBushmen;

    public void coughtCallback(CoughtServiceListener coughtServiceListener){
        this.coughtServiceListener = coughtServiceListener;
    }

    public void setTextViewVisible(){
        new Thread(() -> coughtServiceListener.coughtTetxViewListener()).start();
    }

    public void coughtCallbackPlap(CoughtServiceListenerPlap coughtServiceListenerPlap){
        this.coughtServiceListenerPlap = coughtServiceListenerPlap;
    }

    public void setTextViewVisiblePlap(){
        new Thread(() -> coughtServiceListenerPlap.coughtTextViewListenerPlap()).start();
    }

    public void coughtCallbackBushmen(CoughtServiceListenerBushmen coughtServiceListenerBushmen){
        this.coughtServiceListenerBushmen = coughtServiceListenerBushmen;
    }

    public void setTextViewVisibleBushmen(){
        new Thread(() -> coughtServiceListenerBushmen.coughtTextViewListenerBushmen()).start();
    }

    /**
     * Just for Testing purposes.
     */
    public Map<Class<?>, Callback<BaseMessage>> getCallbackMap() {
        return callbackMap;
    }


}
