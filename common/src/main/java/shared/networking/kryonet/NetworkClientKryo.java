package shared.networking.kryonet;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import java.io.IOException;

import shared.model.GameState;
import shared.model.impl.PlayersStorageImpl;
import shared.networking.Callback;
import shared.networking.NetworkClient;
import shared.networking.dto.BaseMessage;
import shared.networking.dto.ConfirmRegisterMessage;
import shared.networking.dto.NewPlayerMessage;
import shared.networking.dto.StartGameMessage;
import shared.networking.dto.TextMessage;
import shared.networking.dto.UpdateMessage;

import static shared.networking.kryonet.NetworkConstants.CLASS_LIST;

public class NetworkClientKryo implements NetworkClient, KryoNetComponent {
    private Client client;
    private Callback<BaseMessage> callback;
    private PlayersStorageImpl playersStorage = PlayersStorageImpl.getInstance();


    public NetworkClientKryo() {
        client = new Client();
        registerClasses();
    }
    public void registerClass(Class c) {
         client.getKryo().register(c);
   }


    @Override
    public void connect(String host) throws IOException {
        client.start();
        client.connect(5000, host, NetworkConstants.TCP_PORT, NetworkConstants.UDP_PORT);
        //Here the client receives messages from the server !

        client.addListener(new Listener() {
            public void received(Connection connection, Object object) {

                if(object instanceof ConfirmRegisterMessage){
                    Log.debug("Registration Confirmed");
                    playersStorage.setMaster(((ConfirmRegisterMessage)object).isMaster());
                    playersStorage.setCards(((ConfirmRegisterMessage)object).getCards());
                    playersStorage.setTempID(((ConfirmRegisterMessage)object).getID());

                    System.out.println("!!!!!--------Stored TempID:"+playersStorage.getTempID()+"--------!!!!!");
                }

                if(object instanceof NewPlayerMessage){
                    Log.debug("New Player in the Game");
                    playersStorage.addPlayerName(((NewPlayerMessage)object).getPlayerName());
                }

                if(object instanceof StartGameMessage){
                    Log.debug("Game can start now");
                    playersStorage.setState(GameState.READY);
                }

                if(object instanceof UpdateMessage){
                    System.out.println("RECEIVED UPDATE MESSAGE ON CLIENT SIDE!!");
                    //update players score in User Interface (feature does not exist yet)
                    UpdateMessage uM = (UpdateMessage)object;
                    playersStorage.updateOnMessage(uM.getScore(),uM.getCurrentPlayer());

                }


                if (callback != null && object instanceof BaseMessage) {
                    callback.callback((BaseMessage) object);
                    Log.debug("Callback is instance of BaseMessage");
                    if(object instanceof TextMessage){
                        Log.debug(((TextMessage)(object)).getText());
                    }

                }
            }
        });
    }

    public void registerCallback(Callback<BaseMessage> callback) {
        this.callback = callback;
    }

    public void sendMessage(BaseMessage message) {
        client.sendTCP(message);
    }

    private void registerClasses() {
        for (Class c : CLASS_LIST)
            registerClass(c);
    }

}
