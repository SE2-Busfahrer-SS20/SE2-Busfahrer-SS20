package shared.networking.kryonet;

import shared.networking.*;
import shared.networking.dto.TextMessage;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import shared.networking.dto.BaseMessage;
import shared.model.Player;

public class NetworkServerKryo implements NetworkServer, KryoNetComponent {

    private Server server;
    protected Callback<BaseMessage> messageCallback;


    public NetworkServerKryo() {
        server = new Server();
    }

    public void registerClass(Class c) {
        server.getKryo().register(c);
    }

    public void start() throws IOException {
        server.start();
        server.bind(NetworkConstants.TCP_PORT, NetworkConstants.UDP_PORT);
    }

    public void registerCallback(Callback<BaseMessage> callback) {
        this.messageCallback = callback;
    }

    public void broadcastMessage(BaseMessage message) {
        for (Connection connection : server.getConnections())
            connection.sendTCP(message);
    }

    @Override
    public void addListener(Listener listener) {
        server.addListener(listener);
    }

}
