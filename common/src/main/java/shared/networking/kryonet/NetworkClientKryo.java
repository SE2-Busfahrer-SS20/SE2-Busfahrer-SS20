package shared.networking.kryonet;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import shared.model.GameState;
import shared.model.impl.PlayersStorageImpl;
import shared.networking.Callback;
import shared.networking.NetworkClient;
import shared.networking.dto.*;

import static shared.networking.kryonet.NetworkConstants.CLASS_LIST;

public class NetworkClientKryo implements NetworkClient, KryoNetComponent {

    private Client client;
    private PlayersStorageImpl playersStorage = PlayersStorageImpl.getInstance();
    // Callback Map to store callbacks for every DTO Class. HashMap to access object in O(1).
    private Map<Class, Callback<BaseMessage>> callbackMap = new HashMap<>();
    private static NetworkClient instance;

    private NetworkClientKryo() {
        client = new Client();
        registerClasses();
    }

    public void registerClass(Class c) {
        client.getKryo().register(c);
    }


    @Override
    public void connect(String host) throws IOException {
        client.start();
        client.connect(5000, host, NetworkConstants.TCP_PORT);
        //Here the client receives messages from the server !

        client.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                // handle null objects or not known Objects.
                if (!(object instanceof BaseMessage)) {
                    // Log.info("Network Client Listener Error: Received Object is null or not from Type BaseMessage.");
                } else if (object instanceof ConfirmRegisterMessage) {
                    Log.debug("Registration Confirmed");
                    playersStorage.setMaster(((ConfirmRegisterMessage)object).isMaster());
                    playersStorage.setCards(((ConfirmRegisterMessage)object).getCards());
                    playersStorage.setTempID(((ConfirmRegisterMessage)object).getID());

                }

                if(object instanceof NewPlayerMessage){
                    Log.debug("New Player in the Game");
                    //playersStorage.addPlayerName(((NewPlayerMessage)object).getPlayerName());
                    playersStorage.addPlayer(((NewPlayerMessage)object).getPlayer());
                }

                if(object instanceof StartGameMessage){
                    StartGameMessage sgm = (StartGameMessage) object;
                    Log.debug("Game can start now");
                    //playersStorage.setPlayerFromDTO(((StartGameMessage)object).getPlayerList());
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
                // just for debugging purposes.
                if (object instanceof TextMessage) {
                    Log.debug("Callback is instance of TextMessage");
                    Log.debug("Text of TextMessage: "+((TextMessage) (object)).getText());
                    if(callbackMap.get(TextMessage.class)!=null)
                    callbackMap.get(TextMessage.class).callback((BaseMessage) object);
                }
                if (object instanceof CheatedMessage) {
                    Log.debug("CheatedMessage received");
                    System.out.println("\n\n\n CLIENT : "+((CheatedMessage) object).hasCheated()+"\n\n\n");
                    playersStorage.setCheating(((CheatedMessage) object).getTempID());
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
        for (Class c : CLASS_LIST)
            registerClass(c);
    }

    public static synchronized NetworkClient getInstance() {
        if (instance == null)
            instance = new NetworkClientKryo();
        return instance;
    }
}
