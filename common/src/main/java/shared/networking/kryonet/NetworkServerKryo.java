package shared.networking.kryonet;

import shared.networking.*;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import java.io.IOException;
import shared.networking.dto.BaseMessage;

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
        server.bind(NetworkConstants.TCP_PORT);
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

    @Override
    public Connection[] getConnections() {
        return server.getConnections();
    }
}
