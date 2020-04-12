package at.aau.server;

import at.aau.common.networking.NetworkServer;
import at.aau.common.networking.kryonet.NetworkServerKryo;

public class Main {

    public static void main(String[] args) {
        NetworkServer Server = new NetworkServerKryo();
        Server.registerCallback();
    }
}
