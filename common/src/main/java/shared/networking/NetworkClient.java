package shared.networking;

import java.io.IOException;
import shared.networking.dto.BaseMessage;

public interface NetworkClient {

    /**
     * Connects to a host.
     *
     * @param host
     * @throws IOException
     */
     void connect(String host) throws IOException;


    /**
     * Registers a callback which gets called if a message is received.
     * @param dtoClass
     * @param callback
     */
    void registerCallback(Class<?> dtoClass, Callback<BaseMessage> callback);

    /**
     * Sends a message to the server.
     *
     * @param message
     */
    void sendMessage(BaseMessage message);
    /**
     * Close connection.
     *
     */
    void close();
}
