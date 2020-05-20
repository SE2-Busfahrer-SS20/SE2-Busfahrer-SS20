package at.aau.common.shared.networking.kryonet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import shared.networking.NetworkClient;
import shared.networking.kryonet.NetworkClientKryo;

public class NetworkClientKryoTest {

    @Test
    public void checkSingletonImplementation() {
        NetworkClient client1 = NetworkClientKryo.getInstance();
        NetworkClient client2 = NetworkClientKryo.getInstance();
        Assert.assertEquals(client1, client2);
    }
}
