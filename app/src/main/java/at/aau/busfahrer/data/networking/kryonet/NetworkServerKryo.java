package at.aau.busfahrer.data.networking.kryonet;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;

import at.aau.busfahrer.data.networking.Callback;
import at.aau.busfahrer.data.networking.NetworkServer;
import at.aau.busfahrer.data.networking.dto.BaseMessage;

public class NetworkServerKryo implements NetworkServer, KryoNetComponent {
    private Server server;
    private Callback<BaseMessage> messageCallback;

    public NetworkServerKryo() {
        server = new Server();
    }

    public void registerClass(Class c) {
        server.getKryo().register(c);
    }

    public void start() throws IOException {
        server.start();
        server.bind(NetworkConstants.TCP_PORT, NetworkConstants.UDP_PORT);

        server.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (messageCallback != null && object instanceof BaseMessage)
                    messageCallback.callback((BaseMessage) object);
            }
        });
    }

    public void registerCallback(Callback<BaseMessage> callback) {
        this.messageCallback = callback;
    }

    public void broadcastMessage(BaseMessage message) {
        for (Connection connection : server.getConnections())
            connection.sendTCP(message);
    }
}