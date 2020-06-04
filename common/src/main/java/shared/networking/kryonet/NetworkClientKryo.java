package shared.networking.kryonet;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import shared.model.CoughtServiceListener;
import shared.model.CoughtServiceListenerPlap;
import shared.model.GameState;
import shared.model.impl.PlayersStorageImpl;
import shared.networking.Callback;
import shared.networking.NetworkClient;
import shared.networking.dto.*;

import static shared.networking.kryonet.NetworkConstants.getClassList;

public class NetworkClientKryo implements NetworkClient, KryoNetComponent {

    private Client client;
    private PlayersStorageImpl playersStorage = PlayersStorageImpl.getInstance();
    // Callback Map to store callbacks for every DTO Class. HashMap to access object in O(1).
    private Map<Class, Callback<BaseMessage>> callbackMap = new HashMap<>();
    private static NetworkClient instance;
    Listener listenLeaderboardmessage;

    private NetworkClientKryo() {
        client = new Client();
        registerClasses();
    }

    public void registerClass(Class c) {
        client.getKryo().register(c);
    }

    @Override
    public void close(){
        client.removeListener(listenLeaderboardmessage);
        client.close();
    }
    @Override
    public void connect(String host) throws IOException {
        client.start();
        client.connect(5000, host, NetworkConstants.TCP_PORT);
        //Here the client receives messages from the server !
        listenLeaderboardmessage= new Listener(){
            @Override
            public void received(Connection connection, Object object) {

                if(object instanceof LeaderboardMessage) {
                    Log.debug("\n=====================\nLeaderboardMessage received");
                    callbackMap.get(LeaderboardMessage.class).callback((LeaderboardMessage)object);
                }
            }
        };

        client.addListener(listenLeaderboardmessage);
        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {

                if (object instanceof ConfirmRegisterMessage) {
                    Log.debug("Registration Confirmed");
                    playersStorage.setMaster(((ConfirmRegisterMessage)object).isMaster());
                    playersStorage.setCards(((ConfirmRegisterMessage)object).getCards());
                    playersStorage.setTempID(((ConfirmRegisterMessage)object).getID());

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

                if(object instanceof UpdateMessage){
                    UpdateMessage uM = (UpdateMessage)object;
                    playersStorage.updateOnMessage(uM.getPlayerList(),uM.getCurrentPlayer());

                }

                if (object instanceof StartPLabMessage) {
                    Log.info("StartPlaBMesssage received");
                    // call the correct callback to store cards and update UI Thread.
                    callbackMap.get(StartPLabMessage.class).callback((BaseMessage) object);
                }

                if (object instanceof WinnerLooserMessage) {
                    Log.info("WinnerLooserMessage received");
                    // call the correct callback to store cards and update UI Thread.
                    callbackMap.get(WinnerLooserMessage.class).callback((BaseMessage) object);
                }

                if (object instanceof BushmenMessage) {
                  // BushmenMessage bushmenMessage = (BushmenMessage) object;
                    Log.info("Bushmen received");
                   // playersStorage.setBushmenCards(bushmenMessage.getCards());
                    callbackMap.get(BushmenMessage.class).callback((BaseMessage) object);

                }

                if (object instanceof BushmenCardMessage) {
                    Log.info("BushmenCard received" + object);
                    callbackMap.get(BushmenCardMessage.class).callback((BushmenCardMessage) object);

                }

                // just for debugging purposes.
                if (object instanceof TextMessage) {
                    Log.debug("Callback is instance of TextMessage");
                    Log.debug("Text of TextMessage: "+((TextMessage) (object)).getText());
                    if(callbackMap.get(TextMessage.class)!=null)
                        callbackMap.get(TextMessage.class).callback((BaseMessage) object);
                }
                if (object instanceof CheatedMessage) {
                    Log.debug("CheatedMessage received");
                    Log.debug("\n\n\n CLIENT : "+((CheatedMessage) object).hasCheated()+"\n\n\n");
                    playersStorage.setCheating(((CheatedMessage) object).getTempID());
                }
                if(object instanceof CoughtMessage){
                    Log.debug("CoughtMessage received");
                    CoughtMessage coughtMessage = (CoughtMessage)object;
                    playersStorage.getPlayerList().get(coughtMessage.getIndexCheater()).setScore(coughtMessage.getScoreCheater());
                    playersStorage.getPlayerList().get(coughtMessage.getIndexCought()).setScore(coughtMessage.getScoreCought());

                    setTextViewVisible();
                    //Kann zu problemen kommen wenn der boolean cheted auf false gesetzt wird,
                    if(playersStorage.getTempID() == coughtMessage.getIndexCheater() && playersStorage.getPlayerList().get(coughtMessage.getIndexCheater()).isCheating() ){
                        setTextViewVisiblePlap();
                    }
                }
            }
        });
    }

    public void registerCallback(Class dtoClass, Callback<BaseMessage> callback) {
        this.callbackMap.put(dtoClass, callback);
    }

    public void sendMessage(BaseMessage message) {
        client.sendTCP(message);
    }

    private void registerClasses() {
        for (Class c : getClassList())
            registerClass(c);
    }

    public static synchronized NetworkClient getInstance() {
        if (instance == null)
            instance = new NetworkClientKryo();
        return instance;
    }

    private CoughtServiceListener coughtServiceListener;
    private CoughtServiceListenerPlap coughtServiceListenerPlap;

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



}
