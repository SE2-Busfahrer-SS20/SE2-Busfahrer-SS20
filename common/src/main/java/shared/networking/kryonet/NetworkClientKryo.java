package shared.networking.kryonet;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;

import shared.model.impl.playersCards;
import shared.networking.Callback;
import shared.networking.NetworkClient;
import shared.networking.dto.BaseMessage;
import shared.networking.dto.ConfirmRegisterMessage;
import shared.networking.dto.TextMessage;

import static shared.networking.kryonet.NetworkConstants.CLASS_LIST;

public class NetworkClientKryo implements NetworkClient, KryoNetComponent {
    private Client client;
    private Callback<BaseMessage> callback;


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
        //Here the client recives messages from the server !

        client.addListener(new Listener() {
            public void received(Connection connection, Object object) {

                if(object instanceof ConfirmRegisterMessage){
                    System.out.println("Registration Confirmed");
                    //Von hier aus die Karten in GameServiceImpl oder wo anders in der App zu speichern führt zu Circle-Dependencies --> Redesign -> Listener verschieben?
                    //Oder Karten in Common Speichern? (--> so ist es jetzt)
                    playersCards.setCards(((ConfirmRegisterMessage)object).getCards());

                }

                if (callback != null && object instanceof BaseMessage) {    //Es scheint als würde die If-Bedingung am callback !=null scheitern
                    callback.callback((BaseMessage) object);
                    System.out.println("Callback is instance of BaseMessage");
                    if(object instanceof TextMessage){
                        System.out.println(((TextMessage)(object)).getText());
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
