package shared.networking;

import java.io.IOException;

import com.esotericsoftware.kryonet.Connection;
import shared.networking.dto.BaseMessage;
import shared.networking.kryonet.KryoNetComponent;

import com.esotericsoftware.kryonet.Listener;

public interface NetworkServer extends KryoNetComponent {

    /**
     * Starts the Server.
     *
     * @throws IOException
     */
    void start() throws IOException;

    /**
     * Registers a callback which gets called if a message is received.
     *
     * @param callback
     */
    void registerCallback(Callback<BaseMessage> callback);

    /**
     * Sends a message to all clients.
     *
     * @param message
     */
    void broadcastMessage(BaseMessage message);
    void addListener(Listener listener);
    Connection[] getConnections();

}
