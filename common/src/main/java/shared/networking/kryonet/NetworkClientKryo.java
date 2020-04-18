package shared.networking.kryonet;


import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;

import shared.networking.Callback;
import shared.networking.NetworkClient;
import shared.networking.dto.BaseMessage;

import static shared.networking.kryonet.NetworkConstants.CLASS_LIST;

public class NetworkClientKryo implements NetworkClient, KryoNetComponent {
    private Client client;
    private Callback<BaseMessage> callback;
    private Thread thread;
    private String host;


    public NetworkClientKryo(String host) {
        client = new Client();
        this.host = host;
    }
    public void registerClass(Class c) {
         client.getKryo().register(c);
   }


    public void registerCallback(Callback<BaseMessage> callback) {
        this.callback = callback;
    }

    public void sendMessage(BaseMessage message) {
        client.sendTCP(message);
    }


    @Override
    public void run() {
        try {
            registerClasses();
            client.start();
            client.connect(5000, host, NetworkConstants.TCP_PORT, NetworkConstants.UDP_PORT);
            client.addListener(new Listener() {
                public void received(Connection connection, Object object) {
                    if (callback != null && object instanceof BaseMessage)
                        callback.callback((BaseMessage) object);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    @Override
    public void start() {
      Thread thread = new Thread(this);
      thread.start();
    }

    private void registerClasses() {
        for (Class c : CLASS_LIST)
            registerClass(c);
    }

}
